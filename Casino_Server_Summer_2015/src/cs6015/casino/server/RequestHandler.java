package cs6015.casino.server;



import java.io.IOException;
import java.net.Socket;
import java.util.Random;

import org.apache.log4j.Logger;

import cs6015.casino.blackjack.BlackJackGamePlay;
import cs6015.casino.exceptions.CasinoException;
import cs6015.casino.poker.PokerGamePlay;
import cs6015.casino.serializables.BlackJackMessage;
import cs6015.casino.serializables.Message;
import cs6015.casino.serializables.Player;
import cs6015.casino.serializables.PokerMessage;
import cs6015.casino.types.BlackJackType;
import cs6015.casino.types.ClientMessageType;
import cs6015.casino.types.PokerType;
import cs6015.casino.types.ServerMessageType;

/*
 * This class contains methods and techniques used in the following link:
 * http://math.hws.edu/javanotes/source/chapter12/netgame/
 */
public final class RequestHandler {
	private final static Logger log = Logger.getLogger(RequestHandler.class);

	private final int sessionId;
	private final Communication connection;
	private Player player;
	private final DatabaseHandler xml;
	private GamePlay<?> currentGame;

	public RequestHandler(Socket socket, DatabaseHandler xml) throws IOException 
	{
		sessionId = generateSessionId();
		connection = new Communication(socket, this);
		this.xml = xml;
	}

	public GamePlay<?> getCurrentGame() {
		return currentGame;
	}
	
	public void setCurrentGame(GamePlay<?> currentGame) {
		this.currentGame = currentGame;
	}

	public Player getPlayer() {
		return player;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((connection == null) ? 0 : connection.hashCode());
		result = prime * result + sessionId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RequestHandler other = (RequestHandler) obj;
		if (connection == null) {
			if (other.connection != null)
				return false;
		} else if (!connection.equals(other.connection))
			return false;
		if (sessionId != other.sessionId)
			return false;
		return true;
	}

	public void processMessage(Object value) 
	{
		Message<?> message = (Message<?>)value;
		String request = message.getClientMessageType();
		try
		{
			if(request.equals(ClientMessageType.REGISTER.toString()) || request.equals(ClientMessageType.LOGIN.toString()))
			{
				if(Authenticate(message))
				{

					Message<?> response = new Message<>(ServerMessageType.AUTHENTICATIONSUCCESS);
					response.setPlayerName(player.getPlayerName());
					response.setSessionId(player.getSessionId());
					response.setMoney(player.getMoney());
					send(response);

				}
				else
				{
					Message<?> response = new Message<>(ServerMessageType.AUTHENTICATIONFAIL);
					send(response);
				} 
			}else if(request.equals(ClientMessageType.POKER.toString()))
			{
				if(!isCurrentGamePoker())
				{
					if(!RequestHandler.registerPokerPlayer(this.player, this))
					{
						//TODO: send - cannot add the player all poker servers are busy.
					}	
				}
				else{
					this.currentGame.play(this.player, message);
				}
				
			}else if(request.equals(ClientMessageType.BLACKJACK.toString()))
			{
				if(!isCurrentGameBlackJack())
				{
					if(!RequestHandler.registerBlackJackPlayer(this.player, this))
					{
						//TODO: send - cannot add the player all poker servers are busy.
					}	
				}
				else{
					this.currentGame.play(this.player, message);
				}
				
			}else if(request.equals(ClientMessageType.SLOTS.toString()))
			{
				if(this.currentGame != null)
						this.exitGame();
				
				Message<?> response = new Message<>(ServerMessageType.PLAYSLOTS);
				send(response);
				
			}else if(request.equals(ClientMessageType.QUIT.toString()))
			{
				log.info(String.format("Quit message received from %s", this.player));
				xml.updateMoney(this.player.getPlayerName(), this.player.getMoney());
				Message<?> response = new Message<>(ServerMessageType.UPDATEMONEY);
				response.setPlayerName(player.getPlayerName());
				response.setSessionId(player.getSessionId());
				response.setMoney(player.getMoney());
				send(response);
				if(this.currentGame != null)
					this.exitGame();
			}
			else if(request.equals(ClientMessageType.UPDATEMONEY.toString()))
			{
				int money;
				this.player.setMoney(message.getMoney());
				xml.updateMoney(this.player.getPlayerName(), this.player.getMoney());
				Message<?> response = new Message<>(ServerMessageType.UPDATEMONEY);
				response.setPlayerName(player.getPlayerName());
				response.setSessionId(player.getSessionId());
				response.setMoney(player.getMoney());
				send(response);
			}
			
			//			else if(request.equals(RequestType.BLACKJACK.toString())){
			//				int blackJackPort = getAvailablePort(RequestType.BLACKJACK);
			//				if(blackJackPort < 1)
			//					throw new ServerException("No ports available for pokerhub");
			//				BlackJackGameCenter newHub = new BlackJackGameCenter(blackJackPort);
			//				newHub.setPlayerMoney(player.getMoney());
			//				RequestMessage response = new RequestMessage(ResponseType.PLAYBLACKJACK);
			//				response.setPokerPort(newHub.connectionPort);
			//				sendToConnectedClient(response);
			//			}
			//			else if(request.equals(RequestType.UPDATEMONEY.toString()))
			//			{
			//				if(checkLoggedInUsers(message.getPlayerId(), message.getSessionId(), message.getMoney()))
			//				 {
			//					 player.setMoney(message.getMoney());
			//					 RequestMessage response = new RequestMessage(ResponseType.AUTHENTICATIONSUCCESS);
			//					 response.setPlayerId(player.playerId);
			//					 response.setSessionId(player.sessionId);
			//					 response.setMoney(player.money);
			//					 sendToConnectedClient(response);
			//					 xml.updateMoney(player.playerId, player.money);
			//				 }
			//				 else
			//				 {
			//					 RequestMessage response = new RequestMessage(ResponseType.AUTHENTICATIONFAIL);
			//					 sendToConnectedClient(response);
			//					 sendToConnectedClient(new DisconnectMessage("disconnect"));
			//				 }
			//			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{

		}
	} 
	
	public void send(Message<?> msg)
	{
		this.connection.sendToClient(msg);
	}

	public int generateSessionId()
	{
		Random r = new Random();
		int Low = 1000;
		int High = 9999;
		return r.nextInt(High-Low) + Low;
	}

	/*
	 * Verify whether the current game is poker
	 */
	private boolean isCurrentGamePoker()
	{
		if(this.currentGame == null)
		{
			return false;
		}
		else if(!(this.currentGame instanceof PokerGamePlay))
		{
			this.exitGame();
			return false;
		}

		return true;
	}
	
	/*
	 * Verify whether the current game is poker
	 */
	private boolean isCurrentGameBlackJack()
	{
		if(this.currentGame == null)
		{
			return false;
		}
		else if(!(this.currentGame instanceof BlackJackGamePlay))
		{
			this.exitGame();
			return false;
		}

		return true;
	}

	/*
	 * Exit this player from current game 
	 */
	private void exitGame()
	{
		this.currentGame.exitGame(this);
		this.currentGame = null;
	}

	private boolean Authenticate(Message<?> message) throws IndexOutOfBoundsException, ServerException
	{
		if(message.getClientMessageType().equals(ClientMessageType.REGISTER.toString()))
		{
			xml.addPlayer(message.getPlayerName(), message.getMoney());
			if(! checkDb(message.getPlayerName()))
			     return false;	
		}
		else if(message.getClientMessageType().equals(ClientMessageType.LOGIN.toString()))
		{
			if(! checkLoggedInUsers())
			{
				if(! checkDb(message.getPlayerName()))
					return false;	
			} 
		}
		return true; 
	}

	public boolean checkLoggedInUsers()
	{
		if(ServerApplication.playersLoggedIn.contains(this))
			return true;

		return false;
	}

	public boolean checkDb(String playerName) throws IndexOutOfBoundsException, ServerException
	{
		Object[] val;

		if(xml.isPlayerRegistered(playerName))
		{
			val = xml.getPlayer(playerName);
			Player player = new Player(val[0]);
			player.setMoney((Integer)val[1]);
			player.setSessionId(this.sessionId);

			if(ServerApplication.playersLoggedIn.add(this))
			{
				this.player = player;
				return true;
			}
			else
			{
				throw new ServerException("Unable to add player to database.xml");
			}
		}
		return false;
	}

	/*----------------------------------------------------------------------------------------------------------------------------*/
	/*------------------------------------------------Static Classes are defined below--------------------------------------------*/
	/*----------------------------------------------------------------------------------------------------------------------------*/

	/*
	 * TODO : repeat the same for black jack
	 */
	private static synchronized boolean registerPokerPlayer(Player player, RequestHandler handler) 
	{
		try
		{
			PokerGamePlay availablePokerGame = ServerApplication.availableFreePokerGamePlay();

			if(availablePokerGame == PokerGamePlay.INVALID)
			{
				return false;
			}
			availablePokerGame.registerPlayerForGame(player, handler);
		}
		catch(CasinoException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static synchronized boolean registerBlackJackPlayer(Player player, RequestHandler handler) 
	{
		try
		{
			BlackJackGamePlay availableBlackJackGame = ServerApplication.availableFreeBlackJackGamePlay();

			if(availableBlackJackGame == BlackJackGamePlay.INVALID)
			{
				return false;
			}
			availableBlackJackGame.registerPlayerForGame(player, handler, true);
		}
		catch(CasinoException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
} // End of Request Handler
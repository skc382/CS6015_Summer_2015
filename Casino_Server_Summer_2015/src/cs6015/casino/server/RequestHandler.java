package cs6015.casino.server;



import java.io.IOException;
import java.net.Socket;
import java.util.Random;

import org.apache.log4j.Logger;

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

	private Player player;
	private DatabaseHandler xml;
	private Communication connection;
	private GamePlay<?> currentGame;

	public RequestHandler(Socket socket) throws IOException 
	{
		connection = new Communication(socket, this);
	}

	public GamePlay<?> getCurrentGame() {
		return currentGame;
	}

	public void setCurrentGame(GamePlay<?> currentGame) {
		this.currentGame = currentGame;
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
					// sendToConnectedClient(response);
				}
				else
				{
					//					 RequestMessage response = new RequestMessage(ResponseType.AUTHENTICATIONFAIL);
					//					 sendToConnectedClient(response);
					//					 sendToConnectedClient(new DisconnectMessage("disconnect"));
				} 
			}
			else if(request.equals(ClientMessageType.POKER.toString()))
			{
				if(!validateCurrentGameAsPoker())
				{
					if(!RequestHandler.registerPokerPlayer(this.player, this))
					{
						//send - cannot add the player all poker servers are busy.
					}
				}
				//this.currentGame.//

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
	private boolean validateCurrentGameAsPoker()
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
	 * Exit this player from current game 
	 */
	private void exitGame()
	{
		//this.currentGame.exitGame(this)
		//this.currentGame == null;
	}

	private boolean Authenticate(Message<?> message) throws IndexOutOfBoundsException, ServerException
	{
		if(message.getClientMessageType().equals(ClientMessageType.REGISTER.toString()))
		{
			xml.addPlayer(message.getPlayerName(), message.getMoney());
			//	if(! checkDb(message.getPlayerId()))
			return false;	
		}
		else if(message.getClientMessageType().equals(ClientMessageType.LOGIN.toString()))
		{
			if(! checkLoggedInUsers(message.getPlayerName(), message.getSessionId(), message.getMoney()))
			{
				if(! checkDb(message.getPlayerName()))
					return false;	
			} 
		}
		return true; 
	}

	public boolean checkLoggedInUsers(String playerName, int sessionId, int money)
	{
		Player player = new Player(playerName);
		player.setSessionId(sessionId);
		player.setMoney(money);


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
			player.setSessionId(generateSessionId());

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

			if(availablePokerGame.equals(PokerGamePlay.INVALID))
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

} // End of Request Handler
package cs6015.casino.client;

import org.apache.log4j.Logger;

import cs6015.casino.blackjack.BlackJackController;
import cs6015.casino.dashboard.DashBoardView;
import cs6015.casino.login.LoginController;
import cs6015.casino.poker.PokerController;
import cs6015.casino.serializables.BlackJackMessage;
import cs6015.casino.serializables.Message;
import cs6015.casino.serializables.Player;
import cs6015.casino.serializables.PokerMessage;
import cs6015.casino.slots.SlotsController;
import cs6015.casino.types.ClientMessageType;
import cs6015.casino.types.ServerMessageType;

public final class Controller {
	private final static Logger log = Logger.getLogger(Communication.class);
	
	private static final int DEFAULT_MONEY = 2000;
	private static Controller instance = null;
	private DashBoardView dashBoard;
	private Player player;
	private Communication connection;
	private LoginController loginController;
	private PokerController pokerController;
	private BlackJackController blackJackController;
	private SlotsController slotsController;	

	private Controller(LoginController loginController)
	{
		this.loginController = loginController;
		dashBoard = new DashBoardView(this);
		connection = Communication.getInstance(this);
	}

	public static Controller getInstance(LoginController loginController)
	{
		if(instance == null)
			instance = new Controller(loginController);
		return instance;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void setPlayerMoney(int money)
	{
		this.player.setMoney(money);
	}

	public void processMessage(Message<?> message)
	{
		try
		{
			if(message.getServerMessageType().equals(ServerMessageType.AUTHENTICATE.toString()))
			{
				Message<?> response;
				if(this.loginController.isRegister())
				{
					response = new Message<>(ClientMessageType.REGISTER);
					response.setMoney(DEFAULT_MONEY);
				}
				else
				{
					response = new Message<>(ClientMessageType.LOGIN);
				}
				response.setPlayerName(this.loginController.getPlayer().getPlayerName());
				send((Message<?>)response);
			}
			else if((message.getServerMessageType().equals(ServerMessageType.AUTHENTICATIONSUCCESS.toString())))
			{
				this.player = new Player(message.getPlayerName());
				this.player.setSessionId(message.getSessionId());
				this.player.setMoney(message.getMoney());
				
				this.dashBoard.updateDashBoardFields(this.player.getPlayerName(), 
						                     Integer.toString(this.player.getMoney()));   //update the dash board fields 
				this.dashBoard.refreshView(); 											  // refresh dash board
			}
			else if((message.getServerMessageType().equals(ServerMessageType.AUTHENTICATIONFAIL.toString())))
			{
				this.loginController.invalidUserDialog();			
			}
			else if((message.getServerMessageType().equals(ServerMessageType.UPDATEMONEY.toString())))
			{
				this.player = new Player(message.getPlayerName());
				this.player.setSessionId(message.getSessionId());
				this.player.setMoney(message.getMoney());
				
				this.dashBoard.updateDashBoardFields(this.player.getPlayerName(), 
	                     Integer.toString(this.player.getMoney()));   //update the dash board fields 
				this.dashBoard.refreshView(); 											  // refresh dash board
			}
			else if((message.getServerMessageType().equals(ServerMessageType.PLAYPOKER.toString())))
			{
				PokerMessage<?> pokerMessage = (PokerMessage<?>)message.getGameMessage();
				this.pokerController.processPokerMessage(pokerMessage);
			}
			else if((message.getServerMessageType().equals(ServerMessageType.PLAYBLACKJACK.toString())))
			{
				BlackJackMessage<?> blackJackMessage = (BlackJackMessage<?>)message.getGameMessage();
				this.blackJackController.processBlackJackMessage(blackJackMessage);
			}
			else if((message.getServerMessageType().equals(ServerMessageType.PLAYSLOTS.toString())))
			{
				this.slotsController.display();
			}
			
		}
		catch(NullPointerException e)
		{
			log.error(e);
			e.printStackTrace();
		}
	}
	
	public void updateMoney()
	{
		Message<?> msg = new Message<>(ClientMessageType.UPDATEMONEY);
		msg.setPlayerName(this.player.getPlayerName());
		msg.setSessionId(this.player.getSessionId());
		msg.setMoney(this.player.getMoney());
		send(msg);
	}

	public void send(Message<?> message)
	{
		this.connection.sendToServer(message);
	}
	
	public void closingServerConnection()
	{
		Message<?> msg = new Message<>(ClientMessageType.CLOSECONNECTION);
		this.send(msg);
	}
	
	public void registerPokerController()
	{
		this.registerControllers(new PokerController(this));
		send(new Message<>(ClientMessageType.POKER));
	}
	
	public void registerBlackJackController()
	{
		this.registerControllers(new BlackJackController(this));
		send(new Message<>(ClientMessageType.BLACKJACK));
	}
	
	public void registerSlotsController()
	{
		this.registerControllers(new SlotsController(this));
		send(new Message<>(ClientMessageType.SLOTS));
	}
	
	public <T> void registerControllers(T controller)
	{
		if(controller instanceof PokerController)
		{
			this.pokerController = (PokerController)controller;
		}
		else if(controller instanceof BlackJackController)
		{
			this.blackJackController = (BlackJackController) controller;
		}
		else if(controller instanceof SlotsController)
		{
			this.slotsController = (SlotsController) controller;
		}
	}

	public <T> void De_RegisterControllers(T controller)
	{
		if(controller instanceof PokerController)
		{
			this.pokerController = null;
		}
		else if(controller instanceof BlackJackController)
		{
			this.blackJackController = null;
		}
		else if(controller instanceof SlotsController)
		{
			this.slotsController = null;
		}

		this.dashBoard = new DashBoardView(this);
		this.dashBoard.updateDashBoardFields(this.player.getPlayerName(), 
                Integer.toString(this.player.getMoney())); 
		this.dashBoard.refreshView();
	}

} //End of Controller

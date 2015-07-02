package cs6015.casino.login;

import cs6015.casino.client.Controller;
import cs6015.casino.serializables.Player;

public class LoginController {
	
	private static LoginController instance = null;
	private final LoginView view;
	private Player player;
	private Controller controller;
	public boolean register;
	
	private LoginController(String playerName, LoginView view) {
		setPlayer(new Player(playerName));
		this.controller = Controller.getInstance(this);
		this.view = view;
	}

	public static LoginController getInstance(String playerName, LoginView view)
	{
		if(instance == null)
			instance = new LoginController(playerName, view);
		return instance;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player)
	{
		this.player = player;
	}
	
	public void setPlayerName(String playerName) {
		this.player.setPlayerName(playerName);
	}
	
	public boolean isRegister() {
		return register;
	}

	public void setRegister(boolean register) {
		this.register = register;
	}
	
	public void invalidUserDialog()
	{
		this.view.invalidUserDialog();
	}
	
	public void  updateLoginPanel()
	{
		this.view.refreshView();
	}
	
	public void closingAction()
	{
		this.controller.closingServerConnection();
	}
}

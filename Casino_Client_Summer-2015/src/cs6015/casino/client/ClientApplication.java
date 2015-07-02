package cs6015.casino.client;

import org.apache.log4j.Logger;
import cs6015.casino.login.LoginView;

public class ClientApplication {
	private final static Logger log = Logger.getLogger(ClientApplication.class);
	
	public static void main(String[] args) 
	{
		try
		{
			log.info("/******************************************************/");
			log.info("Client starting");
			log.info("/******************************************************/");
			
			LoginView login = new LoginView();
			login.loadView();
		}
		catch(Exception e)
		{
			log.error(e);
			e.printStackTrace();
		}
	}
}

package cs6015.casino.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import cs6015.casino.blackjack.BlackJackGamePlay;
import cs6015.casino.poker.PokerGamePlay;
import cs6015.casino.types.ClientMessageType;


public final class ServerApplication {
	private final static Logger log = Logger.getLogger(ServerApplication.class);

	public static Set<RequestHandler> playersLoggedIn;
	private static Set<PokerGamePlay> pokerGamesEngaged;
	private static Set<BlackJackGamePlay> blackJackGamesEngaged;
	private ServerSocket mainServerSocket;
	private DatabaseHandler xml;
	
	ServerApplication() throws IOException
	{
		mainServerSocket = new ServerSocket(9838);
		playersLoggedIn = Collections.newSetFromMap(new ConcurrentHashMap<RequestHandler, Boolean>());
		pokerGamesEngaged = Collections.newSetFromMap(new ConcurrentHashMap<PokerGamePlay, Boolean>());
		blackJackGamesEngaged = Collections.newSetFromMap(new ConcurrentHashMap<BlackJackGamePlay, Boolean>());
		startGameBoards();

//		xml = new DatabaseHandler();
//		Thread readerThread = new Thread(){
//			public void run() {
//				while (true) {
//					try {
//						xml.writeXmlToFile();
//						Thread.sleep(60*1000);
//					}
//					catch (Exception e) {
//						System.out.println("Exception while handling received message:");
//						e.printStackTrace();
//					}
//				}
//			}
//		};
//		readerThread.setDaemon(true);
//		readerThread.start();

		while(true)
		{
			Socket newSocket = mainServerSocket.accept();
			playersLoggedIn.add(new RequestHandler(newSocket));
		}

	}


	public static void main(String args[])
	{
		try {

			log.info("/******************************************************/");
			log.info("Server starting");
			log.info("/******************************************************/");
			//new ServerApplication();


		} catch (Exception e) {
			log.equals(e);
			e.printStackTrace();
		}
	}

	private void startGameBoards()
	{
		int i = 1;
		log.info("Starting poker game and black jack game servers");
		while(i <= 3)
		{
			pokerGamesEngaged.add(PokerGamePlay.getInstances(ClientMessageType.POKER.toString(), i));
			blackJackGamesEngaged.add(BlackJackGamePlay.getInstances(ClientMessageType.BLACKJACK.toString(), i));
			i++;
		}
		log.info("Servers started");
	}

	public static synchronized PokerGamePlay availableFreePokerGamePlay()
	{
		for(PokerGamePlay pokerGame : pokerGamesEngaged)
		{
			if(!pokerGame.isGameFull())
			{
				return pokerGame;
			}
		}

		return PokerGamePlay.INVALID;
	}

}

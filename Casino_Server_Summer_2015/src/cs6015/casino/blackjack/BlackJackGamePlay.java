package cs6015.casino.blackjack;

import java.util.HashMap;

import org.apache.log4j.Logger;

import cs6015.casino.serializables.Message;
import cs6015.casino.serializables.Player;
import cs6015.casino.server.GamePlay;
import cs6015.casino.server.RequestHandler;
import cs6015.casino.types.BlackJackType;

public final class BlackJackGamePlay extends GamePlay<BlackJackType>{
	private final static Logger log = Logger.getLogger(BlackJackGamePlay.class);

	private static HashMap<String, BlackJackGamePlay> blackJackGamesInstances;

	protected BlackJackGamePlay(String gameName, int gameId) {
		super(gameName, gameId);
		// TODO Auto-generated constructor stub
	}
	
	public static BlackJackGamePlay getInstances(String gameName, int gameId)
	{
		if(blackJackGamesInstances.isEmpty() || !blackJackGamesInstances.containsKey(gameName))
			blackJackGamesInstances.put(gameName, new BlackJackGamePlay(gameName, gameId));
		
		return blackJackGamesInstances.get(gameName);
	}

	@Override
	protected void initializeTableCards() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void BeginGamePlay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void play(Player player, Message<?> msg) {
		// TODO Auto-generated method stub
		
	}

	



}

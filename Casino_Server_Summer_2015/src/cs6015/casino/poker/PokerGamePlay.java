package cs6015.casino.poker;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import cs6015.casino.serializables.Card;
import cs6015.casino.serializables.GameState;
import cs6015.casino.serializables.Message;
import cs6015.casino.serializables.BlackJackMessage;
import cs6015.casino.serializables.PokerMessage;
import cs6015.casino.serializables.Player;
import cs6015.casino.serializables.PokerMessage.PokerEnum;
import cs6015.casino.server.GamePlay;
import cs6015.casino.server.RequestHandler;
import cs6015.casino.types.PokerType;
import cs6015.casino.types.ServerMessageType;

/*
 * This class contains methods and techniques used in the following link:
 * http://math.hws.edu/javanotes/source/chapter12/netgame/
 */
public final class PokerGamePlay extends GamePlay<PokerType>{
	private final static Logger log = Logger.getLogger(PokerGamePlay.class);

	public static final PokerGamePlay INVALID = null;
	private static final int MAX_TABLE_CARD_SIZE = 5;
	private static HashMap<String, PokerGamePlay> PokerGamesInstances;
	private boolean canPlay;
	private int potMoney;


	private PokerGamePlay(String gameName, int gameId)
	{
		super(gameName, gameId);
		canPlay = false;
		log.info(String.format("Started game : %s-%d server.", this.gamePlayName, this.gamePlayId));
	}

	public static PokerGamePlay getInstances(String gameName, int gameId)
	{
		if(PokerGamesInstances.isEmpty() || !PokerGamesInstances.containsKey(gameName))
			PokerGamesInstances.put(gameName, new PokerGamePlay(gameName, gameId));

		return PokerGamesInstances.get(gameName);
	}

	@Override
	protected void BeginGamePlay() {
		log.info(String.format("Beginning game play of %s-%d", this.gamePlayName, this.gamePlayId));
		Player nextPlayer;

		this.potMoney = 0;								 // initialize money on table to zero

		this.loadPlayerList();
		this.currentPlayer = this.playerList.get(0);     //get the first player
		this.dealCardToPlayer(this.currentPlayer);		 //Deal card to the first player

		nextPlayer = this.getNextPlayer(currentPlayer); //get next player
		this.dealCardToPlayer(nextPlayer);				//Deal card to the second player

		nextPlayer = this.getNextPlayer(nextPlayer);	//get next player
		this.dealCardToPlayer(nextPlayer);				//Deal card to the third player

		this.sendPokerMessageToALL(this.getNewPokerMessage(this.getNewPokerState("WAIT")));
		this.sendPokerMessage(this.currentPlayer, this.getNewPokerMessage(PokerEnum.NO_FOLD));

		canPlay = true;
	}



	@Override
	protected void play(Player player, Message<?> msg) {

		if(canPlay)
		{

		}
		else
		{
			//TODO: Send to all that players are being added
		}

	}

	@Override
	protected void initializeTableCards() {
		this.tableCards = new ArrayList<Card>(MAX_TABLE_CARD_SIZE);
		for(int i=0; i<MAX_TABLE_CARD_SIZE; i++)
			this.tableCards.add(Card.NULL_CARD);
	}

	private GameState<PokerType> getNewPokerState(String gameStatus)
	{
		if(this.tableCards == null || this.playerList.get(0) == null || this.playerList.get(1) == null || this.playerList.get(2) == null)
		{
			throw new NullPointerException("Either PlayerList or TableCards are Null.");
		}
		return new GameState<PokerType>(this.tableCards, this.playerList.get(0), this.playerList.get(1), this.playerList.get(2), this.potMoney, gameStatus);
	}
	
	private PokerMessage<?> getNewPokerMessage(GameState<PokerType> pokerState)
	{
		return new PokerMessage<>(pokerState);
	}
	
	private <T> PokerMessage<?> getNewPokerMessage(T value, GameState<PokerType> pokerState)
	{
		return new PokerMessage<T>(value, pokerState);
	}
	
	private <T> PokerMessage<?> getNewPokerMessage(T value)
	{
		return new PokerMessage<T>(value);
	}

	private void sendPokerMessage(Player player, PokerMessage<?> pokerMessage)
	{
		Message<PokerMessage<?>> msg = new Message<PokerMessage<?>>(ServerMessageType.PLAYPOKER);
		msg.setGameMessage(pokerMessage);
		this.sendMessageToClient(player, msg);
	}
	
	private void sendPokerMessageToALL(PokerMessage<?> pokerMessage)
	{
		Message<PokerMessage<?>> msg = new Message<PokerMessage<?>>(ServerMessageType.PLAYPOKER);
		msg.setGameMessage(pokerMessage);
		this.sendToAllClients(msg);
	}

}// End of PokerGamePlay

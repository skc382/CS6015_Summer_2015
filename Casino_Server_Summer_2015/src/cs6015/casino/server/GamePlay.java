package cs6015.casino.server;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import cs6015.casino.common.Deck;
import cs6015.casino.exceptions.CasinoException;
import cs6015.casino.serializables.Card;
import cs6015.casino.serializables.Message;
import cs6015.casino.serializables.Player;
import cs6015.casino.types.GameType;

/*
 * This class contains methods and techniques used in the following link:
 * http://math.hws.edu/javanotes/source/chapter12/netgame/
 */
public abstract class GamePlay<T extends GameType> {
	private final static Logger log = Logger.getLogger(GamePlay.class);
	
	protected final static int TOTAL_NUMBER_OF_PLAYERS = 3;
	protected ConcurrentHashMap<Player, RequestHandler> playerConnections;	
	protected ArrayList<Player> playerList;
	protected String gamePlayName;
	protected int gamePlayId;
	protected boolean canPlay;
	protected Deck deck;
	protected ArrayList<Card> tableCards;
	protected Player currentPlayer;
	protected Player firstPlayer;

	protected GamePlay(String gameName, int gameId)
	{
		this.gamePlayName = gameName;
		this.gamePlayId = gameId;
		playerConnections = new ConcurrentHashMap<Player, RequestHandler>();
		deck = new Deck();
	}
	
	public int getPlayerConnectionsSize()
	{
		return playerConnections.size();
	}
	
	public boolean isGameFull()
	{
		return allPlayersConnected();
	}
	
	protected boolean allPlayersConnected()
	{
		if(playerConnections.size() != TOTAL_NUMBER_OF_PLAYERS)
		{
			return false;
		}
		return true;
	}
	
	/*
	 * Register the player to the current game and Assign current game to the request handler
	 */
	protected void registerPlayerForGame(Player player, RequestHandler handler) throws CasinoException
	{
		if(allPlayersConnected())
		{
			throw new CasinoException(String.format("Cannot add Player: %s because the game: %s is full with players", player.getPlayerName(), this.gamePlayName));
		}
		playerConnections.put(player, handler);
		handler.setCurrentGame(this); //Assign current game to the request handler
		sendWaitMessage(player,"Waiting for other Players to connect");
		
		if(allPlayersConnected())
		{
			this.BeginGamePlay();  //Start the game
		}
	}
	
	protected void  registerPlayerForGame(Player player, RequestHandler handler, boolean singlePlayer) throws CasinoException
	{
		if(this.playerConnections.size() >= 1)
		{
			throw new CasinoException(String.format("Cannot add Player: %s because the game: %s is already engaged", player.getPlayerName(), this.gamePlayName));
		}
		playerConnections.put(player, handler);
		handler.setCurrentGame(this); //Assign current game to the request handler
		
		this.BeginGamePlay();  //Start the game
	}
	
	protected void sendMessageToClient(Player player, Message<?> msg) {
		RequestHandler handler = this.playerConnections.get(player);
		handler.send(msg);
	}

	protected void sendToAllClients(Message<?> msg) {
		Enumeration<RequestHandler> en = this.playerConnections.elements();
		while(en.hasMoreElements())
		{
			RequestHandler handler = en.nextElement();
			handler.send(msg);
		}
	}

	protected void loadPlayerList() {
		this.playerList = new ArrayList<Player>();
		Iterator<Entry<Player, RequestHandler>> it = this.playerConnections.entrySet().iterator();
		while(it.hasNext())
		{
			Entry<Player, RequestHandler> element = it.next();
			this.playerList.add(element.getKey());
		}
	}

	protected Player getNextPlayer(Player currentPlayer) {
		int index, newIndex;
		if(currentPlayer == null)
			throw new NullPointerException("No player at getPlayerIndex()");
		
		index = this.playerList.indexOf(currentPlayer);
		newIndex = (index + 1) % this.playerList.size();
		return this.playerList.get(newIndex);
	}
	
	protected int getPlayerIndex(Player player) throws CasinoException
	{
		if(player == null)
			throw new NullPointerException("No player at getPlayerIndex()");
		
		if(!this.playerList.contains(player))
			throw new CasinoException("Cannot get player index coz The player: "+ player.getPlayerName() 
												+ " does not exist in the playerList of " + this.gamePlayName + "-" + this.gamePlayId);
		
		return (this.playerList.indexOf(player) + 1); // Adding one of index because array list is zero based
	}

	protected void dealCardToPlayer(Player player) {
		Card card1, card2;
		this.deck.shuffle();
		card1 = this.deck.deal();
		card2 = this.deck.deal();
		player.setHand(card1, card2);
	}
	
	protected abstract void initializeTableCards();
	
	protected abstract <K> K addToTableCards(Card card) throws CasinoException;
	
	protected abstract void BeginGamePlay();

	protected abstract void play(Player player, Message<?> msg);
	
	protected abstract void sendWaitMessage(Player player, String message);
	
	protected abstract void exitGame(RequestHandler handler);
}

package cs6015.casino.poker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.Logger;

import cs6015.casino.exceptions.CasinoException;
import cs6015.casino.serializables.Card;
import cs6015.casino.serializables.GameState;
import cs6015.casino.serializables.GameState.GameStatusEnum;
import cs6015.casino.serializables.Message;
import cs6015.casino.serializables.MyEntry;
import cs6015.casino.serializables.Player;
import cs6015.casino.serializables.PokerMessage;
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
	private static final int MAX_ROUNDS = 3;
	private static final int MAX_TABLE_CARD_SIZE = 5;
	private static final HashMap<String, PokerGamePlay> PokerGamesInstances = new HashMap<String, PokerGamePlay>();
	private boolean canPlay;
	private int potMoney;
	private int currentRound;
	private int minimumBet;

	private PokerGamePlay(String gameName, int gameId)
	{
		super(gameName, gameId);
		canPlay = false;
		log.info(String.format("Started game : %s-%d server.", this.gamePlayName, this.gamePlayId));
	}

	public static PokerGamePlay getInstances(String gameName, int gameId)
	{
		if(PokerGamesInstances.isEmpty() || !PokerGamesInstances.containsKey(String.format("%s-%d", gameName, gameId)))
			PokerGamesInstances.put(String.format("%s-%d", gameName, gameId), new PokerGamePlay(gameName, gameId));

		return PokerGamesInstances.get(String.format("%s-%d", gameName, gameId));
	}

	@Override
	protected void BeginGamePlay() {
		log.info(String.format("Beginning game play of %s-%d", this.gamePlayName, this.gamePlayId));
		Player nextPlayer;

		this.initializeTableCards();				     // Assign null cards to table cards to initialize all the 5 elements
		this.tableCards.add(this.deck.deal());
		this.tableCards.add(this.deck.deal());
		this.tableCards.add(this.deck.deal());  		// Dealt three cards

		this.potMoney = 0;								 // initialize money on table to zero
		this.currentRound = 1;							 // Mark as first round.

		this.loadPlayerList();
		this.currentPlayer = this.playerList.get(0);     //get the first player
		this.firstPlayer = this.currentPlayer;			 // Set the First player, a placeholder to keep track of first player.
		this.dealCardToPlayer(this.currentPlayer);		 //Deal card to the first player

		nextPlayer = this.getNextPlayer(currentPlayer); //get next player
		this.dealCardToPlayer(nextPlayer);				//Deal card to the second player

		nextPlayer = this.getNextPlayer(nextPlayer);	//get next player
		this.dealCardToPlayer(nextPlayer);				//Deal card to the third player

		this.sendPokerGameStateToALL(GameStatusEnum.UPDATE_ALL.toString(), true, 
				String.format("%s's turn to play", this.currentPlayer.getPlayerName()),
				PokerEnum.NOT_YOUR_TURN.toString()); //send to all the player saying who is the player 
		this.sendPokerMessage(this.currentPlayer, this.getNewPokerMessage("Please Bet or Check"
				, PokerEnum.BET_CHECK.toString()));  // Indicate first player to start the game

		canPlay = true;
	}



	@Override
	protected void play(Player player, Message<?> message)  {

		PokerMessage<?> pkrMsg = (PokerMessage<?>)message.getGameMessage();
		try{
			if(canPlay)
			{
				if(player.equals(this.currentPlayer))
				{
					if(pkrMsg.getType().equals(PokerEnum.BET_CHECK.toString()))
					{
						int playerMoney;

						if(!(pkrMsg.getMessage() instanceof Integer))
							throw new CasinoException("Poker message does not contain integer. "
									+ "Please check on the client end of Player: "+ player.getPlayerName());

						playerMoney = (Integer)pkrMsg.getMessage();
						this.potMoney = this.potMoney + playerMoney;
						this.currentPlayer.setMoney((this.currentPlayer.getMoney()-playerMoney));
						this.minimumBet = playerMoney;

					}
					else if(pkrMsg.getType().equals(PokerEnum.FOLD.toString()))
					{
						this.currentPlayer.setMoney(1000999);
						this.sendPokerGameStateToALL(GameStatusEnum.UPDATE_ALL.toString(), false, 
								"",	PokerEnum.POKERSTATE.toString()); 
						this.sendPokerMessage(this.currentPlayer, this.getNewPokerMessage("YOU FOLDED", PokerEnum.NOT_YOUR_TURN.toString()));
						this.currentPlayer = this.getNextPlayer(player);
						this.playerList.remove(player);
						
					}

					int round, playerIndex;
					round = this.currentRound;
					playerIndex = this.getPlayerIndex(this.currentPlayer);

					switch(round)
					{
					case 1: switch(playerIndex)
					{
					case 1 : this.playerActionHandler_1_2(PokerEnum.BET_CHECK.toString(), "Please Bet or Check"); // player 1&2 method (no fold)
					break;
					case 2 : this.playerActionHandler_1_2(PokerEnum.BET_CHECK.toString(), "Please Bet or Check"); // player 1&2 method (no fold)
					break;
					case 3 : this.playerActionHandler_3(PokerEnum.BET_CHECK_FOLD.toString(), "Please bet, check or Fold");
					break;
					default : throw new IllegalArgumentException("The player index is incorrect in round 1");
					}
					break;

					case 2: switch(playerIndex)
					{
					case 1 : this.playerActionHandler_1_2(PokerEnum.BET_CHECK_FOLD.toString(), "Please bet, check or fold");
					break;
					case 2 : this.playerActionHandler_1_2(PokerEnum.BET_CHECK_FOLD.toString(), "Please bet, check or fold");
					break;
					case 3 :  this.playerActionHandler_3(PokerEnum.BET_CHECK_FOLD.toString(), "Please bet, check or Fold");
					break;
					default : throw new IllegalArgumentException("The player index is incorrect in round 2");
					}
					break;

					case 3: switch(playerIndex)
					{
					case 1 : this.playerActionHandler_1_2(PokerEnum.BET_CHECK_FOLD.toString(), "Please bet, check or fold");
					break;
					case 2 : this.playerActionHandler_1_2(PokerEnum.BET_CHECK_FOLD.toString(), "Please bet, check or fold");
					break;
					case 3 : findTheWinner();
						break;
					default : throw new IllegalArgumentException("The player index is incorrect in round 3");
					}
					break;

					default : throw new IllegalArgumentException("Illegal round number");
					}
				}
				else
				{
					//TODO: send to this player that it is not his turn to play.
					// And send wait message.
					throw new CasinoException("Request from wrong player. current player is "+ this.currentPlayer);
				}
			}
			else
			{
				//TODO: Send to all that players are being added
				//sendWaitMessage(player);
			}
		}
		catch(CasinoException e)
		{
			log.error(e);
			return;
		}
	}

	private void findTheWinner() throws CasinoException
	{
		HashSet<Player> tiebreakerList;
		Player winner = null;
		int maxRank = 0;
		
		tiebreakerList = new HashSet<Player>();
		Iterator<Player> it = this.playerList.iterator();
		while(it.hasNext()){
			Player player = it.next();
			int rank = 0;
			
			if(tiebreakerList.size() < 1)
				tiebreakerList.add(player);
			
			rank = PokerEval.getRank(this.bestHandOfThePlayer(player)).rank();
			
			if(rank > maxRank){
				maxRank = rank;
				winner = player;
			}
			else if(rank == maxRank){
				tiebreakerList.add(player);
			}
		}
		
		// If there is a tie 
		if(tiebreakerList.size() > 1){
			Iterator<Player> ite = tiebreakerList.iterator();
			maxRank = 0;
			while(ite.hasNext()){
				Player player;
				int rank = 0;
				ArrayList<Card> playerHand, sortedHand;
				
				player = ite.next();
				playerHand = this.bestHandOfThePlayer(player);
				sortedHand = PokerEval.sortCards(playerHand);
				
				// compare the rank of the highest card
				rank = PokerEval.numVal(sortedHand.get(0));
				if(rank >= maxRank){
					maxRank = rank;
					winner = player;
				}
			}
		}
		
		this.declareTheWinner(winner);
	}
	
	private void declareTheWinner(Player winner)
	{
		if(winner == null){
			throw new NullPointerException("Winner is null");
		}
		Iterator<Player> iter = this.playerList.iterator();
		while(iter.hasNext()){
			Player player = iter.next();
			
			if(player.equals(winner)){
				this.sendPokerMessage(player, this.getNewPokerMessage(String.format("Congratulations you won %d", this.potMoney)
						, this.getNewPokerState(player, GameStatusEnum.WON.toString()), PokerEnum.NOT_YOUR_TURN.toString()));
			}
			else{
				this.sendPokerMessage(player, this.getNewPokerMessage(String.format("%s won the game", winner.getPlayerName())
						, this.getNewPokerState(player, GameStatusEnum.LOST.toString()), PokerEnum.NOT_YOUR_TURN.toString()));
			}
		}
	}

	private ArrayList<Card> bestHandOfThePlayer(Player player) throws CasinoException
	{
		ArrayList<Card> tableCardsTEMP, bestHand, tempHand, playerHand;
		Card playerCard1, playerCard2;

		playerHand = player.getHand();
		playerCard1 = playerHand.get(0);
		playerCard2 =  playerHand.get(1);

		tableCardsTEMP = new ArrayList<Card>();
		bestHand = new ArrayList<Card>();
		tableCardsTEMP.addAll(this.tableCards);
		tableCardsTEMP = PokerEval.sortCards(tableCardsTEMP);
		while(tableCardsTEMP.size() >= 3)
		{
			Card card1, card2, card3;
			int tempRank, bestRank;
			card1 = tableCardsTEMP.get(0);
			card2 = tableCardsTEMP.get(1);
			card3 = tableCardsTEMP.get(2);

			tempHand = new ArrayList<Card>();
			tempHand.add(card1);
			tempHand.add(card2);
			tempHand.add(card3);
			tempHand.add(playerCard1);
			tempHand.add(playerCard2);
			tempHand =  PokerEval.sortCards(tempHand);
			
			tempRank = PokerEval.getRank(tempHand).rank();
			bestRank = PokerEval.getRank(bestHand).rank();
			if( tempRank >= bestRank ){
				bestHand = tempHand;
			}
			tableCardsTEMP.remove(0);
		}
		
		return bestHand;
	}

	private void playerActionHandler_1_2(String pkrMsgType, String message)
	{
		this.currentPlayer = this.getNextPlayer(this.currentPlayer); // update current player

		MyEntry<String, Integer> pokerInnerMessage = new MyEntry<String, Integer>(String.format("%s's turn to play", 
				this.currentPlayer.getPlayerName()),this.potMoney);

		this.sendPokerMessageToALL(this.getNewPokerMessage(pokerInnerMessage
				, PokerEnum.NOT_YOUR_TURN.toString())); //Send to All indicating who is the current player
		this.sendPokerMessage(this.currentPlayer, this.getNewPokerMessage(message
				, pkrMsgType));	// Send to current player indicating his next action
		this.sendPokerMessage(this.currentPlayer, this.getNewPokerMessage(this.minimumBet
				, pkrMsgType));	// Send to current player minimum bet
	}

	private void playerActionHandler_3(String pkrMsgType, String message) throws CasinoException
	{
		if(this.currentRound < PokerGamePlay.MAX_ROUNDS)
			this.currentRound++;			     // update current round
		else
			throw new CasinoException(String.format("Cannot increment because current round value is %d", this.currentRound));

		this.currentPlayer = this.firstPlayer;  // Make first player as the current player for this new round
		this.addToTableCards(this.deck.deal()); // Deal a card from the deck and add it to the table cards

		this.sendPokerGameStateToALL(GameStatusEnum.UPDATE_TABLE.toString(), true, 
				String.format("%s turn to play", this.currentPlayer.getPlayerName()),
				PokerEnum.NOT_YOUR_TURN.toString()); // Update the cards on the table
		this.sendPokerMessage(this.currentPlayer, this.getNewPokerMessage(message
				, pkrMsgType));	// Send to current player indicating his next action
		this.minimumBet = 0; // Minimum bet becomes zero in new round
		this.sendPokerMessage(this.currentPlayer, this.getNewPokerMessage(this.minimumBet
				, pkrMsgType));	// Send to current player minimum bet
	}

	private GameState<PokerType> getNewPokerState(Player player, String gameStatus)
	{
		Player firstPlayer = player;
		Player secondPlayer = this.getNextPlayer(firstPlayer);
		Player thirdPlayer = this.getNextPlayer(secondPlayer);

		if(this.tableCards == null || firstPlayer == null || secondPlayer == null || thirdPlayer == null)
		{
			throw new NullPointerException("Either PlayerList or TableCards are Null.");
		}
		return new GameState<PokerType>(this.tableCards, firstPlayer, secondPlayer, thirdPlayer, this.potMoney, gameStatus);
	}

	private PokerMessage<?> getNewPokerMessage(GameState<PokerType> pokerState, String type)
	{
		return new PokerMessage<>(pokerState, type);
	}

	private <T> PokerMessage<?> getNewPokerMessage(T value, GameState<PokerType> pokerState, String type)
	{
		return new PokerMessage<T>(value, pokerState, type);
	}

	private <T> PokerMessage<?> getNewPokerMessage(T value, String type)
	{
		return new PokerMessage<T>(value, type);
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

//	private void sendPokerMessageToALL(PokerMessage<?> pokerMessage)
//	{
//		Message<PokerMessage<?>> msg = new Message<PokerMessage<?>>(ServerMessageType.PLAYPOKER);
//		msg.setGameMessage(pokerMessage);
//		for(Player player : this.playerList)
//		{
//			this.sendPokerMessage(player, pokerMessage);
//		}
//	}

	private <T> void sendPokerGameStateToALL(String gameStatus, boolean includePokerInnerMessage, T pokerInnerMessage, String pokerType)
	{

		for(Player player : this.playerList)
		{
			GameState<PokerType> pokerState = this.getNewPokerState(player, gameStatus);
			PokerMessage<?> pkrMsg;
			if(includePokerInnerMessage)
			{
				pkrMsg = this.getNewPokerMessage(pokerInnerMessage, pokerState, pokerType);
			}
			else{
				pkrMsg = this.getNewPokerMessage(pokerState, pokerType);
			}

			this.sendPokerMessage(player, pkrMsg);
		}
	}

	protected void quitPoker(RequestHandler handler)
	{
		if(this.playerList != null && this.playerList.contains(handler.getPlayer()))
		{
			int index = this.playerList.indexOf(handler.getPlayer());
			Player player = this.playerList.get(index);
			this.playerList.remove(index);
			this.sendPokerMessageToALL(this.getNewPokerMessage(String.format("Player - %s folded and exited from game", player.getPlayerName())
					, PokerEnum.FOLD.toString()));
		}
		else{
			log.info(String.format("%s already exited from poker - %s", handler.getPlayer(), this.gamePlayName+this.gamePlayId));
		}
	}

	@Override
	protected void initializeTableCards() {
		this.tableCards = new ArrayList<Card>(MAX_TABLE_CARD_SIZE);
		//		for(int i=0; i<MAX_TABLE_CARD_SIZE; i++)
		//			this.tableCards.add(Card.NULL_CARD);
	}

	@Override
	protected <Boolean> Boolean addToTableCards(Card card) throws CasinoException
	{
		if(this.tableCards.size() >= PokerGamePlay.MAX_TABLE_CARD_SIZE)
			throw new CasinoException("Cannot add Card to table cards because the size of table cards is "+this.tableCards.size());

		this.tableCards.add(card);
		return null;
	}

	@Override
	protected void sendWaitMessage(Player player, String message) {
		this.sendPokerMessage(player, this.getNewPokerMessage(message, PokerEnum.WAIT.toString()));
	}

	@Override
	protected void exitGame(RequestHandler handler) {
		this.quitPoker(handler);
		if(this.playerConnections != null && this.playerConnections.contains(handler.getPlayer())){
			this.playerConnections.remove(handler.getPlayer(), handler);
			log.info(String.format("%s was removed from poker - %s", handler.getPlayer(), this.gamePlayName+this.gamePlayId));
		}
		else{
			log.info(String.format("%s already exited from poker - %s", handler.getPlayer(), this.gamePlayName+this.gamePlayId));
		}
	}

}// End of PokerGamePlay

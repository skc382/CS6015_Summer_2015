package cs6015.casino.blackjack;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import cs6015.casino.exceptions.CasinoException;
import cs6015.casino.serializables.BlackJackMessage;
import cs6015.casino.serializables.BlackJackMessage.BlackJackEnum;
import cs6015.casino.serializables.Card;
import cs6015.casino.serializables.GameState;
import cs6015.casino.serializables.GameState.GameStatusEnum;
import cs6015.casino.serializables.Message;
import cs6015.casino.serializables.Player;
import cs6015.casino.server.GamePlay;
import cs6015.casino.server.RequestHandler;
import cs6015.casino.types.BlackJackType;
import cs6015.casino.types.ServerMessageType;

public final class BlackJackGamePlay extends GamePlay<BlackJackType>{
	private final static Logger log = Logger.getLogger(BlackJackGamePlay.class);

	public static final BlackJackGamePlay INVALID = null;
	private static final int MAX_TABLE_CARD_SIZE = 5;
	private static final HashMap<String, BlackJackGamePlay> blackJackGamesInstances = new HashMap<String, BlackJackGamePlay>();
	private boolean canPlay;
	//	private int moneyOnTable;
	private int playerMoney;
	private int previousBet;
	private int moneyOnTable;

	protected BlackJackGamePlay(String gameName, int gameId) {
		super(gameName, gameId);
		canPlay = false;
		log.info(String.format("Started game : %s-%d server.", this.gamePlayName, this.gamePlayId));
	}

	public static BlackJackGamePlay getInstances(String gameName, int gameId)
	{
		if(blackJackGamesInstances.isEmpty() || !blackJackGamesInstances.containsKey(String.format("%s-%d", gameName, gameId)))
			blackJackGamesInstances.put(String.format("%s-%d", gameName, gameId), new BlackJackGamePlay(gameName, gameId));

		return blackJackGamesInstances.get(String.format("%s-%d", gameName, gameId));
	}

	@Override
	protected void BeginGamePlay() {
		log.info(String.format("Beginning game play of %s-%d", this.gamePlayName, this.gamePlayId));
		this.initializeTableCards();				     // Assign null cards to table cards to initialize all the 5 elements
		
		this.moneyOnTable = 0;
		this.previousBet = 0;
		loadPlayerList();

		this.currentPlayer = (this.currentPlayer == null)? this.playerList.get(0) : this.currentPlayer;
		this.playerMoney = this.currentPlayer.getMoney();
		this.currentPlayer.setHand(new ArrayList<Card>());
		GameState<BlackJackType> state = this.getNewBlackJackState(this.currentPlayer, GameStatusEnum.USERACTION.toString());
		this.sendBlackJackMessage(this.currentPlayer, this.getNewBlackJackMessage(state, BlackJackEnum.BET.toString()));
		canPlay = true;
	}

	@Override
	protected void play(Player player, Message<?> msg) {
		BlackJackMessage<?> blkMsg = (BlackJackMessage<?>) msg.getGameMessage();
		try{
			if(canPlay)
			{
				if(player.equals(this.currentPlayer))
				{
					if(blkMsg.getType().equals(BlackJackEnum.BET.toString()))
					{
						int bet;

						if(!(blkMsg.getMessage() instanceof Integer))
							throw new CasinoException("BlackJack message does not contain integer. "
									+ "Please check on the client end of Player: "+ player.getPlayerName());

						bet = (Integer) blkMsg.getMessage();
						if (bet < 0 ) {
							throw new CasinoException("Error: Illegal bet amount received.");
						}

						moneyOnTable += bet;
						playerMoney -= bet;
						previousBet = bet;

						this.dealCardToPlayer(this.currentPlayer);
						this.tableCards.add(this.deck.deal());	// Deal card to the dealer
						this.tableCards.add(this.deck.deal());	// Deal card to the dealer
						this.proceed();			    		 
					}
					else if(blkMsg.getType().equals(BlackJackEnum.HIT.toString()))
					{
						deck.shuffle();
						this.currentPlayer = this.addToPlayerHand(this.currentPlayer, deck.deal());
						this.proceed();
					}
					else if(blkMsg.getType().equals(BlackJackEnum.DOUBLE.toString()))
					{
						int bet = 2*previousBet;
						moneyOnTable += bet;
						playerMoney -= bet;
						previousBet = bet;

						deck.shuffle();
						this.currentPlayer = this.addToPlayerHand(this.currentPlayer, deck.deal());
						this.proceed();
					}
					else if(blkMsg.getType().equals(BlackJackEnum.SURRENDER.toString()))
					{
						this.playerMoney = this.playerMoney + (int)(this.moneyOnTable/2);
						this.currentPlayer.setMoney(playerMoney);
						this.sendBlackJackMessage(this.currentPlayer, this.getNewBlackJackMessage(this.playerMoney, BlackJackEnum.SURRENDER.toString()));
					}
					else if(blkMsg.getType().equals(BlackJackEnum.STAND.toString()))
					{
						int dealerDifference, playerDifference, playerValue, dealerValue;
						playerValue = BlackJackEval.getValueOfHand(this.currentPlayer.getHand());
						dealerValue = BlackJackEval.getValueOfHand(this.tableCards);

						while(dealerValue < 17)
						{
							this.tableCards = this.addToTableCards(deck.deal());
							dealerValue = BlackJackEval.getValueOfHand(this.tableCards);
						}

						dealerDifference = 21 - dealerValue;
						playerDifference = 21 - playerValue;

						if(dealerDifference < 0)
						{
							this.playerMoney = this.playerMoney + this.moneyOnTable + (this.moneyOnTable*2);
							this.currentPlayer.setMoney(playerMoney);
							GameState<BlackJackType> state = this.getNewBlackJackState(this.currentPlayer, GameStatusEnum.WON.toString());
							this.sendBlackJackMessage(this.currentPlayer, this.getNewBlackJackMessage(state, BlackJackEnum.HIT_DOUBLE_STAND_SURRENDER.toString()));
						}
						else if(dealerDifference < playerDifference)
						{
							this.currentPlayer.setMoney(playerMoney);
							GameState<BlackJackType> state = this.getNewBlackJackState(this.currentPlayer, GameStatusEnum.LOST.toString());
							this.sendBlackJackMessage(this.currentPlayer, this.getNewBlackJackMessage(state, BlackJackEnum.HIT_DOUBLE_STAND_SURRENDER.toString()));
						}
						else
						{
							this.playerMoney = this.playerMoney + this.moneyOnTable + (this.moneyOnTable*2);
							this.currentPlayer.setMoney(playerMoney);
							GameState<BlackJackType> state = this.getNewBlackJackState(this.currentPlayer, GameStatusEnum.WON.toString());
							this.sendBlackJackMessage(this.currentPlayer, this.getNewBlackJackMessage(state, BlackJackEnum.HIT_DOUBLE_STAND_SURRENDER.toString()));
						}
					}
					else if(blkMsg.getType().equals(BlackJackEnum.NEW.toString()))
					{
						this.BeginGamePlay();
					}
				}
			}
		}
		catch(Exception e)
		{
			log.error(e);
			e.printStackTrace();
		}
	}

	private void proceed()
	{
		GameStatusEnum action = getNextActionFromCardValue(this.currentPlayer.getHand());
		if(action == GameStatusEnum.WON)
		{
			playerMoney = playerMoney + moneyOnTable +  (this.moneyOnTable*2);
		}
		if(action == GameStatusEnum.LOST){
			
		}

		this.currentPlayer.setMoney(this.playerMoney);
		GameState<BlackJackType> state = this.getNewBlackJackState(this.currentPlayer, action.toString());
		this.sendBlackJackMessage(this.currentPlayer, this.getNewBlackJackMessage(state, BlackJackEnum.HIT_DOUBLE_STAND_SURRENDER.toString()));
	}

	@Override
	protected void initializeTableCards() {
		this.tableCards = new ArrayList<Card>(MAX_TABLE_CARD_SIZE);
//		for(int i=0; i<MAX_TABLE_CARD_SIZE; i++)
//			this.tableCards.add(Card.NULL_CARD);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ArrayList<Card> addToTableCards(Card card) throws CasinoException {
		if(this.tableCards.size() >= BlackJackGamePlay.MAX_TABLE_CARD_SIZE)
			throw new CasinoException("Cannot add Card to table cards because the size of table cards is "+this.tableCards.size());

		this.tableCards.add(card);
		return this.tableCards;
	}

	private Player addToPlayerHand(Player player, Card card) throws CasinoException
	{
		if(player.getHand() == null)
			throw new CasinoException(String.format("The Player - %s's hand is empty and hence cannot add card - %s", player.getPlayerName(), card.toString()));

		ArrayList<Card> playerHand = player.getHand();
		playerHand.add(card);
		player.setHand(playerHand);
		return player;
	}

	@Override
	protected void sendWaitMessage(Player player, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void exitGame(RequestHandler handler) {
		this.quitBlackJack(handler);
		if(this.playerConnections != null && this.playerConnections.contains(handler.getPlayer())){
			this.playerConnections.remove(handler.getPlayer(), handler);
			log.info(String.format("%s was removed from Black Jack - %s", handler.getPlayer(), this.gamePlayName+this.gamePlayId));
		}
		else{
			log.info(String.format("%s already exited from black jack - %s", handler.getPlayer(), this.gamePlayName+this.gamePlayId));
		}	
	}

	private void quitBlackJack(RequestHandler handler)
	{
		if(this.currentPlayer != null)
		{
			this.currentPlayer = null;
		}
		else{
			log.info(String.format("%s already exited from black jack - %s", handler.getPlayer(), this.gamePlayName+this.gamePlayId));
		}
	}

	private GameStatusEnum getNextActionFromCardValue(ArrayList<Card> cards)
	{
		int cardsValue;
		int winValue = 21;

		cardsValue = BlackJackEval.getValueOfHand(cards);

		if(cardsValue == winValue){
			return GameStatusEnum.WON;
		}
		else if(cardsValue > winValue){
			return GameStatusEnum.LOST;
		}
		else
			return GameStatusEnum.USERACTION;
	}

	private GameState<BlackJackType> getNewBlackJackState(Player player, String gameStatus)
	{
		Player firstPlayer = player;
		Player secondPlayer = null;
		Player thirdPlayer = null;

		if(this.tableCards == null || firstPlayer == null)
		{
			throw new NullPointerException("Either PlayerList or TableCards are Null.");
		}
		return new GameState<BlackJackType>(this.tableCards, firstPlayer, secondPlayer, thirdPlayer, this.moneyOnTable, gameStatus);
	}

	private BlackJackMessage<?> getNewBlackJackMessage(GameState<BlackJackType> BlackJackState, String type)
	{
		return new BlackJackMessage<>(BlackJackState, type);
	}

	private <T> BlackJackMessage<?> getNewBlackJackMessage(T value, GameState<BlackJackType> BlackJackState, String type)
	{
		return new BlackJackMessage<T>(BlackJackState, value, type);
	}

	private <T> BlackJackMessage<?> getNewBlackJackMessage(T value, String type)
	{
		return new BlackJackMessage<T>(value, type);
	}

	private void sendBlackJackMessage(Player player, BlackJackMessage<?> BlackJackMessage)
	{
		Message<BlackJackMessage<?>> msg = new Message<BlackJackMessage<?>>(ServerMessageType.PLAYBLACKJACK);
		msg.setGameMessage(BlackJackMessage);
		this.sendMessageToClient(player, msg);
	}

}// End of BlackJackGamePlay

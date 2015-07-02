package cs6015.casino.blackjack;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import cs6015.casino.client.Controller;
import cs6015.casino.common.CardNumber;
import cs6015.casino.common.CardSymbol;
import cs6015.casino.common.CardsImagesResource;
import cs6015.casino.exceptions.CasinoException;
import cs6015.casino.serializables.BlackJackMessage;
import cs6015.casino.serializables.BlackJackMessage.BlackJackEnum;
import cs6015.casino.serializables.Card;
import cs6015.casino.serializables.GameState;
import cs6015.casino.serializables.GameState.GameStatusEnum;
import cs6015.casino.serializables.Message;
import cs6015.casino.serializables.Player;
import cs6015.casino.types.BlackJackType;
import cs6015.casino.types.ClientMessageType;

public class BlackJackController {
	
	private Controller controller;
	private BlackJackView blackJackView;
	public int previousPlayerMoney;
	
	public BlackJackController(Controller controller) {
		this.controller = controller;
		blackJackView = new BlackJackView(this);
		blackJackView.setUserNameLabel(this.controller.getPlayer().getPlayerName());
		blackJackView.setAccountMoneyLabel(Integer.toString(this.controller.getPlayer().getMoney()));
	}
	
	public void processBlackJackMessage(BlackJackMessage<?> blkMsg)
	{
		clearAllCards();
		try
		{
			if(blkMsg.getType().equals(BlackJackEnum.HIT_DOUBLE_STAND_SURRENDER.toString()))
			{
				GameState<BlackJackType> state;
				if(blkMsg.getBlackJackState() == null)
				{
					throw new CasinoException("The black Jack state is empty.");
				}
				
				if(blkMsg.getMessage() instanceof String){
					this.blackJackView.setMessageLabel((String)blkMsg.getMessage());
				}
				
				state = blkMsg.getBlackJackState();
				this.processGameState(state);
			}
			else if(blkMsg.getType().equals(BlackJackEnum.BET.toString()))
			{
				GameState<BlackJackType> state;
//				if(blkMsg.getBlackJackState() == null)
//				{
//					throw new CasinoException("The black Jack state is empty.");
//				}
//				
				state = blkMsg.getBlackJackState();
				this.processGameState(state);
				this.blackJackView.updateButtonStatus(true, false, false, false, false, true);
				this.blackJackView.setNewGameButtonStatus(false);
				this.blackJackView.setMessageLabel("Please place your Bet.");
			}
			else if(blkMsg.getType().equals(BlackJackEnum.SURRENDER.toString()))
			{
				if(blkMsg.getMessage() instanceof Integer)
				{
//					this.minimumBet = (Integer) blkMsg.getMessage();
					this.blackJackView.setPlayer1_Money(Integer.toString((Integer) blkMsg.getMessage()));
					this.blackJackView.setMessageLabel("You Surrendered. Would you like to play another game ?");
					this.blackJackView.updateButtonStatus(false, false, false, false, false, false);
					this.blackJackView.setNewGameButtonStatus(true);
				}
			}
				
			this.blackJackView.refreshView();			
		}
		catch(CasinoException e)
		{
			e.printStackTrace();
		}
	}
	
	private void clearAllCards()
	{
		ImageIcon icon = CardsImagesResource.getCardImage("1");
		this.blackJackView.setTableCard1(icon);
		this.blackJackView.setTableCard2(icon);
		this.blackJackView.setTableCard3(icon);
		this.blackJackView.setTableCard4(icon);
		this.blackJackView.setTableCard5(icon);
		
		this.blackJackView.setPlayer1_FirstCard(icon);
		this.blackJackView.setPlayer1_SecondCard(icon);
		this.blackJackView.setPlayer1_ThirdCard(icon);
		this.blackJackView.setPlayer1_FourthCard(icon);
		this.blackJackView.setPlayer1_FifthCard(icon);
		
		this.blackJackView.setMessageLabel("");
		this.blackJackView.setWinLabel("");
		this.blackJackView.setPotMoney("");
	}
	
	private void processGameState(GameState<BlackJackType> state)
	{
		Player player_1;
		ArrayList<Card> tableCards;
		int potMoney, money;
		String gameStatus = state.getGameStatus();
		GameStatusEnum status = GameStatusEnum.valueOf(gameStatus);
		
		player_1 = state.getPlayer1();
		tableCards = state.getTableCards();
		potMoney = state.getPotMoney();
		money = player_1.getMoney();
		this.controller.setPlayerMoney(money);
		boolean flag = false;
		switch(status)
		{
		case WON: this.blackJackView.setWinLabel("YOU WON");
				  this.blackJackView.setMessageLabel("You Won "+(money-this.previousPlayerMoney)+" dollars");
				  this.blackJackView.setPotMoney(Integer.toString(potMoney));
		          this.blackJackView.updateButtonStatus(false, false, false, false, false, false);
		          this.blackJackView.setNewGameButtonStatus(true);
			      break;
		case LOST: this.blackJackView.setWinLabel("YOU LOST");
				   this.blackJackView.setMessageLabel("You Lost. Would you like to play another game?");
				   this.blackJackView.setPotMoney(Integer.toString(potMoney));
				   this.blackJackView.updateButtonStatus(false, false, false, false, false, false);
				   this.blackJackView.setNewGameButtonStatus(true);
				   break;
		case USERACTION: this.blackJackView.setPotMoney(Integer.toString(potMoney));
						 this.blackJackView.updateButtonStatus(true, true, true, true, true, true);
						 this.blackJackView.setNewGameButtonStatus(false);
						 this.previousPlayerMoney = money;
						 flag = true;
					     break;
			default: throw new IllegalArgumentException("Illegal game status. Game status is: "+ status.toString());
		}
		
		this.updatePlayerInPanel(player_1);
		this.updateTableCards(tableCards, flag);
	}
	
	private void updatePlayerInPanel(Player player)
	{
		ImageIcon imageIconCard1, imageIconCard2, imageIconCard3, imageIconCard4, imageIconCard5;
		String name;
		int money, size = 0;
		ArrayList<Card> cards = null;
		if(player.getHand() != null)
		{
			cards = player.getHand();
			size = cards.size();
		}
		
		name = player.getPlayerName();
		money = player.getMoney();
		
		this.blackJackView.setPlayer1_Name(name);
		this.blackJackView.setPlayer1_Money(Integer.toString(money));
		this.blackJackView.setAccountMoneyLabel(Integer.toString(money));
		
		if(size >= 2)
		{
			imageIconCard1 = CardsImagesResource.getCardImage(this.deriveCardIndex(cards.get(0)));
			imageIconCard2 = CardsImagesResource.getCardImage(this.deriveCardIndex(cards.get(1)));
			this.blackJackView.setPlayer1_FirstCard(imageIconCard1);
			this.blackJackView.setPlayer1_SecondCard(imageIconCard2);
		}
		if(size >= 3)
		{
			imageIconCard3 = CardsImagesResource.getCardImage(this.deriveCardIndex(cards.get(2)));
			this.blackJackView.setPlayer1_ThirdCard(imageIconCard3);
		}
		if(size >= 4)
		{
			imageIconCard4 = CardsImagesResource.getCardImage(this.deriveCardIndex(cards.get(3)));
			this.blackJackView.setPlayer1_FourthCard(imageIconCard4);
		}
		if(size >= 5)
		{
			imageIconCard5 = CardsImagesResource.getCardImage(this.deriveCardIndex(cards.get(4)));
			this.blackJackView.setPlayer1_FifthCard(imageIconCard5);
		}
	}
	
	private void updateTableCards(ArrayList<Card> cards, boolean displayCard)
	{
		ImageIcon imageIconCard1, imageIconCard2, imageIconCard3, imageIconCard4, imageIconCard5;
		int size = 0;
		
		if(cards != null)
			size = cards.size();
		
		if(size >= 2)
		{
			imageIconCard1 = CardsImagesResource.getCardImage(this.deriveCardIndex(cards.get(0)));
			imageIconCard2 = CardsImagesResource.getCardImage(this.deriveCardIndex(cards.get(1)));
			if(size == 2 && displayCard){
				imageIconCard2 = CardsImagesResource.getCardImage("0");
			}
			this.blackJackView.setTableCard1(imageIconCard1);
			this.blackJackView.setTableCard2(imageIconCard2);
		}
		if(size >= 3)
		{
			imageIconCard3 = CardsImagesResource.getCardImage(this.deriveCardIndex(cards.get(2)));
			this.blackJackView.setTableCard3(imageIconCard3);
		}
		if(size >= 4)
		{
			imageIconCard4 = CardsImagesResource.getCardImage(this.deriveCardIndex(cards.get(3)));
			this.blackJackView.setTableCard4(imageIconCard4);
		}
		if(size >= 5)
		{
			imageIconCard5 = CardsImagesResource.getCardImage(this.deriveCardIndex(cards.get(4)));
			this.blackJackView.setTableCard5(imageIconCard5);
		}
	}
	
	private String deriveCardIndex(Card card)
	{
		String cardNumber, cardSymbol, finalIndex;
		int indexCardNumber, indexCardSymbol;
		
		if(card == null)
		{
			return "1";
		}
		
		cardNumber = card.getCardNumber();
		cardSymbol = card.getCardSymbol();
		
		indexCardNumber = (CardNumber.valueOf(cardNumber)).ordinal();
		indexCardSymbol = (CardSymbol.valueOf(cardSymbol)).ordinal();
		finalIndex = String.format("%d%d", (indexCardNumber + 1), (indexCardSymbol + 1));
		
		return finalIndex;
	}
	
	public void quit(BlackJackView view)
	{
		Message<BlackJackMessage<?>> msg = new Message<BlackJackMessage<?>>(ClientMessageType.QUIT);
		this.controller.send(msg);
		this.controller.updateMoney();
		De_Register();
	}
	
	public void De_Register()
	{
		// TODO: Empty all the fields of this class
		this.controller.De_RegisterControllers(this);
		this.controller = null;
		this.blackJackView = null;
	}
	
	public void sendBetMoney(int value)
	{
		BlackJackMessage<?> blkMsg = this.getNewBlackJackMessage(value, BlackJackEnum.BET.toString());
		this.sendBlackJackMessage(blkMsg);
	}
	
	public void sendMessageToServer(String blakckJackEnumType)
	{
		BlackJackMessage<?> blkMsg = this.getNewBlackJackMessage(null, blakckJackEnumType);
		this.sendBlackJackMessage(blkMsg);
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

	private void sendBlackJackMessage(BlackJackMessage<?> BlackJackMessage)
	{
		Message<BlackJackMessage<?>> msg = new Message<BlackJackMessage<?>>(ClientMessageType.BLACKJACK);
		msg.setGameMessage(BlackJackMessage);
		this.controller.send(msg);
	}
	
	public void closingAction()
	{
		this.controller.closingServerConnection();
	}
	
} // End of BlackJackController
package cs6015.casino.poker;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import cs6015.casino.blackjack.BlackJackView;
import cs6015.casino.client.Controller;
import cs6015.casino.common.CardNumber;
import cs6015.casino.common.CardSymbol;
import cs6015.casino.common.CardsImagesResource;
import cs6015.casino.exceptions.CasinoException;
import cs6015.casino.serializables.Card;
import cs6015.casino.serializables.GameState;
import cs6015.casino.serializables.GameState.GameStatusEnum;
import cs6015.casino.serializables.BlackJackMessage;
import cs6015.casino.serializables.Message;
import cs6015.casino.serializables.MyEntry;
import cs6015.casino.serializables.Player;
import cs6015.casino.serializables.PokerMessage;
import cs6015.casino.serializables.PokerMessage.PokerEnum;
import cs6015.casino.types.ClientMessageType;
import cs6015.casino.types.PokerType;
import cs6015.casino.types.ServerMessageType;

public class PokerController {

	private static PokerController instance = null;
	private Controller controller;
	private PokerView pokerView;
	public int minimumBet;

	public PokerController(Controller controller) {
		this.controller = controller;
		pokerView = new PokerView(this);
		pokerView.setUserNameLabel(this.controller.getPlayer().getPlayerName());
		pokerView.setAccountMoneyLabel(Integer.toString(this.controller.getPlayer().getMoney()));
	}

	public void processPokerMessage(PokerMessage<?> pkrMsg)
	{
		try
		{
			if(pkrMsg.getType().equals(PokerEnum.WAIT.toString()))
			{
				String msgToDisplay = this.retrievePokerMessage(pkrMsg.getMessage());
				pokerView.setMessageLabel(msgToDisplay);
				pokerView.updateButtonStatus(false, false, false, false);
			}
			else if(pkrMsg.getType().equals(PokerEnum.NOT_YOUR_TURN.toString()))
			{
				String msgToDisplay = this.retrievePokerMessage(pkrMsg.getMessage());
				this.pokerView.setMessageLabel(msgToDisplay);
				this.pokerView.updateButtonStatus(false, false, false, false);
			}
			else if(pkrMsg.getType().equals(PokerEnum.BET_CHECK.toString()))
			{
				if(pkrMsg.getMessage() instanceof Integer)
				{
					this.minimumBet = (Integer) pkrMsg.getMessage();
					this.pokerView.setBetTextField(minimumBet);
				}
				else{
					String msgToDisplay = this.retrievePokerMessage(pkrMsg.getMessage());
					this.pokerView.setMessageLabel(msgToDisplay);
					this.pokerView.updateButtonStatus(true, false, true, true);
				}
			}
			else if(pkrMsg.getType().equals(PokerEnum.BET_CHECK_FOLD.toString()))
			{
				if(pkrMsg.getMessage() instanceof Integer)
				{
					this.minimumBet = (Integer) pkrMsg.getMessage();
					this.pokerView.setBetTextField(minimumBet);
				}
				else{
					String msgToDisplay = this.retrievePokerMessage(pkrMsg.getMessage());
					this.pokerView.setMessageLabel(msgToDisplay);
					this.pokerView.updateButtonStatus(true, true, true, true);
				}
			}
			else
			{
				if(pkrMsg.getMessage() instanceof String)
				{
					String msgToDisplay = this.retrievePokerMessage(pkrMsg.getMessage());
					this.pokerView.setMessageLabel(msgToDisplay);
				}
			}

			if(!pkrMsg.isPokerStateNull())
			{
				GameState<PokerType> pokerState = pkrMsg.getPokerState();
				this.handlePokerState(pokerState);
			}
			pokerView.refreshView();
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
	}

	private void handlePokerState(GameState<PokerType> state)
	{
		Player player_1, player_2, player_3;
		ArrayList<Card> tableCards;
		int potMoney;
		String status;

		player_1 = state.getPlayer1();
		player_2 = state.getPlayer2();
		player_3 = state.getPlayer3();
		tableCards = state.getTableCards();
		potMoney = state.getPotMoney();

		if(state.getGameStatus().equals(GameStatusEnum.UPDATE_ALL.toString()))
		{
			this.updateTableCards(tableCards);
			this.updatePlayerInPanel(1, player_1, true);
			this.updatePlayerInPanel(2, player_2, false);
			this.updatePlayerInPanel(3, player_3, false);
		}
		else if(state.getGameStatus().equals(GameStatusEnum.UPDATE_TABLE.toString()))
		{
			this.updateTableCards(tableCards);
			this.pokerView.setPotMoney(Integer.toString(potMoney));
		}
		else  if(state.getGameStatus().equals(GameStatusEnum.WON.toString()) ||  state.getGameStatus().equals(GameStatusEnum.LOST.toString()))
		{
			this.updateTableCards(tableCards);
			this.updatePlayerInPanel(1, player_1, true);
			this.updatePlayerInPanel(2, player_2, true);
			this.updatePlayerInPanel(3, player_3, true);
			this.pokerView.setPotMoney(Integer.toString(potMoney));
			this.pokerView.setWinLabel("You" + state.getGameStatus());
		}
	}

	private void updatePlayerInPanel(int playerIndex, Player player, boolean displayCards)
	{
		String name, indexCard1, indexCard2;
		int money;
		ImageIcon imageIconCard1, imageIconCard2;

		ArrayList<Card> cards = player.getHand();

		if(displayCards)
		{
			indexCard1 = this.deriveCardIndex(cards.get(0));
			indexCard2 = this.deriveCardIndex(cards.get(1));
		}
		else
		{
			indexCard1 = "0";
			indexCard2 = "0";
		}

		imageIconCard1 = CardsImagesResource.getCardImage(indexCard1);
		imageIconCard2 = CardsImagesResource.getCardImage(indexCard2);

		name = player.getPlayerName();
		money = player.getMoney();

		switch(playerIndex)
		{
		case 1: this.pokerView.setPlayer1_Name(name);
		this.pokerView.setPlayer1_Money(Integer.toString(money));
		this.pokerView.setPlayer1_FirstCard(imageIconCard1);
		this.pokerView.setPlayer1_SecondCard(imageIconCard2);
		this.pokerView.setAccountMoneyLabel(Integer.toString(money));
		break;

		case 2:	this.pokerView.setPlayer2_Name(name);
		this.pokerView.setPlayer2_Money(Integer.toString(money));
		this.pokerView.setPlayer2_FirstCard(imageIconCard1);
		this.pokerView.setPlayer2_SecondCard(imageIconCard2);
		break;

		case 3:	this.pokerView.setPlayer3_Name(name);
		this.pokerView.setPlayer3_Money(Integer.toString(money));
		this.pokerView.setPlayer3_FirstCard(imageIconCard1);
		this.pokerView.setPlayer3_SecondCard(imageIconCard2);
		break;

		default : throw new IllegalArgumentException("Illegal Player Index. Player index is: "+ playerIndex);
		}
	}

	private void updateTableCards(ArrayList<Card> cards)
	{
		ImageIcon imageIconCard1, imageIconCard2, imageIconCard3, imageIconCard4, imageIconCard5;
		int size = 0;

		if(cards != null)
			size = cards.size();

		if(size >= 2)
		{
			imageIconCard1 = CardsImagesResource.getCardImage(this.deriveCardIndex(cards.get(0)));
			imageIconCard2 = CardsImagesResource.getCardImage(this.deriveCardIndex(cards.get(1)));
			this.pokerView.setTableCard1(imageIconCard1);
			this.pokerView.setTableCard2(imageIconCard2);
		}
		if(size >= 3)
		{
			imageIconCard3 = CardsImagesResource.getCardImage(this.deriveCardIndex(cards.get(2)));
			this.pokerView.setTableCard3(imageIconCard3);
		}
		if(size >= 4)
		{
			imageIconCard4 = CardsImagesResource.getCardImage(this.deriveCardIndex(cards.get(3)));
			this.pokerView.setTableCard4(imageIconCard4);
		}
		if(size >= 5)
		{
			imageIconCard5 = CardsImagesResource.getCardImage(this.deriveCardIndex(cards.get(4)));
			this.pokerView.setTableCard5(imageIconCard5);
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

	private <T> String retrievePokerMessage(T message)
	{
		if(message instanceof MyEntry)
		{
			MyEntry<String, Integer> entry = (MyEntry<String, Integer>)message;
			this.pokerView.setPotMoney(Integer.toString(entry.getValue())); // retrieve entry from pkrMsg and Set potMoney
			return entry.getKey();
		}
		return (String)message;
	}

	public void quit(PokerView view)
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
		this.pokerView = null;
	}

	public void sendBetMoney(int value)
	{
		PokerMessage<?> pkrMsg = this.getNewPokerMessage(value, PokerEnum.BET_CHECK.toString());
		this.sendPokerMessage(pkrMsg);
	}
	
	public void fold()
	{
		PokerMessage<?> pkrMsg = this.getNewPokerMessage("Folded", PokerEnum.FOLD.toString());
		this.sendPokerMessage(pkrMsg);
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

	private void sendPokerMessage(PokerMessage<?> pokerMessage)
	{
		Message<PokerMessage<?>> msg = new Message<PokerMessage<?>>(ClientMessageType.POKER);
		msg.setGameMessage(pokerMessage);
		this.controller.send(msg);
	}

	public void closingAction()
	{
		this.controller.closingServerConnection();
	}
} // End of Poker controller

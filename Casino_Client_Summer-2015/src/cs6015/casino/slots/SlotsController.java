package cs6015.casino.slots;

import java.util.Random;

import javax.swing.ImageIcon;

import cs6015.casino.blackjack.BlackJackView;
import cs6015.casino.client.Controller;
import cs6015.casino.common.CardsImagesResource;
import cs6015.casino.serializables.Message;
import cs6015.casino.serializables.Player;
import cs6015.casino.types.ClientMessageType;
/*
 * References: http://stackoverflow.com/questions/363681/generating-random-integers-in-a-range-with-java/363692#363692
 */
public class SlotsController {
	
	private Controller controller;
	private SlotsView slotsView;
	private int cardNumber1, cardNumber2, cardNumber3, cardSymbol1, cardSymbol2, cardSymbol3;
	private int playerMoney, betMoney;
	
	public SlotsController(Controller controller)
	{
		this.controller = controller;
		this.slotsView = new SlotsView(this);
		this.slotsView.setUserNameLabel(this.controller.getPlayer().getPlayerName());
		this.slotsView.setAccountMoneyLabel(Integer.toString(this.controller.getPlayer().getMoney()));
		playerMoney = this.controller.getPlayer().getMoney();
	}
	
	public SlotsController(SlotsView slotsView) {
		super();
		this.slotsView = slotsView;
		playerMoney = 200;
	}

	public void setBetMoney(int betMoney) {
		this.betMoney = betMoney;
	}

	public void display()
	{
		this.slotsView.refreshView();
	}
	
	public void spin()
	{
		this.slotsView.setMessageLabel("");
		ImageIcon card1, card2, card3;
		int factor;
		
		card1 = this.spinReel(1);
		card2 = this.spinReel(2);
		card3 = this.spinReel(3);
		
		this.slotsView.setTableCard1(card1);
		this.slotsView.setTableCard2(card2);
		this.slotsView.setTableCard3(card3);
		
		factor = analyze();
		
		if(factor > 0)
		{
			int priceMoney = betMoney * factor;
			this.slotsView.setMessageLabel(String.format("You Won %d dollars", priceMoney));
			playerMoney += priceMoney;
			this.slotsView.setAccountMoneyLabel(Integer.toString(this.playerMoney));
		}
		else{
			this.slotsView.setMessageLabel(String.format("You Lost %d dollars", betMoney));
			playerMoney -= betMoney;
			this.slotsView.setAccountMoneyLabel(Integer.toString(this.playerMoney));
		}
	}
	
	private int analyze()
	{
		int factor = 0;
		
		if((cardNumber1 == cardNumber2) || (cardNumber2 == cardNumber3) || (cardNumber1 == cardNumber3))
		{
			factor = factor + 1;
		}
		
		if((cardSymbol1 == cardSymbol2) || (cardSymbol1 == cardSymbol3)) //(cardSymbol2 == cardSymbol3) || 
		{
			factor = factor + 1;
		}
		
		if((cardNumber1 == cardNumber2) && (cardNumber2 == cardNumber3))
		{
			factor = factor + 1;
		}
		
		if((cardSymbol1 == cardSymbol2) && (cardSymbol2 == cardSymbol3))
		{
			factor = factor + 1;
		}
		return factor;
	}
	
	private ImageIcon spinReel(int reelNumber)
	{
		int cardNumberIndex, cardSymbolIndex;
		String finalIndex;
		ImageIcon icon;
		cardNumberIndex = this.returnRandomCardNumber();
		cardSymbolIndex = this.returnRandomCardSymbol();
		
		switch(reelNumber)
		{
		case 1 : cardNumber1 = cardNumberIndex;
				 cardSymbol1 = cardSymbolIndex;
				 break;
		case 2 : cardNumber2 = cardNumberIndex;
				 cardSymbol2 = cardSymbolIndex;
				 break;
		case 3 : cardNumber3 = cardNumberIndex;
				 cardSymbol3 = cardSymbolIndex;
				 break;
		}
		
		finalIndex = String.format("%d%d", cardNumberIndex, cardSymbolIndex);
		icon = CardsImagesResource.getCardImage(finalIndex);
		return icon;
	}
	
	private int returnRandomCardSymbol()
	{
		return this.randomInt(1, 4);
	}
	
	private int returnRandomCardNumber()
	{
		return this.randomInt(1, 13);
	}
	
	private int randomInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	public void closingAction() {
		this.controller.closingServerConnection();
	}
	
	public void quit(SlotsView view)
	{
		Message<?> msg = new Message<>(ClientMessageType.QUIT);
		this.controller.send(msg);
		Player player = this.controller.getPlayer();
		player.setMoney(playerMoney);
		this.controller.setPlayer(player);
		this.controller.updateMoney();
		De_Register();
	}
	
	public void De_Register()
	{
		// TODO: Empty all the fields of this class
		this.controller.De_RegisterControllers(this);
		this.controller = null;
		this.slotsView = null;
	}

}

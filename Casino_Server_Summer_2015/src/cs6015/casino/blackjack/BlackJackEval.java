package cs6015.casino.blackjack;

import java.util.ArrayList;

import cs6015.casino.common.CardNumber;
import cs6015.casino.serializables.Card;

public class BlackJackEval {

	public static int getValueOfHand(ArrayList<Card> cards)
	{
		int returnValue = 0;
		for(Card card : cards)
		{
			if(card != null){
				returnValue += cardValue(card);
			}
		}
		return returnValue;
	}

	private static int cardValue(Card card)
	{
		String val = card.getCardNumber();
		CardNumber cardNum = CardNumber.valueOf(val);

		switch(cardNum)
		{
		case TWO : return 2; 
		case THREE : return 3;
		case FOUR: return 4;
		case FIVE : return 5;
		case SIX : return 6;
		case SEVEN : return 7;
		case EIGHT : return 8;
		case NINE : return 9;
		case TEN :return 10;
		case JACK : return 10;
		case QUEEN : return 10;
		case KING : return 10;
		case ACE : return 11;
		default : throw new IllegalArgumentException("Illegal card value");
		}
	}
} // End of BlackJackEval

package cs6015.casino.poker;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import cs6015.casino.common.CardNumber;
import cs6015.casino.common.CardSymbol;
import cs6015.casino.exceptions.CasinoException;
import cs6015.casino.serializables.Card;

/*
 * This class contains methods and techniques used in the following links:
 * http://math.hws.edu/javanotes/source/chapter12/netgame/
 * http://www.mathcs.emory.edu/~cheung/Courses/170/Syllabus/10/pokerCheck.html
 */
public class PokerEval { 
	private final static Logger log = Logger.getLogger(PokerGamePlay.class);
	
	public synchronized static HandRank getRank(ArrayList<Card> hand) throws CasinoException
	{
		ArrayList<Card> cards;
		if(hand == null)
			throw new NullPointerException("Hand is null");

		if(hand.size() == 0)
			return HandRank.NIL;
		
		if(hand.size() != 5)
			throw new CasinoException("Hand size has to be exactly 5, but currently hand size is "+ hand.size());

		cards = new ArrayList<Card>();
		for(Card card : hand)
		{
			if (card == null)
				throw new IllegalArgumentException("Card is null");
			cards.add(card); 
		}
		return calculateRankOfHand(cards);
	}

	public static ArrayList<Card> sortCards(ArrayList<Card> cards) 
	{
		ArrayList<Card> sortedCards;
		if (cards == null)
			throw new IllegalArgumentException("Cards are null");

		if(cards.size() <= 0)
			throw new IllegalArgumentException("Cards size cannot be zero");

		sortedCards = new ArrayList<Card>();
		while(cards.size() > 0)
		{
			Card max = cards.get(0);
			for(int i = 1; i < cards.size(); i++)
			{
				if((numVal(cards.get(i)) > numVal(max)) || ((numVal(cards.get(i)) == numVal(max)) && (symVal(cards.get(i)) > (symVal(max)))))
					max = cards.get(i);
			}
			cards.remove(max);
			sortedCards.add(max);		
		}
		return sortedCards;
	}
	
	private static HandRank calculateRankOfHand(ArrayList<Card> cards) throws CasinoException
	{
		Card card1, card2, card3, card4, card5;
		boolean checkFlush, checkStraight;
		
		try{
			
			if(cards.size() != 5)
				throw new CasinoException("Hand size has to be exactly 5, but currently hand size is "+ cards.size());
			
			cards = sortCards(cards); // sort the cards
			
			card1 = cards.get(0);
			card2 = cards.get(1);
			card3 = cards.get(2);
			card4 = cards.get(3);
			card5 = cards.get(4);
			
			checkFlush = ((symVal(card1) == symVal(card2)) && (symVal(card2) == symVal(card3)) 
						&& (symVal(card3) == symVal(card4)) && (symVal(card1) == symVal(card2)));
			
			if(card1.equals(CardNumber.ACE) && numVal(card2) == 5 && numVal(card3) == 4 && numVal(card4) == 3 && numVal(card5) == 2)
			{
				checkStraight = true;
			}
			else {
				checkStraight = ((numVal(card1) == numVal(card2)+1) && (numVal(card2) == numVal(card3)+1) && (numVal(card3) == numVal(card4)+1)
						&& (numVal(card4) == numVal(card5)+1));
			}
			
			if(checkFlush){
				if(checkStraight){
					if(card1.equals(CardNumber.ACE)){
						return HandRank.ROYAL_FLUSH;
					}
					else{
						return HandRank.STRAIGHT_FLUSH;
					}
				}
				else{
					return HandRank.FLUSH;
				}
			}
			
			if(checkStraight){
				return HandRank.STRAIGHT;
			}
			
			if(((numVal(card1) == numVal(card2)) && (numVal(card2) == numVal(card3)) && (numVal(card3) == numVal(card4)))
					|| ((numVal(card2) == numVal(card3)) && (numVal(card3) == numVal(card4)) && (numVal(card4) == numVal(card5)))){
				return HandRank.FOUR_OF_A_KIND;
			}
			
			if(((numVal(card1) == numVal(card2)) && (numVal(card2) == numVal(card3))  && (numVal(card4) == numVal(card5)))
					|| ((numVal(card1) == numVal(card2)) && (numVal(card3) == numVal(card4))  && (numVal(card4) == numVal(card5)))){
				return HandRank.FULL_HOUSE;
			}
			
			if(((numVal(card1) == numVal(card2)) && (numVal(card2) == numVal(card3))) || ((numVal(card2) == numVal(card3)) && (numVal(card3) == numVal(card4))) 
					|| ((numVal(card3) == numVal(card4)) && (numVal(card4) == numVal(card5)))){
				return HandRank.TRIPLE;
			}
			
			if(((numVal(card1) == numVal(card2)) && (numVal(card3) == numVal(card4))) || ((numVal(card1) == numVal(card2)) && (numVal(card4) == numVal(card5)))
					|| ((numVal(card2) == numVal(card3)) && (numVal(card4) == numVal(card5)))){
				return HandRank.TWO_PAIR;
			}
			
			if((numVal(card1) == numVal(card2)) || (numVal(card2) == numVal(card3)) || (numVal(card3) == numVal(card4)) || (numVal(card4) == numVal(card5))){
				return HandRank.PAIR;
			}		
		}
		catch(IllegalArgumentException e){
			log.error(e);
		}
		
		return HandRank.NIL;
	}
	
	public static int symVal(Card card)
	{
		String val = card.getCardSymbol();
		CardSymbol cardSym= CardSymbol.valueOf(val);

		switch(cardSym)
		{
		case CLUBS : return 1; 
		case SPADES : return 2;
		case HEARTS : return 3;
		case DIAMONDS : return 4;
		default : throw new IllegalArgumentException("Illegal card symbol");
		}
	}

	public static int numVal(Card card)
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
		case JACK : return 11;
		case QUEEN : return 12;
		case KING : return 13;
		case ACE : return 11;
		default : throw new IllegalArgumentException("Illegal card number");
		}
	}

	public enum HandRank{
		NIL(0), PAIR(1), TWO_PAIR(2), TRIPLE(3), STRAIGHT(4), FLUSH(5), FULL_HOUSE(6), FOUR_OF_A_KIND(7), STRAIGHT_FLUSH(8), ROYAL_FLUSH(9);

		private int val;
		HandRank(int val){
			this.val = val;
		}

		int rank()
		{
			return this.val;
		}
		
		String stringVal()
		{
			switch(this)
			{
			case NIL : return "Nothing";
			case PAIR : return "Pair";
			case TWO_PAIR : return "Two pairs";
			case TRIPLE : return "Triple";
			case STRAIGHT : return "Straght";
			case FULL_HOUSE : return "Full House";
			case FOUR_OF_A_KIND : return "Four of a kind";
			case STRAIGHT_FLUSH : return "Straight Flush";
			case ROYAL_FLUSH : return "Royal Flush";
			default : throw new IllegalArgumentException("Illegal card value");
			}
		}
	}
} // End of PokerEval
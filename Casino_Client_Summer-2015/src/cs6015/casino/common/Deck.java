package cs6015.casino.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import cs6015.casino.serializables.Card;

public class Deck implements Serializable{

	private static final long serialVersionUID = 5684709842706372659L;
	
	private ArrayList<Card> deck;
	
	public Deck()
	{
		deck = new ArrayList<Card>();
		loadDeck();
	}	
	
	private void loadDeck()
	{
		for(CardNumber cardNumber : CardNumber.values())
		{
			for(CardSymbol cardSymbol : CardSymbol.values())
			{
				deck.add(new Card(cardNumber.stringValue(), cardSymbol.stringValue()));
			}
		}
	}
	
	public void shuffle()
	{
		Collections.shuffle(deck);
	}
	
	public void reset()
	{
		deck.clear();
		this.loadDeck();
	}
	
	public Card deal()
	{
		shuffle();
		Card topCard = deck.get(0);
		deck.remove(0);
		return topCard;
	}

	@Override
	public String toString() {
		return "Deck [deck=" + deck + "]";
	}
}

package cs6015.casino.serializables;

import java.io.Serializable;

public final class Card implements Serializable{

	private static final long serialVersionUID = 445365183419480759L;
	
	public static final transient Card NULL_CARD = null; 
	private String cardNumber;
	private String cardSymbol;
	
	public Card(String cardNumber, String cardSymbol)
	{
		this.cardNumber = cardNumber;
		this.cardSymbol = cardSymbol;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public String getCardSymbol() {
		return cardSymbol;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cardNumber == null) ? 0 : cardNumber.hashCode());
		result = prime * result
				+ ((cardSymbol == null) ? 0 : cardSymbol.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (cardNumber == null) {
			if (other.cardNumber != null)
				return false;
		} else if (!cardNumber.equals(other.cardNumber))
			return false;
		if (cardSymbol == null) {
			if (other.cardSymbol != null)
				return false;
		} else if (!cardSymbol.equals(other.cardSymbol))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Card [cardNumber=" + cardNumber + ", cardSymbol=" + cardSymbol
				+ "]";
	}
		
}

package cs6015.casino.common;

public enum CardNumber
{
	ACE("ACE"), KING("KING"), QUEEN("QUEEN"), JACK("JACK"), TEN("TEN"), NINE("NINE"), EIGHT("EIGHT"),
	SEVEN("SEVEN"), SIX("SIX"), FIVE("FIVE"), FOUR("FOUR"), THREE("THREE"), TWO("TWO");
	
	private String enumVal;
	
	CardNumber(String enumVal)
	{
		this.enumVal = enumVal;
	}
	
	String stringValue()
	{
		return enumVal;
	}
	
}
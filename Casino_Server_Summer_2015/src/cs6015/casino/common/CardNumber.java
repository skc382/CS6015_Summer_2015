package cs6015.casino.common;

public enum CardNumber
{
	ACE("Ace"), KING("King"), QUEEN("Queen"), JACK("Jack"), TEN("Ten"), NINE("Nine"), EIGHT("Eight"),
	SEVEN("Seven"), SIX("Six"), FIVE("Five"), FOUR("Four"), THREE("Three"), TWO("Two");
	
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
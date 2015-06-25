package cs6015.casino.common;

public enum CardSymbol
{
	CLUBS("Clubs"), SPADES("Spades"), HEARTS("Hearts"), DIAMONDS("Diamonds");
	
	private String enumVal;
	
	CardSymbol(String enumVal)
	{
		this.enumVal = enumVal;
	}
	
	String stringValue()
	{
		return enumVal;
	}
}
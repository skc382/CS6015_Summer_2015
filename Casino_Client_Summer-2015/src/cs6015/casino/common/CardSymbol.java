package cs6015.casino.common;

public enum CardSymbol
{
	CLUBS("CLUBS"), SPADES("SPADES"), HEARTS("HEARTS"), DIAMONDS("DIAMONDS");
	
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
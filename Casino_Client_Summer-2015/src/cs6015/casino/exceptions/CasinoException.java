package cs6015.casino.exceptions;

public class CasinoException extends Exception
{
	public CasinoException(String message)
	{
		super(message);
	}
	
	public String getMessage()
    {
        return super.getMessage();
    }
}

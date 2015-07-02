package cs6015.casino.types;

public enum ServerMessageType 
{
	AUTHENTICATE, 
	ASSIGNSESSION,
	AUTHENTICATIONSUCCESS,
	AUTHENTICATIONFAIL,
	PLAYPOKER,
	PLAYBLACKJACK,
	PLAYSLOTS,
	UPDATEMONEY,
	CLOSECONNECTION;
	
	public String toString()
	{
		switch(this)
		{
		case AUTHENTICATE : return "Authenticate";
		case ASSIGNSESSION : return "AssignSession";
		case AUTHENTICATIONSUCCESS : return "Success";
		case AUTHENTICATIONFAIL : return "failed";
		case PLAYPOKER : return "Play Poker";
		case PLAYSLOTS : return "Play Slots";
		case UPDATEMONEY : return "update money";
		case PLAYBLACKJACK : return "Play BlackJack";
		case CLOSECONNECTION : return "Close";
		
		default: throw new IllegalArgumentException();
		}
	}

}

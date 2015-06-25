package cs6015.casino.types;

public enum ClientMessageType {
	
	LOGIN, REGISTER, POKER, BLACKJACK, SLOTS, CLOSECONNECTION, UPDATEMONEY;
	
	public String toString()
	{
		switch(this)
		{
		case LOGIN : return "Login";
		case REGISTER : return "Register";
		case POKER : return "Poker";
		case BLACKJACK : return "BlackJack";
		case SLOTS : return "Slots";
		case CLOSECONNECTION : return "Close";
		case UPDATEMONEY : return "update money";
		
		default: throw new IllegalArgumentException();
		}
	}
}

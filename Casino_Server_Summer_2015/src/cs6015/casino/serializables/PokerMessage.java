package cs6015.casino.serializables;

import java.io.Serializable;

import cs6015.casino.types.PokerType;

public class PokerMessage<T> implements Serializable{

	private static final long serialVersionUID = 2593093065324542708L;
	
	private T message;
	private final String type;
	private GameState<PokerType> pokerState;

	public PokerMessage(GameState<PokerType> pokerState, String type) {
		super();
		this.type = type;
		this.pokerState = pokerState;
	}

	public PokerMessage(T message, String type) {
		super();
		this.message = message;
		this.type = type;
	}
	
	public PokerMessage(T message, GameState<PokerType> pokerState, String type) {
		super();
		this.message = message;
		this.type = type;
		this.pokerState = pokerState;
	}

	public T getMessage() {
		return message;
	}

	public GameState<PokerType> getPokerState() {
		return pokerState;
	}
	
	public String getType() {
		return type;
	}
	
	public boolean isPokerStateNull()
	{
		return (this.pokerState == null);
	}
	
	public enum PokerEnum{
		WAIT("Waiting"), POKERSTATE("Poker State"), BET_CHECK("Bet or Check"), BET_CHECK_FOLD("Bet or Check or Fold"),
		FOLD("Fold"), YOUR_TURN("your turn"), NOT_YOUR_TURN("Not your turn");
		
		private String enumVal;
		
		PokerEnum(String enumVal)
		{
			this.enumVal = enumVal;
		}
		
		public String stringVal()
		{
			return enumVal;
		}
	}

	@Override
	public String toString() {
		return "PokerMessage [message=" + message + ", type=" + type
				+ ", pokerState=" + pokerState + "]";
	}
}

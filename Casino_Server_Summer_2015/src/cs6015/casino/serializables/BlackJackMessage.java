package cs6015.casino.serializables;

import java.io.Serializable;

import cs6015.casino.types.BlackJackType;

public class BlackJackMessage<T> implements Serializable {
	private static final long serialVersionUID = 3737771713252083030L;

	private GameState<BlackJackType> blackJackState;
	private T message;
	private final String type;
	
	public BlackJackMessage(GameState<BlackJackType> BlackJackState, T message,	String type) {
		super();
		this.blackJackState = BlackJackState;
		this.message = message;
		this.type = type;
	}

	public BlackJackMessage(GameState<BlackJackType> BlackJackState, String type) {
		super();
		this.blackJackState = BlackJackState;
		this.type = type;
	}

	public BlackJackMessage(T message, String type) {
		super();
		this.message = message;
		this.type = type;
	}

	public GameState<BlackJackType> getBlackJackState() {
		return blackJackState;
	}

	public T getMessage() {
		return message;
	}

	public String getType() {
		return type;
	}
	
	public boolean isBlackJackStateNull()
	{
		return (blackJackState == null);
	}

	public enum BlackJackEnum{
		WAIT("WAIT"), BLACKJACKSTATE("BLACKJACKSTATE"), BET("BET"), HIT_DOUBLE_STAND_SURRENDER("HIT_DOUBLE_STAND_SURRENDER")
		, SURRENDER("SURRENDER"), HIT("HIT"), DOUBLE("DOUBLE"), STAND("STAND"), NEW("NEW");
		
		private String enumVal;
		
		BlackJackEnum(String enumVal)
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
		return "BlackJackMessage [BlackJackState=" + blackJackState + ", message="
				+ message + ", type=" + type + "]";
	}

	
	

}

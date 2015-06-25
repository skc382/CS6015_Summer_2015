package cs6015.casino.serializables;

import java.io.Serializable;

import cs6015.casino.types.PokerType;

public class PokerMessage<STRING_OR_INT_TYPE> implements Serializable{

	private static final long serialVersionUID = 2593093065324542708L;
	
	private STRING_OR_INT_TYPE message;
	private GameState<PokerType> pokerState;

	public PokerMessage(GameState<PokerType> pokerState) {
		super();
		this.pokerState = pokerState;
	}

	public PokerMessage(STRING_OR_INT_TYPE message) {
		super();
		this.message = message;
	}

	public PokerMessage(STRING_OR_INT_TYPE message, GameState<PokerType> pokerState) {
		super();
		this.message = message;
		this.pokerState = pokerState;
	}

	public STRING_OR_INT_TYPE getMessage() {
		return message;
	}

	public GameState<PokerType> getPokerState() {
		return pokerState;
	}
	
	public enum PokerEnum{
		WAIT("Waiting"), NO_FOLD("Bet or Raise"), CALL("Bet or Raise or Fold") ;
		
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
}

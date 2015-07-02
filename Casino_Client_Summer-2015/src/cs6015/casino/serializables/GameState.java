package cs6015.casino.serializables;

import java.io.Serializable;
import java.util.ArrayList;

import cs6015.casino.types.GameType;

public final class GameState<T extends GameType> implements Serializable{

	private static final long serialVersionUID = -8890361302319901163L;
	
	private Player player1;
	private Player player2;
	private Player player3;
	private ArrayList<Card> tableCards;
	private int potMoney;
	private String gameStatus;

	public GameState(ArrayList<Card> tableCards, Player player1, Player player2, Player player3,
			 int potMoney, String gameStatus) {
		super();
		this.player1 = player1;
		this.player2 = player2;
		this.player3 = player3;
		this.tableCards = tableCards;
		this.potMoney = potMoney;
		this.gameStatus = gameStatus;
	}

	public Player getPlayer1() {
		return player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public Player getPlayer3() {
		return player3;
	}

	public int getPotMoney() {
		return potMoney;
	}

	public String getGameStatus() {
		return gameStatus;
	}
	
	public ArrayList<Card> getTableCards() {
		return tableCards;
	}

	public enum GameStatusEnum{
		UPDATE_ALL("UPDATE_ALL"), UPDATE_TABLE("UPDATE_TABLE"), UPDATE_POT("UPDATE_POT"), WON("WON"), LOST("LOST"), USERACTION("USERACTION") ;
		
		private String enumVal;
		
		GameStatusEnum(String enumVal)
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
		return "GameState [player1=" + player1 + ", player2=" + player2
				+ ", player3=" + player3 + ", tableCards=" + tableCards
				+ ", potMoney=" + potMoney + ", gameStatus=" + gameStatus + "]";
	}
	
}

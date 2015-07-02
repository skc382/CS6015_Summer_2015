package cs6015.casino.serializables;

import java.io.Serializable;
import java.util.ArrayList;

public final class Player implements Serializable {
	
	private static final long serialVersionUID = -8230950966029500146L;
	
	private String playerName;
	private int sessionId;
	private int money;
	private ArrayList<Card> hand;

	public Player(Object playerName) {
		this.playerName = (String)playerName;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public int getSessionId() {
		return sessionId;
	}
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	
	public ArrayList<Card> getHand() {
		return hand;
	}

	public void setHand(Card card1, Card card2) {
		if(this.hand == null){
			this.hand = new ArrayList<Card>();
		}
		this.hand.add(card1);
		this.hand.add(card2);
	}
	
	public void setHand(ArrayList<Card> hand)
	{
		this.hand = hand;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((playerName == null) ? 0 : playerName.hashCode());
		result = prime * result + sessionId;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (playerName == null) {
			if (other.playerName != null)
				return false;
		} else if (!playerName.equals(other.playerName))
			return false;
		if (sessionId != other.sessionId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Player [playerName=" + playerName + ", sessionId=" + sessionId
				+ ", money=" + money + ", hand=" + hand + "]";
	}
	
}

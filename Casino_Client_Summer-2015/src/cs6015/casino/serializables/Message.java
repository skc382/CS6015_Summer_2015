package cs6015.casino.serializables;

import java.io.Serializable;

import cs6015.casino.types.ClientMessageType;
import cs6015.casino.types.ServerMessageType;

public final class Message<T> implements Serializable {
	private static final long serialVersionUID = 8837691826365538839L;
	
	private String playerName;
	private int sessionId;
	private int money;
	private String clientMessageType;
	private String serverMessageType;
    private T gameMessage;
	
	public Message(ClientMessageType clientMessageType)
	{
		this.clientMessageType = clientMessageType.toString();
	}
	
	public Message(ServerMessageType serverMessageType)
	{
		this.serverMessageType = serverMessageType.toString();
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

	public String getClientMessageType() {
		return clientMessageType;
	}

	public void setClientMessageType(String clientMessageType) {
		this.clientMessageType = clientMessageType;
	}

	public String getServerMessageType() {
		return serverMessageType;
	}

	public void setServerMessageType(String serverMessageType) {
		this.serverMessageType = serverMessageType;
	}

	public T getGameMessage() {
		return gameMessage;
	}

	public void setGameMessage(T gameMessage) {
		this.gameMessage = gameMessage;
	}

	@Override
	public String toString() {
		return "Message [playerName=" + playerName + ", sessionId=" + sessionId
				+ ", money=" + money + ", clientMessageType="
				+ clientMessageType + ", serverMessageType="
				+ serverMessageType + ", gameMessage=" + gameMessage + "]";
	}

}

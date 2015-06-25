package cs6015.casino.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import cs6015.casino.poker.PokerGamePlay;
import cs6015.casino.serializables.Message;
import cs6015.casino.types.ClientMessageType;
import cs6015.casino.types.ServerMessageType;

/*
 * This class contains methods and techniques used in the following link:
 * http://math.hws.edu/javanotes/source/chapter12/netgame/common/Hub.java
 */
public class Communication {
	private final static Logger log = Logger.getLogger(Communication.class);
	
	private Socket clientSocket;
	private boolean connectionClosed;
	private RequestHandler client;
	private LinkedBlockingQueue<Object> incomingMessages;
	private LinkedBlockingQueue<Object> outgoingMessages;
	private ObjectInputStream inChannel;
	private ObjectOutputStream outChannel;
	private ExecutorService executor;
	
	public Communication(Socket socket, RequestHandler client)
	{
		this.clientSocket = socket;
		this.client = client;
		this.connectionClosed = false;
		this.incomingMessages = new LinkedBlockingQueue<Object>();
		this.outgoingMessages = new LinkedBlockingQueue<Object>();
		this.executor = Executors.newCachedThreadPool();
		
		try
		{
			outChannel = new ObjectOutputStream(clientSocket.getOutputStream());
			outChannel.writeObject(new Message<>(ServerMessageType.AUTHENTICATE)); 
			outChannel.flush();
			executor.execute(new processThread());
			executor.execute(new sender());
			executor.execute(new receiver());
		}
		catch(IOException ex)
		{
			try 
			{
				connectionClosed = true;
				clientSocket.close();
			} catch (IOException e) 
			{
				log.error(e);
			}
			log.error(ex);
			ex.printStackTrace();
		}
	}

	
	public synchronized void sendToClient(Object message)
	{
		try {
			if(message instanceof Message<?>)
			{
				if(((Message<?>)message).getServerMessageType().equals(ServerMessageType.CLOSECONNECTION))
				{
					outgoingMessages.clear();
				}
				outgoingMessages.put(message);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log.error(e);
			e.printStackTrace();
		}
	}
	
	private class processThread implements Runnable
	{
		@Override
		public void run() {
			try
			{
			while(!connectionClosed)
			{
				Object message = incomingMessages.take();
				if(message instanceof Message<?>)
				{
					if(((Message<?>)message).getClientMessageType().equals(ClientMessageType.CLOSECONNECTION))
						connectionClosed = true;
						closeConnection();
				}
				else{
					client.processMessage(message);
				}
			}
			}
			catch(Exception e)
			{
				log.error(e);
				e.printStackTrace();
			}
			
		}
	}
	
	private class receiver implements Runnable
	{
		@Override
		public void run() 
		{
			try 
			{
			inChannel = new ObjectInputStream(clientSocket.getInputStream());
			while(!connectionClosed)
			{
				Object message = inChannel.readObject();
				if(message instanceof Message<?>)
				{
					Message<?> msg = (Message<?>) message;
					if(msg.getClientMessageType().equals(ClientMessageType.CLOSECONNECTION))
					{
						connectionClosed = true;
						System.out.println("Client connection closing");
						closeConnection();
					}
					else
					{
						incomingMessages.put(msg);	
					}
				}
			}
			} 
			catch (ClassNotFoundException | IOException | InterruptedException e) 
			{
				log.error(e);
				e.printStackTrace();
				closeConnection();
			} 
		}
	}
	
	private class sender implements Runnable
	{

		@Override
		public void run() {
			Object message;
			try 
			{
			while(!connectionClosed)
			{
				message = outgoingMessages.take();
			
				if(message instanceof Message<?>)
				{
					Message<?> msg = (Message<?>) message;
					outChannel.writeObject(msg);
					outChannel.flush();
					
					if(msg.getServerMessageType().equals(ServerMessageType.CLOSECONNECTION))
					{
						closeConnection();
					}
				}
			}
			} catch (InterruptedException | IOException e) 
			{
				log.error(e);
				e.printStackTrace();
				closeConnection();
			}
		}	
	}
	
	public void closeConnection()
	{
		try
		{
			executor.shutdown();
			if(ServerApplication.playersLoggedIn.contains(this))
			{
				ServerApplication.playersLoggedIn.remove(this);
			}
		}
		catch(NoSuchElementException e)
		{
			log.error(e);
			e.printStackTrace();
		}
	}
	
}
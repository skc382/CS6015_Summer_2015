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
		}
	}

	
	public synchronized void sendToClient(Object message)
	{
		try {
			if(message instanceof Message<?>)
			{
				if(((Message<?>)message).getServerMessageType().equals(ServerMessageType.CLOSECONNECTION.toString()))
				{
					outgoingMessages.clear();
				}
				outgoingMessages.put(message);
			}
		} catch (InterruptedException e) {
			log.error(e);
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
					if(((Message<?>)message).getClientMessageType().equals(ClientMessageType.CLOSECONNECTION.toString()))
					{
						connectionClosed = true;
						log.info("Connection closed by client. Player name: "+ Communication.this.client.getPlayer());
						closeConnection();
					}
					else{
						client.processMessage(message);
					}
				}
			}
			}
			catch(Exception e)
			{
				log.error(e);
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
					if(msg.getClientMessageType().equals(ClientMessageType.CLOSECONNECTION.toString()))
					{
						connectionClosed = true;
						log.info("Connection closed by client. Player name: "+ Communication.this.client.getPlayer());
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
				//e.printStackTrace();
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
					outChannel.reset();
					
					if(msg.getServerMessageType().equals(ServerMessageType.CLOSECONNECTION.toString()))
					{
						log.info("Connection closed by client. Player name: "+ Communication.this.client.getPlayer());
						closeConnection();
					}
				}
			}
			} catch (InterruptedException | IOException e) 
			{
				log.error(e);
				//e.printStackTrace();
				closeConnection();
			}
		}	
	}
	
	public void closeConnection()
	{
		try
		{
			log.info("Closing Connection of request");
			this.clientSocket.close();
			log.info("Closing client socket");
			executor.shutdown();
			log.info("Shutting down communication threads");
			if(ServerApplication.playersLoggedIn.contains(this.client))
			{
				ServerApplication.playersLoggedIn.remove(this.client);
				log.info("Removing the client from logged in clients");
			}
		}
		catch(NoSuchElementException | IOException e)
		{
			log.error(e);
			//e.printStackTrace();
		} 
	}
	
}
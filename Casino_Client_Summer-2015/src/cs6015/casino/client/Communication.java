package cs6015.casino.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import cs6015.casino.serializables.Message;
import cs6015.casino.types.ClientMessageType;
import cs6015.casino.types.ServerMessageType;


/*
 * This class contains methods and techniques used in the following link:
 * http://math.hws.edu/javanotes/source/chapter12/netgame/common/Hub.java
 */
public class Communication {
	private final static Logger log = Logger.getLogger(Communication.class);
	
	private final int SERVER_PORT = 9838;
	private static Communication instance = null;
	private Socket serverSocket;
	private boolean connectionClosed;
	private LinkedBlockingQueue<Object> incomingMessages;
	private LinkedBlockingQueue<Object> outgoingMessages;
	private ObjectInputStream inChannel;
	private ObjectOutputStream outChannel;
	private ExecutorService executor; 
	private Controller controller;	
	
	private Communication(Controller controller)
	{
		try
		{
			this.controller = controller;
			this.serverSocket = new Socket("localhost", SERVER_PORT);
			this.connectionClosed = false;
			this.incomingMessages = new LinkedBlockingQueue<Object>();
			this.outgoingMessages = new LinkedBlockingQueue<Object>();
			this.executor = Executors.newCachedThreadPool();
			outChannel = new ObjectOutputStream(serverSocket.getOutputStream());
			executor.execute(new ProcessThread());
			executor.execute(new Sender());
			executor.execute(new Receiver());
		}
		catch(IOException ex)
		{
			try 
			{
				connectionClosed = true;
				if(serverSocket != null)
					serverSocket.close();
			} 
			catch (IOException e) 
			{ 
				log.error(e);	
				e.printStackTrace();
			}
			log.error(ex);
			ex.printStackTrace();
		}
	}

	
	public static Communication getInstance(Controller controller)
	{
		if(instance == null)
			instance = new Communication(controller);
		
		return instance;
	}
	
	public synchronized void sendToServer(Object message)
	{
		try {
			if(message instanceof Message<?>)
			{
				if(((Message<?>)message).getClientMessageType().equals(ClientMessageType.CLOSECONNECTION.toString()))
				{
					outgoingMessages.clear();
				}
				outgoingMessages.put(message);
			}
		} catch (InterruptedException e) {
			log.error(e);
			e.printStackTrace();
		}
	}
	
	public void handleRequestMessage(Message<?> message) {
		this.controller.processMessage(message);
	}
	
	public void closeConnection()
	{
		try {

			executor.shutdown();
			if(inChannel != null)
				inChannel.close();
			
			if(outChannel != null)
				 outChannel.close();
			
			this.serverSocket.close();
			executor.awaitTermination(1, TimeUnit.SECONDS);
			System.exit(0);
		}
		catch(InterruptedException e)
		{
			log.error(e);
			e.printStackTrace();
		} catch (IOException e) {
			log.error(e);
			e.printStackTrace();
		} 
	}	
	
	/********************************************
	 *       Private Classes below
	 *******************************************/
	
	private class ProcessThread implements Runnable
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
					if(((Message<?>)message).getServerMessageType().equals(ServerMessageType.CLOSECONNECTION.toString()))
					{
						connectionClosed = true;
						closeConnection();
					}
					else
					{
						handleRequestMessage((Message<?>)message);
					}
				}
				else
				{
					// TODO: Handle this situation
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
	
	private class Receiver implements Runnable
	{
		@Override
		public void run() 
		{
			try 
			{
			inChannel = new ObjectInputStream(serverSocket.getInputStream());
			while(!connectionClosed)
			{
				Object message = inChannel.readObject();
				if(message instanceof Message<?>)
				{
					if(((Message<?>)message).getServerMessageType().equals(ServerMessageType.CLOSECONNECTION.toString()))
					{
						connectionClosed = true;
						System.out.println("Client connection closing");
						closeConnection();
					}
					else
					{
						incomingMessages.put(message);	
					}
				}
			}
			} 
			catch (ClassNotFoundException | IOException | InterruptedException e) 
			{
				log.error(e);
				e.printStackTrace();
			} 
		}
	}
	
	private class Sender implements Runnable
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
					outChannel.writeObject(message);
					outChannel.flush();
					outChannel.reset();
					
					if(((Message<?>)message).getClientMessageType().equals(ClientMessageType.CLOSECONNECTION.toString()))
					{
						closeConnection();
					}
				}
			  }
			} catch (InterruptedException | IOException e) 
			{
				log.error(e);
				e.printStackTrace();
			}
		}	
	}// End of Sender
	
}// End of Communication
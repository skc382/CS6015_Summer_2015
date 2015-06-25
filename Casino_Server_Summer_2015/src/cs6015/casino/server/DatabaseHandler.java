package cs6015.casino.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class DatabaseHandler {
	
	Document document;
	
	File xmlFile;
	SAXBuilder builder;
	Element rootNode;
	
	public  DatabaseHandler()
	{
		 builder = new SAXBuilder();
		 xmlFile = new File("Database.xml");
		 try 
		 {
			 document = (Document) builder.build(xmlFile);
			 rootNode = document.getRootElement();
		 } 
		 catch (IOException io) {
		 			System.out.println(io.getMessage());
		 } 
		 catch (JDOMException jdomex) {
		 		System.out.println(jdomex.getMessage());
		 }
		
	}
	
	public synchronized boolean isPlayerRegistered(String playerName)
	{
		
		List<Element> list = rootNode.getChildren();
		 
		for (int i = 0; i < list.size(); i++) {
 
		   Element node = (Element) list.get(i);
		   String val = node.getChild("name").getTextTrim(); 
		   if(val.equalsIgnoreCase(playerName))
			   return true;
		}
		return false;
	}
	
	public synchronized void updateMoney(String playerName, int money)
	{
		List<Element> list = rootNode.getChildren();
		 
		for (int i = 0; i < list.size(); i++) {
 
		   Element node = (Element) list.get(i);
		   String val = node.getChild("name").getTextTrim(); 
		   if(val.equalsIgnoreCase(playerName))
			   node.getChild("money").setText(Integer.toString(money));
		}
	}
	
	public synchronized void writeXmlToFile()
	{
		try {
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		xmlOutput.output(document, new FileWriter("Database.xml"));
		System.out.println("Writing to database");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized Object[] getPlayer(String playerName)
	{
		
		List<Element> list = rootNode.getChildren("Player");
		Object[] returnVal = new Object[2];
		 
		for (int i = 0; i < list.size(); i++) {
 
		   Element node = (Element) list.get(i);
 
		   if(node.getChild("name").getText().equals(playerName))
		   {
			   returnVal[0] = node.getChild("name").getText();
		   	   returnVal[1] = Integer.parseInt(node.getChild("money").getText());
		   }
		}
		return returnVal;
	}
	
	public synchronized void addPlayer(String playerName, int money)
	{
		Element child = new Element("Player");
		child.addContent(new Element("id").setText(playerName));
		child.addContent(new Element("money").setText(Integer.toString(money)));
		rootNode.addContent(child);
		writeXmlToFile();
	}

}

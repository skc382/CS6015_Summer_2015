package cs6015.casino.client;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class CardsImagesResource {
	
	private HashMap<Integer,ImageIcon> cardImages;
	private static CardsImagesResource instance;
	
	private CardsImagesResource()
	{
		cardImages = new HashMap<Integer,ImageIcon>();
		loadCards();
		System.out.println();
	}
	
	public static CardsImagesResource getInstance()
	{
		if(instance == null)
			instance = new CardsImagesResource();
		
		return instance;
	}
	
	private void loadCards()
	{
		StringBuilder path = new StringBuilder();
		BufferedImage image = null;
		ImageIcon imgIcon;
		String initialPath = "C:\\Users\\shreedhar\\WorkSpace_Casino_Client\\CasinoFrontEnd\\src\\Deck_OF_Cards\\";

		try
		{
			for(int index=0; index<=53; index++)
			{
				path.append(initialPath).append(index).append(".png");
				image = ImageIO.read(new File(path.toString()));
				imgIcon = new ImageIcon(image);
				cardImages.put(index, imgIcon);
				path.setLength(0);
			}
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public ImageIcon getCardImage(int index)
	{
		return cardImages.get(index);
	}

}

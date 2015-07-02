package cs6015.casino.common;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class CardsImagesResource {
	
	private static HashMap<String,ImageIcon> cardImages;
	private static CardsImagesResource instance;
	
	private CardsImagesResource()
	{
		cardImages = new HashMap<String,ImageIcon>();
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
		Path initialPath = Paths.get("Resources\\Deck_OF_Cards\\");
		try
		{
			path.append(initialPath.toString()).append("\\").append("0").append(".png");
			image = ImageIO.read(new File(path.toString()));
			imgIcon = new ImageIcon(image);
			cardImages.put("0", imgIcon);
			cardImages.put("1", (ImageIcon) Utils.createTransparentIcon(72, 96));
			path.setLength(0);
			
			for(CardNumber cardNumber : CardNumber.values())
			{
				for(CardSymbol cardSymbol : CardSymbol.values())
				{
					int firstIndex, secondIndex;
					String finalIndex;
					firstIndex = (cardNumber.ordinal() + 1);  // Add one to the ordinal to match with the values of the image resources.
					secondIndex = (cardSymbol.ordinal() + 1);	// Add one to the ordinal to match with the values of the image resources.
					finalIndex = String.format("%d%d", firstIndex, secondIndex);
					path.append(initialPath.toString()).append("\\").append(finalIndex).append(".png");
					image = ImageIO.read(new File(path.toString()));
					imgIcon = new ImageIcon(image);
					cardImages.put(finalIndex, imgIcon);
					path.setLength(0);
				}
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
	
	public static ImageIcon getCardImage(String index)
	{
		return cardImages.get(index);
	}

}

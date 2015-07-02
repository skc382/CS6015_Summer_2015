package cs6015.casino.common;

import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/*
 * http://www.java2s.com/Tutorial/Java/0240__Swing/Createsatransparenticon.htm	
 */
public class Utils {
	
	  public static BufferedImage createTransparentImage (final int width, final int height)
	  {
	    return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	  }

	  public static Icon createTransparentIcon (final int width, final int height)
	  {
	    return new ImageIcon(createTransparentImage(width, height));
	  }

}

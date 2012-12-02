package Lib;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageManipulation {
	/**
	 * 
	 * By Cirno. ImageManipulation library.
	 * 
	 */

	public static Image resizeImage(BufferedImage originalImage){
		BufferedImage resizedImage = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, 128, 128, null);
		g.finalize();
		g.dispose();
		resizedImage.flush();
		return resizedImage;
	}
}

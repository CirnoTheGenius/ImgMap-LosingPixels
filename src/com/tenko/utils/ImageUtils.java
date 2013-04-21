package com.tenko.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageUtils {

	/**
	 * Resizes the image to a 128x128 image.
	 * @param s - The URL
	 * @return An image that we can use.
	 */
	public static Image resizeImage(Image img){
		BufferedImage originalImage = (BufferedImage)img;
		BufferedImage resizedImage = new BufferedImage(128, 128, 2);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, 128, 128, null);
		g.finalize();
		g.dispose();
		resizedImage.flush();

		return resizedImage;
	}

}

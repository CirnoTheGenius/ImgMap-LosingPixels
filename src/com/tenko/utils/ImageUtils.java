package com.tenko.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageUtils {

	public static Image resizeImage(String s){
		BufferedImage resizedImage = new BufferedImage(128, 128, 2);
		try {
			BufferedImage originalImage = ImageIO.read(new URL(s));
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, 128, 128, null);
			g.finalize();
			g.dispose();
			resizedImage.flush();
		} catch (IOException e){
			e.printStackTrace();
		}
		return resizedImage;
	}
	
}

package com.tenko.objs;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.bukkit.Bukkit;
import org.bukkit.map.MapPalette;

import com.tenko.ImgMap;

public class GifAnimation {

	/**
	 * Frames in a GIF image.
	 */
	private ArrayList<Color[][]> frames = new ArrayList<Color[][]>();

	/**
	 * Constructs a GifAnimation object.
	 * @param url - The URL
	 * @throws IOException - Thrown if the URL is invalid.
	 */
	public GifAnimation(String url) throws IOException {
		ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();
		reader.setInput(ImageIO.createImageInputStream(new URL(url).openStream()));

		for(int i=0, count=reader.getNumImages(true); i < count; i++){
			final int pos = i;
			final BufferedImage img = MapPalette.resizeImage(reader.read(pos));
			frames.add(new Color[128][128]);
			
			//Unsafe.
			Thread populationThread = new Thread(){
				@Override
				public void run(){
					for(int x=0; x <= 128; x++){
						for(int y=0; y <= 128; y++){
							frames.get(pos)[x][y] = new Color(img.getRGB(x, y));
						}
					}
				}
			};
			
			populationThread.start();
		}
	}

	/**
	 * Gets the frames.
	 * @return The array containg Color[][].
	 */
	public ArrayList<Color[][]> getFrames(){
		return frames;
	}
}

package com.tenko.objs;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.bukkit.map.MapPalette;

public class GifAnimation {
	
	public ArrayList<byte[]> frames = new ArrayList<byte[]>();
	
	public GifAnimation(URL img){
		try {
			ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();  
			ImageInputStream in = ImageIO.createImageInputStream(img.openStream());
			reader.setInput(in); 
			for (int i = 0, count = reader.getNumImages(true); i < count; i++){
				frames.add(MapPalette.imageToBytes(reader.read(i)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

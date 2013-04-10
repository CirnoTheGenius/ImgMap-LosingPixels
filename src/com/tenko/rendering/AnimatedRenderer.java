package com.tenko.rendering;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;


/**
 * EXTREAMLY experimental animated rendering. Quite possibly an extreamly laggy one too.
 * @author Tsunko
 *
 */
public class AnimatedRenderer extends MapRenderer {

	@Override
	public void render(MapView viewport, MapCanvas canvas, Player plyr) {
		
	}
	
//	  ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();  
//    ImageInputStream in = ImageIO.createImageInputStream(new URL("https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Rotating_earth_%28large%29.gif/200px-Rotating_earth_%28large%29.gif").openStream());  
//    reader.setInput(in); 
//    for (int i = 0, count = reader.getNumImages(true); i < count; i++)  
//    {
//        ImageIO.write((RenderedImage)ImageUtils.resizeImage(reader.read(i)), "png", new File("C:/Users/Tenshi/Desktop/derp/output" + i + ".jpg"));  
//    }  
	
	
}

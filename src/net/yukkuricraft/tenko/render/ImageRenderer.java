package net.yukkuricraft.tenko.render;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.bukkit.entity.Player;
import org.bukkit.map.*;

public class ImageRenderer extends MapRenderer {
	
	private BufferedImage cacheImage;
	private boolean hasRendered = false;
	
	// Hue.
	// http://i.snag.gy/EntDA.jpg
	// http://i.snag.gy/dZIPl.jpg
	// http://i.snag.gy/sLziT.jpg
	// http://i.snag.gy/Uzyv4.jpg
	public ImageRenderer(URL url) throws IOException {
		this.cacheImage = this.getImage(url);
	}
	
	@Override
	public void render(MapView view, MapCanvas canvas, Player player){
		if(!this.hasRendered){
			canvas.drawImage(0, 0, this.cacheImage);
			this.hasRendered = true;
			return;
		}else{
			player.sendMap(view);
		}
	}
	
	public BufferedImage getImage(URL url) throws IOException{
		BufferedImage image = ImageIO.read(url);
		RenderUtils.resizeImage(image);
		return image;
	}
	
}

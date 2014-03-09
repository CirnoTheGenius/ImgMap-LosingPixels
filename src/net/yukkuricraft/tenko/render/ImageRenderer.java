package net.yukkuricraft.tenko.render;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.URL;

import javax.imageio.ImageIO;

import net.yukkuricraft.tenko.ImgMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class ImageRenderer extends MapRenderer {
	
	// So fancy.
	private SoftReference<BufferedImage> cacheImage;
	private boolean hasRendered = false;
	
	// Hue.
	// http://i.snag.gy/EntDA.jpg
	// http://i.snag.gy/dZIPl.jpg
	// http://i.snag.gy/sLziT.jpg
	// http://i.snag.gy/Uzyv4.jpg
	public ImageRenderer(String url) throws IOException {
		this.cacheImage = new SoftReference<>(this.getImage(url));
	}
	
	public ImageRenderer(File file) throws IOException {
		this.cacheImage = new SoftReference<>(this.getImage(file));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void render(MapView view, MapCanvas canvas, Player player){
		if(this.hasRendered){
			return;
		}
		
		if(this.cacheImage.get() != null){
			canvas.drawImage(0, 0, this.cacheImage.get());
			this.hasRendered = true;
		}else{
			player.sendMessage(ChatColor.RED + "Attempted to render the image, but the cached image was null!");
			ImgMap.logMessage(ChatColor.RED + "While rendering image map ID #" + view.getId() + ", cacheImage was garbage collected.");
			this.hasRendered = true;
		}
	}
	
	public BufferedImage getImage(String url) throws IOException{
		BufferedImage image = ImageIO.read(new URL(url));
		RenderUtils.resizeImage(image);
		
		return image;
	}
	
	public BufferedImage getImage(File file) throws IOException{
		BufferedImage image = ImageIO.read(file);
		RenderUtils.resizeImage(image);
		
		return image;
	}
	
}

package net.yukkuricraft.tenko.render;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Stack;

import net.yukkuricraft.tenko.ImgMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.plugin.java.JavaPlugin;

public class SlideshowRenderer extends MapRenderer {
	
	private boolean needsUpdating = true;
	private Stack<BufferedImage> images;
	// In seconds.
	private final int timeTotalLength;
	
	public SlideshowRenderer(int length, List<BufferedImage> images) {
		images = new Stack<>();
		images.addAll(images);
		
		this.timeTotalLength = length;
	}
	
	@Override
	public void render(MapView view, MapCanvas canvas, Player player){
		if(this.needsUpdating){
			Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(ImgMap.class), new Runnable() {
				@Override
				public void run(){
					SlideshowRenderer.this.needsUpdating = true;
				}
			}, this.timeTotalLength * 1000);
			BufferedImage image = this.images.pop();
			canvas.drawImage(0, 0, image);
			this.images.push(image);
			
			this.needsUpdating = false;
		}
	}
	
}

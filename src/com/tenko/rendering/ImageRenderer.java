package com.tenko.rendering;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.tenko.ImgMap;
import com.tenko.utils.ImageUtils;

public class ImageRenderer extends MapRenderer {
	
	private final String url;
	private final ImgMap plugin;

	private boolean hasRendered = false;
	
	public ImageRenderer(ImgMap plugin, String theUrl){
		this.url = theUrl;
		this.plugin = plugin;
	}
	
	@Override
	public void render(MapView view, final MapCanvas canvas, Player plyr) {
		if(url != null && !hasRendered){
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
				@Override
				public void run() {
					canvas.drawImage(0, 0, ImageUtils.resizeImage(url));
					hasRendered = true;
				}
			});
		}
	}
	
}
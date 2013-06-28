package com.tenko.rendering;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class ImageRenderer extends MapRenderer {
	
	private boolean hasRendered = false;
	
	public String sauce;
	
	public ImageRenderer(String url){
		this.sauce = url;
	}
	
	private void _render(final MapCanvas canvas){
		Thread renderThread = new Thread(){
			@Override
			public void run(){
				try {
					canvas.drawImage(0, 0, MapPalette.resizeImage(ImageIO.read(new URL(sauce))));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		renderThread.start();
		hasRendered = true;
	}
	
	@Override
	public void render(MapView view, MapCanvas canvas, Player plyr) {
		if(!hasRendered && sauce != null && !sauce.isEmpty()){
			_render(canvas);
		}
	}

}

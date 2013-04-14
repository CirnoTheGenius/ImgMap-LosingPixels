package com.tenko.rendering;

import java.io.IOException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.tenko.events.ImageRenderEvent;

public class ImageRenderer extends MapRenderer {

	/**
	 * Non-modifiable URL associated with the renderer.
	 */
	private final String url;

	/**
	 * Has the renderer rendered? (Try typing that one!)
	 */
	private boolean hasRendered = false;

	/**
	 * Creates a new ImageRenderer object.
	 * @param theUrl - URL to be used to render images.
	 */
	public ImageRenderer(String theUrl){
		this.url = theUrl;
	}

	/**
	 * This should never be called outside of this class.
	 * @param canvas - The MapCanvas. Note: This has a final modifier.
	 */
	private final void startRenderThread(final MapCanvas canvas, MapView view){
		for(int i=0; i < canvas.getCursors().size(); i++){
			canvas.getCursors().removeCursor(canvas.getCursors().getCursor(i));
		}

		new Thread(){
			@Override
			public void run() {
				try {
					if (url.startsWith("http://")) {
						canvas.drawImage(0,0,MapPalette.resizeImage(ImageIO.read(new URL(url))));
					}
					else {
						/*
						 * Here we assume that 'url' was already accepted by the input methods.
						 * That is, we are not trying to access anything confidential.
						 */
						canvas.drawImage(0,0,MapPalette.resizeImage(ImageIO.read(new File(url))));
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * Rendering method. Don't call anywhere; called by CraftBukkit itself.
	 */
	@Override
	public void render(MapView view, MapCanvas canvas, Player plyr) {
		if(url != null && !hasRendered){			
			hasRendered = true;
			startRenderThread(canvas, view);
			Bukkit.getServer().getPluginManager().callEvent(new ImageRenderEvent(url, view));
		}
	}

}

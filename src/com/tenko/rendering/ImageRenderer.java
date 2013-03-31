package com.tenko.rendering;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.tenko.events.ImageRenderEvent;
import com.tenko.utils.ImageUtils;

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
				canvas.drawImage(0, 0, ImageUtils.resizeImage(url));
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
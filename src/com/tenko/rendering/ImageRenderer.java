package com.tenko.rendering;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

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
	 * @param canvas - The MapCanvas. Note: This is a final canvas.
	 */
	private final void startRenderThread(final MapCanvas canvas){
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
			startRenderThread(canvas);
		}
	}
	
}
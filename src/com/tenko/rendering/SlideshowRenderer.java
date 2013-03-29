package com.tenko.rendering;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.tenko.utils.ImageUtils;

public class SlideshowRenderer extends MapRenderer {

	/**
	 * Non-modifiable URL associated with the renderer.
	 */
	private final String[] urls;

	/**
	 * Has the renderer rendered? (Try typing that one!)
	 */
	private boolean hasRendered = false;
	
	/**
	 * The time to wait in seconds.
	 */
	private int waitTime = 0;

	/**
	 * Creates a new ImageRenderer object.
	 * @param theUrl - URL to be used to render images.
	 */
	public SlideshowRenderer(String[] theUrl, int time){
		this.urls = theUrl;
	}

	/**
	 * This should never be called outside of this class.
	 * @param canvas - The MapCanvas. Note: This is a final canvas.
	 */
	private final void startRenderThread(final MapCanvas canvas){
		new Thread(){
			@Override
			public void run() {
				try {
					for(int i=0; i <= urls.length; i++){
						i = (i == urls.length ? 0 : i);
						canvas.drawImage(0, 0, ImageUtils.resizeImage(urls[i]));
						Thread.sleep(waitTime*20);
					}
				} catch (InterruptedException e) {
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
		if(urls != null && !hasRendered){
			hasRendered = true;
			startRenderThread(canvas);
		}
	}
}

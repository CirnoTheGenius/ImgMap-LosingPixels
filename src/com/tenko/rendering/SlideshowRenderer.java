package com.tenko.rendering;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.tenko.threading.SlideshowThread;

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
	private float waitTime = 0;
	
	private SlideshowThread thread;
	
	/**
	 * Creates a new ImageRenderer object.
	 * @param theUrl - URL to be used to render images.
	 */
	public SlideshowRenderer(String[] theUrl, float waitTime){
		this.urls = theUrl;
		this.waitTime = waitTime;
	}

	/**
	 * This should never be called outside of this class.
	 * @param canvas - The MapCanvas. Note: This is a final canvas.
	 */
	private final void startRenderThread(final MapCanvas canvas){
		thread = new SlideshowThread(urls, waitTime, canvas);
		thread.start();
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

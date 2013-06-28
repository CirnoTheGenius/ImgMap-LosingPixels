package com.tenko.rendering;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.tenko.threading.SlideshowThread;

public class SlideshowRenderer extends MapRenderer {
	
	private final String[] urls;
	private boolean hasRendered = false;
	private float waitTime = 0;

	public SlideshowRenderer(String[] theUrl, float wait){
		this.urls = theUrl;
		this.waitTime = wait;
	}

	private void startRenderThread(final MapCanvas canvas){
        SlideshowThread thread = new SlideshowThread(urls, waitTime, canvas);
		thread.start();
	}

	@Override
	public void render(MapView view, MapCanvas canvas, Player plyr) {
		for(int i=0; i < canvas.getCursors().size(); i++){
			canvas.getCursors().removeCursor(canvas.getCursors().getCursor(i));
		}

		if(urls != null && !hasRendered){
			hasRendered = true;
			startRenderThread(canvas);
		}
	}
}

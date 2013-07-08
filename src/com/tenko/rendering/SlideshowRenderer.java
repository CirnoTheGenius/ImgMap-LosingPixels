package com.tenko.rendering;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.tenko.threading.SlideshowThread;

public class SlideshowRenderer extends MapRenderer {
	
	private final ArrayList<String> urls;
	private final float waitTime;
	private boolean hasRendered = false;
	public SlideshowThread thread;
	
	public SlideshowRenderer(ArrayList<String> theUrls, float wait){
		this.urls = theUrls;
		this.waitTime = wait;
	}
	
	private void startRenderThread(final MapCanvas canvas){
        thread = new SlideshowThread(urls, waitTime, canvas);
        thread.start();
	}

	@Override
	public void render(MapView view, MapCanvas canvas, Player plyr){
		while(canvas.getCursors().size() > 0) canvas.getCursors().removeCursor(canvas.getCursors().getCursor(0));
	
		if(urls != null && !hasRendered){
			hasRendered = true;		
			startRenderThread(canvas);
		}
	}
	
	public Collection<String> getUrls(){
		return urls;
	}
	
}

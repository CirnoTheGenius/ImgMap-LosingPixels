package com.tenko.threading;

import org.bukkit.map.MapCanvas;

import com.tenko.ImgMap;
import com.tenko.utils.ImageUtils;

public class SlideshowThread extends Thread {
	
	private final String[] urls;
	private final float waitTime;
	private final MapCanvas viewport;
	private boolean running;
	
	public SlideshowThread(String[] urls, float waitTime, MapCanvas viewport){
		super(ImgMap.getThreadGroup() , "SlideshowRenderer #" + viewport.getMapView().getId());
		this.urls = urls;
		this.waitTime = waitTime;
		this.viewport = viewport;
		this.running = true;
	}
	
	@Override
	public void run() {
		int pos = 0;
		
		try {
			while(running){
				pos++;
				if(pos == urls.length){
					pos = 0;
				}
				viewport.drawImage(0, 0, ImageUtils.resizeImage(urls[pos]));
				Thread.sleep((int)(waitTime)*20);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void stopThread(){
		running = false;
	}
}

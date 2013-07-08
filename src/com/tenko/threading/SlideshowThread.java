package com.tenko.threading;

import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;

public class SlideshowThread extends SafeThread {
	
	private final Iterable<String> urls;
	private final float waitTime;
	private final MapCanvas view;
	
	public SlideshowThread(Iterable<String> theUrls, float time, MapCanvas viewport){
		super("Slideshow Render Thread for " + viewport.getMapView().getId());
		this.waitTime = time;
		this.urls = theUrls;
		this.view = viewport;
		running = true;
	}

	@Override
	public void stopThread(){
		this.running = false;
	}

	@Override
	public void run(){
		try {
			while(isRunning()){
				for(String s : urls){
					System.out.println(s);
					view.drawImage(0, 0, MapPalette.resizeImage(ImageIO.read(new URL(s))));
					Thread.sleep(((int)waitTime)*1000);					
				}
			}
		} catch (IOException e){
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}

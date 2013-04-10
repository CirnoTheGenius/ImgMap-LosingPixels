package com.tenko.threading;

import java.net.MalformedURLException;
import java.net.URL;

import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;

import com.tenko.objs.GifAnimation;

public class AnimationThread extends Thread {

	/**
	 * Wait time in seconds.
	 */
	private final float waitTime;

	/**
	 * The map canvas to render to.
	 */
	private final MapCanvas viewport;

	/**
	 * Is this thread running?
	 */
	private boolean running;

	private GifAnimation gif;

	/**
	 * Creates a new slideshow thread. It is not started until told to.
	 * @param urls - The list of images.
	 * @param waitTime - The wait time.
	 * @param viewport - The map canvas.
	 */
	public AnimationThread(String url, float waitTime, MapCanvas viewport){
		super("SlideshowRenderer #" + viewport.getMapView().getId());
		this.waitTime = waitTime;
		this.running = false;
		this.viewport = viewport;

		try {
			this.gif = new GifAnimation(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Am I running?
	 * @return Whether or not this thread is truely alive.
	 */
	public boolean amIAlive(){
		return running;
	}



	@Override
	public void run() {
		this.running = true;
		int i=0;

		while(running){
			//for every image
			for(int frameNum = 0; frameNum < gif.frames.size(); frameNum++){
				//every pixel in the image
				for(int x=0; x < 128; x++){
					for(int y=0; y < 128; y++){
						i++;
						//set this to that pixel.
						viewport.setPixel(x, y, gif.frames.get(frameNum)[i]);
					}
				}
			}
		}
	}

	/**
	 * Safely stop the thread.
	 */
	public void stopThread(){
		running = false;
	}
}
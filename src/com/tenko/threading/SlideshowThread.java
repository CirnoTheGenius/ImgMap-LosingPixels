package com.tenko.threading;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;

import com.tenko.ImgMap;

public class SlideshowThread extends Thread {

	/**
	 * List of images to render.
	 */
	private final String[] urls;

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

	/**
	 * Creates a new slideshow thread. It is not started until told to.
	 * @param urls - The list of images.
	 * @param waitTime - The wait time.
	 * @param viewport - The map canvas.
	 */
	public SlideshowThread(String[] urls, float waitTime, MapCanvas viewport){
		super("SlideshowRenderer #" + viewport.getMapView().getId());
		this.urls = urls;
		this.waitTime = waitTime;
		this.viewport = viewport;
		this.running = true;
		ImgMap.getThreadGroup().getThreads().add(this);
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
		int pos = 0;

		try {
			while(running){
				pos++;
				if(pos >= urls.length){
					pos = 0;
				}
				if (urls[pos].startsWith("http://")) {
					viewport.drawImage(0, 0, MapPalette.resizeImage(ImageIO.read(new URL(urls[pos]))));
				}
				else {
					/*
					 * Here we assume that 'url' was already accepted by the input methods.
					 * That is, we are not trying to access anything confidential.
					 */
					viewport.drawImage(0,0,MapPalette.resizeImage(ImageIO.read(new File(urls[pos]))));
				}
				Thread.sleep(((int)waitTime)*1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Safely stop the thread.
	 */
	public void stopThread(){
		running = false;
	}
}

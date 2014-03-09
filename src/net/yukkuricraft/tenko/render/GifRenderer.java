package net.yukkuricraft.tenko.render;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_7_R1.PacketPlayOutMap;
import net.yukkuricraft.tenko.ImgMap;
import net.yukkuricraft.tenko.objs.BufferedGif;
import net.yukkuricraft.tenko.threading.AbstractSafeRunnable;
import net.yukkuricraft.tenko.threading.AnimationRunnable;
import net.yukkuricraft.tenko.threading.CachingRunnable;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class GifRenderer extends MapRenderer {
	
	public final static int TOLERANCE = 10;
	
	private List<String> watchers;
	private List<AbstractSafeRunnable> running;
	private PacketPlayOutMap[][] cache;
	private int delayMilli;
	private boolean ready = false;
	private BufferedGif gif;
	
	// Spinning thing: /drawanimatedimage http://www.thisiscolossal.com/wp-content/uploads/2013/01/4.gif
	// Transforming gel: /drawanimatedimage http://i.imgur.com/vzzoyGs.gif
	// Tenshi Tambourine: /drawanimatedimage http://24.media.tumblr.com/tumblr_mcbqgdaKRT1qasv9zo1_500.gif
	// Experimental packet caching.
	// The world is waiting~
	public GifRenderer(String str, short id) throws IOException {
		boolean useSun = false;
		
		try{
			Class.forName("com.sun.imageio.plugins.gif.GIFImageReader");
			Class.forName("com.sun.imageio.plugins.gif.GIFImageReaderSpi");
			ImgMap.logMessage("Using Sun GIF reader.");
			useSun = true;
		}catch (ClassNotFoundException e){
			ImgMap.logMessage("Using homemade GIF reader.");
		}
		
		this.gif = new BufferedGif(str, useSun);
		this.setupCache(id);
		this.watchers = new ArrayList<>();
		this.running = new ArrayList<>();
	}
	
	public GifRenderer(File file, short id) throws IOException {
		boolean useSun = false;
		
		try{
			Class.forName("com.sun.imageio.plugins.gif.GIFImageReader");
			Class.forName("com.sun.imageio.plugins.gif.GIFImageReaderSpi");
			ImgMap.logMessage("Using Sun GIF reader.");
			useSun = true;
		}catch (ClassNotFoundException e){
			ImgMap.logMessage("Using homemade GIF reader.");
		}
		
		this.gif = new BufferedGif(file, useSun);
		this.setupCache(id);
		this.watchers = new ArrayList<>();
	}
	
	public BufferedGif getBufferedGif(){
		return this.gif;
	}
	
	public void setMillisecondDelay(int delay){
		this.delayMilli = delay;
	}
	
	public void MissionStarto(){
		this.ready = true;
	}
	
	public void setCache(PacketPlayOutMap[][] a){
		this.cache = a;
	}
	
	public PacketPlayOutMap[][] initializeCache(int length){
		return this.cache = new PacketPlayOutMap[length][];
	}
	
	public void setupCache(short id) throws IOException{
		CachingRunnable run = new CachingRunnable(id, this);
		Thread cacheThread = new Thread(run);
		cacheThread.start();
	}
	
	@Override
	public void render(MapView view, MapCanvas canvas, Player player){
		if(this.watchers.contains(player.getName()) || !this.ready){
			return;
		}
		
		this.watchers.add(player.getName());
		AbstractSafeRunnable run = new AnimationRunnable(player, this.cache, this.delayMilli);
		this.running.add(run);
		Thread thread = new Thread(run);
		thread.start();
	}
	
	public void stopRendering(){
		if(this.running == null){
			System.out.println("Called to stop renderer on " + this.toString() + " but returned had a null renderRunnable!");
		}else{
			// Safely stops it.
			for(AbstractSafeRunnable renderRunnable : this.running){
				renderRunnable.stopRunning();
				System.out.println("Safely stopped runnable.");
				if(renderRunnable.isRunning()){
					System.out.println("renderRunnable is still running!");
				}
			}
		}
	}
	
}

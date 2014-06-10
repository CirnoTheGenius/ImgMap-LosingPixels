package net.yukkuricraft.tenko.render;

import java.io.IOException;
import java.net.URL;

import net.minecraft.server.v1_7_R3.PacketPlayOutMap;
import net.yukkuricraft.tenko.objs.BufferedGif;
import net.yukkuricraft.tenko.threading.*;

import org.bukkit.entity.Player;
import org.bukkit.map.*;

public class GifRenderer extends MapRenderer implements StoppableRenderer {
	
	public final static int TOLERANCE = 20;
	
	private PacketPlayOutMap[][] cache;
	private GroupWatchRunnable uiThread;
	private int delayMilli;
	private boolean ready = false;
	private BufferedGif gif;
	
	// Spinning thing: /drawanimatedimage http://www.thisiscolossal.com/wp-content/uploads/2013/01/4.gif
	// Transforming gel: /drawanimatedimage http://i.imgur.com/vzzoyGs.gif
	// Experimental packet caching.
	// The world is waiting~
	public GifRenderer(short id, URL toDraw) throws IOException {
		this.gif = new BufferedGif(toDraw);
		this.setupCache(id);
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
	
	public PacketPlayOutMap[][] initializeCache(int length){
		return this.cache = new PacketPlayOutMap[length][];
	}
	
	public void setupCache(short id) throws IOException{
		CachingRunnable run = new CachingRunnable(id, this);
		Thread cacheThread = new Thread(run);
		cacheThread.start();
	}
	
	@Override
	public void render(MapView view, MapCanvas canvas, Player plyr){
		if(ready && uiThread == null){
			uiThread = new AnimationRunnable(this.cache, this.delayMilli);
			uiThread.addPlayer(plyr);
			uiThread.start();
		}
		
		if(uiThread != null && !uiThread.isPlayerWatching(plyr)){
			uiThread.addPlayer(plyr);
		}
	}
	
	@Override
	public void stopRendering(){
		uiThread.stopRunning();
	}
	
}

package com.tenko.threading;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapView;

import com.tenko.objs.GifAnimation;

public class AnimeThread extends Thread {
	
	/**
	 * The map canvas to render to.
	 */
	private final MapCanvas viewport;
	
	/**
	 * Is this thread running?
	 */
	private boolean running;
	
	private final MapView view;
	
	private final Player plyr;
	
	private final GifAnimation anime;
	
	public AnimeThread(MapView view, MapCanvas viewport, Player plyr, GifAnimation gif){
		this.viewport = viewport;
		this.view = view;
		this.plyr = plyr;
		this.anime = gif;
		this.running = true;
	}
	
	@Override
	public void run(){
		while(running){
			for(int pos=0; pos < anime.getFrames().size(); pos++){
				if(pos >= anime.getFrames().size()){
					pos = 0;
				}
				
				for(int x=0; x < 128; x++){
					for(int y=0; y < 128; y++){
						try {
							viewport.setPixel(x, y, MapPalette.matchColor(anime.getFrames().get(pos)[x][y]));
						} catch (NullPointerException e){}
					}
				}
				
				try {
					sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				plyr.sendMap(view);
			}

		}
	}
	
}

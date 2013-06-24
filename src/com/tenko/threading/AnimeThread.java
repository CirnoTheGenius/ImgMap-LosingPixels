package com.tenko.threading;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapView;

import com.tenko.ImgMap;
import com.tenko.objs.GifAnimation;

public class AnimeThread extends SafeThread {

	/**
	 * The map canvas to render to.
	 */
	private final MapCanvas viewport;

	private final MapView view;

	private final Player plyr;

	private final GifAnimation anime;

	public AnimeThread(MapView mapview, MapCanvas viewCanvas, Player player, GifAnimation gif){
		super("Animation thread for " + gif);
		this.viewport = viewCanvas;
		this.view = mapview;
		this.plyr = player;
		this.anime = gif;
		this.running = true;
		ImgMap.getThreadGroup().getThreads().add(this);
	}

	@Override
	public void run(){
		while(isRunning()){
			for(int pos=0; pos < anime.getFrames().size(); pos++){
				if(pos >= anime.getFrames().size()){
					pos = 0;
				}

				for(int x=0; x < 128; x++){
					for(int y=0; y < 128; y++){
						try {
							viewport.setPixel(x, y, MapPalette.matchColor(anime.getFrames().get(pos)[x][y]));
						} catch (NullPointerException e){/**/}
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
	
	@Override
	public void stopThread(){
		running = false;
	}

}

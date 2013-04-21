package com.tenko.rendering;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.tenko.objs.GifAnimation;
import com.tenko.threading.AnimeThread;

/**
 * EXTREAMLY experimental animated rendering. Quite possibly an extremely laggy one too.
 * @author Tsunko
 */
public class AnimatedRenderer extends MapRenderer {

	private GifAnimation anime;

	private AnimeThread rendering;
	
	private boolean hasRendered = false;
	
	public AnimatedRenderer(String url){
		try {
			this.anime = new GifAnimation(url);
			Bukkit.broadcastMessage("Created anime object!");
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void render(final MapView viewport, final MapCanvas canvas, final Player plyr) {
		for(int i=0; i < canvas.getCursors().size(); i++){
			canvas.getCursors().removeCursor(canvas.getCursors().getCursor(i));
		}
		
		if(anime != null && !hasRendered){
			hasRendered = true;
			startRenderThread(viewport, canvas, plyr);	
		}
	}

	/**
	 * This should never be called outside of this class.
	 * @param canvas - The MapCanvas. Note: This is a final canvas.
	 */
	private final void startRenderThread(final MapView viewport, final MapCanvas canvas, final Player plyr){
		rendering = new AnimeThread(viewport, canvas, plyr, anime);
		rendering.start();
	}
	
}

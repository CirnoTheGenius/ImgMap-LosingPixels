package com.tenko.threading;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.google.common.io.Files;
import com.tenko.ImgMap;
import com.tenko.objs.MapData;
import com.tenko.rendering.ImageRenderer;
import com.tenko.rendering.SlideshowRenderer;
import com.tenko.utils.DataUtils;
import com.tenko.utils.URLUtils;

/**
 * Loads persistent maps into the game; multithreaded so it doesn't disturb the server.
 * @author Tsunko
 */
public class PersistencyThread extends Thread {

	public PersistencyThread(){
		try {
			DataUtils.initialize();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void run(){
		try {
			List<String> savedMapData = Files.readLines(ImgMap.getList(), Charset.defaultCharset());
			File[] dirFiles = new File(ImgMap.getPlugin().getDataFolder().getAbsolutePath() + "/SlideshowData/").listFiles();
			
			for(String line : savedMapData){
				MapData md = MapData.convertData(line);
				MapView viewport = Bukkit.getServer().getMap(md.getId());
				Iterator<MapRenderer> mr = viewport.getRenderers().iterator();
				
				while(mr.hasNext()){
					viewport.getRenderers().remove(mr.next());
				}
				
				viewport.addRenderer(new ImageRenderer(URLUtils.isLocal(md.getUrl()) ? URLUtils.getLocal(md.getUrl()) : md.getUrl()));
			}
			
			for(File f : dirFiles){
				short id = Short.valueOf(f.getName().substring(0, f.getName().indexOf(".")));
				MapView viewport = Bukkit.getServer().getMap(id);
				Iterator<MapRenderer> mr = viewport.getRenderers().iterator();
				
				while(mr.hasNext()){
					viewport.getRenderers().remove(mr.next());
				}
				
				List<String> lines = Files.readLines(f, Charset.defaultCharset());
				float waitTime = Float.valueOf(lines.remove(0));
				viewport.addRenderer(new SlideshowRenderer(lines.toArray(new String[0]), waitTime));
			}
			
			//This is probably a really bad idea. Will remove in the future.
			//inb4i-forget-about-this.
			System.gc();
		} catch (IOException e){
			e.printStackTrace();
		} catch (MapData.InvalidMapDataException e) {
			e.printStackTrace();
		}
	}
	

}

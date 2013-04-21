package com.tenko.threading;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.google.common.io.Files;
import com.tenko.ImgMap;
import com.tenko.rendering.ImageRenderer;
import com.tenko.rendering.SlideshowRenderer;
import com.tenko.utils.DataUtils;

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
			for(String s : Files.readLines(ImgMap.getList(), Charset.defaultCharset())){
				String url = s.substring(s.indexOf(":")+1, s.length());
				short id = Short.valueOf(s.substring(0, s.indexOf(":")));

				MapView viewport = Bukkit.getServer().getMap(id);

				for(MapRenderer mr : viewport.getRenderers()){
					viewport.removeRenderer(mr);
				}

				viewport.addRenderer(new ImageRenderer(url));
			}

			for(File f : new File(ImgMap.getPlugin().getDataFolder().getAbsolutePath() + "/SlideshowData/").listFiles()){
				short id = Short.valueOf(f.getName().substring(0, f.getName().indexOf(".")));
				MapView viewport = Bukkit.getServer().getMap(id);

				for(MapRenderer mr : viewport.getRenderers()){
					viewport.removeRenderer(mr);
				}

				List<String> lines = Files.readLines(f, Charset.defaultCharset());
				float waitTime = Float.valueOf(lines.remove(0));
				String[] urls = new String[lines.size()];
				lines.toArray(urls);

				viewport.addRenderer(new SlideshowRenderer(urls, waitTime));
			}
		} catch (IOException e){
			e.printStackTrace();
		}

	}
}

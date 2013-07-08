package com.tenko.threading;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.google.common.io.Files;
import com.tenko.ImgMap;
import com.tenko.rendering.ImageRenderer;
import com.tenko.rendering.SlideshowRenderer;
import com.tenko.utils.MapDataUtils;

public class PersistencyThread extends SafeThread {

	public PersistencyThread(){
		super("Persistency thread for ImgMap");
	}

	@Override
	public void stopThread(){
		//Nothing.
	}

	@Override
	public void run(){
		try {
			List<String> savedMapData = Files.readLines(MapDataUtils.getList(), Charset.defaultCharset());
			File[] dirFiles = new File(ImgMap.getInstance().getDataFolder().getAbsolutePath() + "/SlideshowData/").listFiles();

			for(String data : savedMapData){
				MapData mapdata = MapData.convertData(data);
				
				if(mapdata == null){
					System.out.println("There appears to be something wrong with the line \"" + data + "\".");
					continue;
				}
				
				MapView view = Bukkit.getMap(mapdata.getId());
				Iterator<MapRenderer> mr = view.getRenderers().iterator();
				
				while(mr.hasNext()){
					view.getRenderers().remove(mr.next());
				}
				
				view.addRenderer(new ImageRenderer(mapdata.getUrl()));
			}
			
			for(File f : dirFiles){
				short id = Short.valueOf(f.getName().substring(0, f.getName().indexOf(".")));
				MapView viewport = Bukkit.getServer().getMap(id);
				Iterator<MapRenderer> mr = viewport.getRenderers().iterator();
				
				while(mr.hasNext()){
					viewport.getRenderers().remove(mr.next());
				}
				
				ArrayList<String> lines = new ArrayList<String>();
				lines.addAll(Files.readLines(f, Charset.defaultCharset()));
				float waitTime = Float.valueOf(lines.remove(0));
				viewport.addRenderer(new SlideshowRenderer(lines, waitTime));
			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	
	private static class MapData {
		
		private final String url;
		private final short id;
		
		private MapData(short mapId, String theURL){
			this.url = theURL;
			this.id = mapId;
		}
		
		public static MapData convertData(String s){
			if(s.indexOf(":") > -1 && s.split(":")[1].length() > 0){
				try {
					return new MapData(Short.valueOf(s.split(":")[0]), s.split(":")[1]);
				} catch (NumberFormatException e){
					e.printStackTrace();
				}
			}
			return null;
		}	
		
		public String getUrl(){
			return url;
		}
		
		public short getId(){
			return id;
		}
	}
}

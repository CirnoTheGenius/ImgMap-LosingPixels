package com.tenko.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import com.google.common.io.Files;
import com.tenko.ImgMap;
import com.tenko.test.MapListener;

public class MapDataUtils {
	
	private static File mapList;
	
	public static void init(){
		ImgMap.getInstance().getDataFolder().mkdir();
		
		mapList = new File(ImgMap.getInstance().getDataFolder(), "Maps.list");
		try {
			mapList.createNewFile();	
		} catch (IOException e){
			System.out.println("Failed to create Maps.list!");
			e.printStackTrace();
		}
		
		new File(ImgMap.getInstance().getDataFolder(), "SlideshowData").mkdir();
		new File(ImgMap.getInstance().getDataFolder(), "images").mkdir();
	}

	public static File getSlideshowFile(int id){
		return new File(ImgMap.getInstance().getDataFolder().getAbsolutePath() + "/SlideshowData/", String.valueOf(id + ".slideshow"));
	}
	
	public static File getList(){
		return mapList;
	}
	
	public static boolean add(int src, String dest){
		try {
			List<String> contents = Files.readLines(mapList, Charset.defaultCharset());
			BufferedWriter bw = new BufferedWriter(new FileWriter(mapList, false));
			
			boolean isAlreadyAdded = false;
			
			for(String s : contents){
				bw.write((isAlreadyAdded = s.startsWith(src+":")) ? src + ":" + dest : s);
				bw.newLine();
			}
			
			if(!isAlreadyAdded){
				bw.write(src + ":" + dest);
			}
			
			bw.flush();
			bw.close();
			
			MapListener.updateList();
			return true;
		} catch (IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean addArray(File f, Iterable<String> dest){
		try {
			BufferedWriter bw = Files.newWriter(f, Charset.defaultCharset());
			
			for(String l : dest){
				bw.write(l);
				bw.newLine();
			}

			bw.flush();
			bw.close();

			MapListener.updateList();
			return true;
		} catch (IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean deleteMapData(short target){
		try {
			List<String> contents = Files.readLines(mapList, Charset.defaultCharset());
			BufferedWriter bw = Files.newWriter(mapList, Charset.defaultCharset());
			
			for(String l : contents){
				if(!l.startsWith(String.valueOf(target)+":")){
					bw.write(l);
					bw.newLine();
				}
			}
			
			bw.flush();
			bw.close();
			
			MapListener.updateList();
			return true;
		} catch (IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean attemptDeleteSlideshow(int id){
		return getSlideshowFile(id).delete();
	}
}

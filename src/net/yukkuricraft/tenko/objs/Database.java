package net.yukkuricraft.tenko.objs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import net.yukkuricraft.tenko.ImgMap;
import net.yukkuricraft.tenko.render.GifRenderer;
import net.yukkuricraft.tenko.render.ImageRenderer;
import net.yukkuricraft.tenko.render.RenderUtils;

import org.bukkit.Bukkit;
import org.bukkit.map.MapView;

public class Database {
	
	private static File database;
	private static Database singleton;
	
	static{
		if(singleton == null){
			// This is a really bad setup; considering the fact that I'm dependent upon the chances
			// of the local image dir's parent to be ImgMap's datadir.
			singleton = new Database(new File(ImgMap.getLocalImagesDir().getParent(), "maps.dat"));
		}
	}
	
	private Database(File file) {
		if(!file.exists()){
			try{
				file.createNewFile();
			}catch (IOException e){
				ImgMap.logMessage("Failed to create flat-file database!");
				e.printStackTrace();
			}
		}
		
		database = file;
	}
	
	public static void saveImage(short id, String url, boolean isAnimated){
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(database, true));
			
			writer.write(String.valueOf(id));
			writer.write("|");
			writer.write(url);
			writer.write("|");
			writer.write(isAnimated ? 'a' : 's');
			writer.newLine();
			
			writer.flush();
			writer.close();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public static void deleteImage(short id){
		try{
			BufferedReader reader = new BufferedReader(new FileReader(database));
			ArrayList<String> contents = new ArrayList<>();
			// Shave off some time by not constructing a new StringBuffer every line.
			String beginning = id + "|";
			String line;
			while((line = reader.readLine()) != null){
				if(line.startsWith(beginning)){
					contents.add(line);
				}
			}
			reader.close();
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(database, false));
			for(String content : contents){
				writer.write(content);
			}
			writer.flush();
			writer.close();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void loadImages(){
		try{
			BufferedReader reader = new BufferedReader(new FileReader(database));
			
			String line;
			while((line = reader.readLine()) != null){
				if(line.indexOf("|") == -1){
					System.out.println("Database contained invalid line (Reason: Invalid format!): \"" + line + "\".");
					continue;
				}
				
				String[] strs = line.split("\\|");
				short id = Short.valueOf(strs[0]);
				String url = strs[1];
				String type = strs[2];
				
				MapView view = Bukkit.getMap(id);
				RenderUtils.removeRenderers(view);
				
				if(type.equalsIgnoreCase("s")){
					view.addRenderer(new ImageRenderer(new URL(url)));
				}else if(type.equalsIgnoreCase("a")){
					view.addRenderer(new GifRenderer(id, new URL(url)));
				}else{
					System.out.println("Database contained invalid line (Reason: Invalid render type!): \"" + type + "\".");
					continue;
				}
			}
			
			reader.close();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
}

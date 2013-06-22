package com.tenko;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.annotations.Beta;
import com.tenko.Gunvarrel.Gunvarrel;
import com.tenko.Gunvarrel.Parts.*;
import com.tenko.threading.MapThreadGroup;
import com.tenko.threading.PersistencyThread;
import com.tenko.threading.SlideshowThread;

/**
 * ImgMap - Maps become picture frames!
 * @author Tsunko
 * @version 3 Beta (It's the square root of 9!
 */
@Beta
public class ImgMap extends JavaPlugin {
	
	private static ImgMap instance;
	
	private final static MapThreadGroup group = new MapThreadGroup();
	
	private Gunvarrel commandHandler;
	
	/**
	 * Knockin' on heaven's door.
	 */
	@Override
	public void onEnable(){
		instance = this;
		
		//A new way to handle commands.
		commandHandler = new Gunvarrel();
		
		commandHandler.add(AniCommand.class, "ani");
		commandHandler.add(ImagesCommand.class, "images");
		commandHandler.add(MapCommand.class, "map");
		commandHandler.add(RestoreMapCommand.class, "rmap", "restoremap");
		commandHandler.add(SlideshowCommand.class, "smap");
	
		PersistencyThread pt = new PersistencyThread();
		pt.start();
	}
	
	@Override
	public void onDisable(){
		group.stopThreads();
	}
	
	public static ImgMap getPlugin(){
		return instance;
	}
	
	/**
	 * Gets the file and returns a File object.
	 * @return The Maps.list file.
	 */
	public static File getList(){
		return new File(ImgMap.getPlugin().getDataFolder(), "Maps.list");
	}

	/**
	 * Get slideshow by ID.
	 * @param id - The Map ID.
	 * @return A file for the Slideshow.
	 */
	public static File getSlideshowFile(int id){
		return new File(ImgMap.getPlugin().getDataFolder().getAbsolutePath() + "/SlideshowData/", String.valueOf(id + ".slideshow"));
	}

	/**
	 * Returns the custom threadgroup.
	 * @return MapThreadGroup.
	 */
	public static MapThreadGroup getThreadGroup(){
		return group;
	}
}

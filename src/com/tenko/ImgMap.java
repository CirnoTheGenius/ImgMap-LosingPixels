package com.tenko;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.tenko.Gunvarrel.Gunvarrel;
import com.tenko.Gunvarrel.Parts.ImagesCommand;
import com.tenko.Gunvarrel.Parts.MapCommand;
import com.tenko.Gunvarrel.Parts.RestoreMapCommand;
import com.tenko.Gunvarrel.Parts.SlideshowCommand;
import com.tenko.Gunvarrel.Parts.TestCommand;
import com.tenko.test.MapListener;
import com.tenko.utils.MapDataUtils;

//ByteFailure build.
public class ImgMap extends JavaPlugin {
	
	public static ImgMap pluginInstance;
	
	private Gunvarrel commandHandler;
	
	@Override
	public void onEnable(){
		pluginInstance = this;
		
		//Prepare the plugin folder.
		MapDataUtils.init();
		
		//Add commands.
		commandHandler = new Gunvarrel();
		commandHandler.add(MapCommand.class, "map");
		commandHandler.add(RestoreMapCommand.class, "restoremap");
		commandHandler.add(ImagesCommand.class, "images");
		commandHandler.add(SlideshowCommand.class, "smap");
		commandHandler.add(TestCommand.class, "test");
		
		Bukkit.getPluginManager().registerEvents(new MapListener(), this);
		//Load configuration (if any)
		//Load old data and set canvas to images.
		//Load slideshow data and set canvas to data.
	}
	
	public static ImgMap getInstance(){
		return pluginInstance;
	}
}

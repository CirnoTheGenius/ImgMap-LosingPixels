package com.tenko;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.plugin.java.JavaPlugin;

import com.tenko.cmdexe.CommanderCirno;
import com.tenko.rendering.ImageRenderer;
import com.tenko.utils.DataUtils;

public class ImgMap extends JavaPlugin {
	
	static ImgMap pl;
	private CommanderCirno cc = new CommanderCirno();
	
	/**
	 * Let's start it!
	 * Get chance and luck!
	 */
	public void onEnable(){
		pl = this;
		
		//Setting executors
		getCommand("map").setExecutor(cc);
		
		getCommand("imap").setExecutor(cc);
		getCommand("imgmap").setExecutor(cc);
		
		getCommand("restoremap").setExecutor(cc);
		getCommand("rmap").setExecutor(cc);
		
		//Usage
		getCommand("map").setUsage(ChatColor.BLUE + "Usage: /map <url>");
		
		getCommand("imap").setUsage(ChatColor.BLUE + "Usage: /imap");
		getCommand("imgmap").setUsage(ChatColor.BLUE + "Usage: /imgmap");
		
		getCommand("restoremap").setUsage(ChatColor.BLUE + "Usage: /restoremap");
		getCommand("rmap").setUsage(ChatColor.BLUE + "Usage: /rmap");
		
		//Prepare the .list file.
		//This whole chunk is prone to IOExceptions.
		try {
			DataUtils.checkFolder();
			File theList = DataUtils.createFile("Maps.list");
			for(String line : DataUtils.getLines(theList)){
				String[] l = line.split(":");
				MapView viewport = Bukkit.getServer().getMap(Short.valueOf(l[0]));

				for(MapRenderer mr : viewport.getRenderers()){
					viewport.removeRenderer(mr);
				}
				
				viewport.addRenderer(new ImageRenderer(ImgMap.getPlugin(), l[1]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ImgMap getPlugin(){
		return pl;
	}
	
	public File getList(){
		return new File(getDataFolder(), "Maps.list");
	}
}
package com.tenko.cmdexe;

import org.bukkit.ChatColor;

import com.tenko.ImgMap;

/**
 * Commands that can only be used by players.
 */
public class PlayerCommands {
	
	private final ImgMap plugin;
	
	public PlayerCommands(){
		this.plugin = ImgMap.getPlugin();
		
		plugin.getCommand("map").setUsage(ChatColor.BLUE + "Usage: /map <url|file>");
		plugin.getCommand("smap").setUsage(ChatColor.BLUE + "Usage: /smap <time> <url1|file> [url2|file] [url3|file] and so on.");
		plugin.getCommand("ani").setUsage(ChatColor.BLUE + "Usage: /ani <url|file>");
		plugin.getCommand("restoremap").setUsage(ChatColor.BLUE + "Usage: /restoremap");
		plugin.getCommand("rmap").setUsage(ChatColor.BLUE + "Usage: /rmap");
	}
	
}
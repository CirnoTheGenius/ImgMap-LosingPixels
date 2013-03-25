package com.tenko;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.tenko.cmdexe.CommanderCirno;

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
	}
	
	public static ImgMap getPlugin(){
		return pl;
	}
	
}
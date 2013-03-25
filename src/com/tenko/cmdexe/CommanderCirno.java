package com.tenko.cmdexe;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommanderCirno implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command c, String label, String[] args) {
		if(c.getName().equalsIgnoreCase("imgmap") || c.getName().equalsIgnoreCase("imap")){
			new ImgMapCommandExe(cs, args);
			return true;
		} else if(c.getName().equalsIgnoreCase("map")){
			if(args.length < 1){
				cs.sendMessage(ChatColor.RED + "[ImgMap] Not enough arguments!");
				return false;
			}
			new MapCommandExe(cs, args);
			return true;
		} else if(c.getName().equalsIgnoreCase("restoremap") || c.getName().equalsIgnoreCase("rmap")){
			new RestoreMapCommandExe(cs, args);
			return true;
		}
		return false;
	}
	
	
	
}

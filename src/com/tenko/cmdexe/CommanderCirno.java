package com.tenko.cmdexe;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommanderCirno implements CommandExecutor {
	
	/**
	 * Filters all the commands to the correct executors. (hurdur)
	 */
	@Override
	public boolean onCommand(CommandSender cs, Command c, String label, String[] args) {
		try {
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
		} catch (IOException e){
			cs.sendMessage(ChatColor.RED + "[ImgMap] Something really bad happend. Contact an admin or look at the console itself.");
			e.printStackTrace();
		}
		return false;
	}
	
}

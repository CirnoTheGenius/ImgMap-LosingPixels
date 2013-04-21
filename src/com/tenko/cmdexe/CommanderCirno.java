package com.tenko.cmdexe;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommanderCirno implements CommandExecutor {

	/**
	 * Filters all the commands to the correct executors. (hurdur)
	 */
	@Override
	public boolean onCommand(CommandSender cs, Command c, String label, String[] args) {
		try {
			if(cs instanceof Player){
				if(c.getName().equalsIgnoreCase("imgmap") || c.getName().equalsIgnoreCase("imap")){
					if(checkPermission(cs, "imgmap")){
						new ImgMapCommandExe(cs, args);
					}
					return true;
				}

				if(c.getName().equalsIgnoreCase("map")){
					if(args.length < 1){
						cs.sendMessage(ChatColor.RED + "[ImgMap] Not enough arguments!");
						return false;
					}
					if(checkPermission(cs, "map")){
						new MapCommandExe(cs, args);
					}
					return true;
				}

				if(c.getName().equalsIgnoreCase("maps")){
					if(checkPermission(cs, "maps")){
						new MapsCommandExe(cs, args);
					}
					return true;
				}

				if(c.getName().equalsIgnoreCase("smap")){
					if(args.length < 2){
						cs.sendMessage(ChatColor.RED + "[ImgMap] Not enough arguments!");
						return false;
					}
					if(checkPermission(cs, "slideshow")){
						new SlideshowCommandExe(cs, args);
					}
					return true;
				}


				if(c.getName().equalsIgnoreCase("restoremap") || c.getName().equalsIgnoreCase("rmap")){
					if(checkPermission(cs, "restoremap")){
						new RestoreMapCommandExe(cs, args);
					}
					return true;
				}
			} else {
				cs.sendMessage(ChatColor.RED + "[ImgMap] You must be a player.");
				return true;
			}

		} catch (IOException e){
			cs.sendMessage(ChatColor.RED + "[ImgMap] Something really bad happend. Contact an admin or look at the console itself.");
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Checks permission for a command.
	 * @param cs - The sender that we are trying to check.
	 * @param cmd - The command.
	 * @return Whether or not the player can use this command.
	 */
	public boolean checkPermission(CommandSender cs, String cmd) {
		if(cs.isOp()){
			return true;
		}
		return cs.hasPermission("imgmap.cmd." + cmd);
	}

}

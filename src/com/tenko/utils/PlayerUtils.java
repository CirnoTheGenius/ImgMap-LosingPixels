package com.tenko.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerUtils {

	/**
	 * Resolves a CommandSender to a Player.
	 * @param s - CommandSender that we're trying to get.
	 * @return The player from a CommandSender
	 */
	public static Player resolveToPlayer(CommandSender s){
		return Bukkit.getServer().getPlayer(s.getName());
	}

}

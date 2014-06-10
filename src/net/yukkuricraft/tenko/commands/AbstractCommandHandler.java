package net.yukkuricraft.tenko.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public abstract class AbstractCommandHandler implements CommandExecutor {
	
	private final int argumentCount;
	private final boolean playerOnly;
	private final boolean requiresMap;
	private final String permission;
	
	public AbstractCommandHandler(boolean playerOnly, boolean requiresMap, int argumentCount, String permission) {
		this.playerOnly = playerOnly;
		this.argumentCount = argumentCount;
		this.requiresMap = requiresMap;
		this.permission = permission;
	}
	
	public boolean preCommand(CommandSender cs, String[] args){
		if(!cs.hasPermission(permission)){
			cs.sendMessage(ChatColor.RED + "[ImgMap] I'm sorry " + cs.getName() + ", but I can't let you do that.");
			return false;
		}
		
		if(playerOnly && !(cs instanceof Player)){
			sendMessage(cs, "You must be a player to use this command!", ChatColor.RED);
			return false;
		}
		
		if(argumentCount > 0 && args.length < argumentCount){
			sendMessage(cs, "You're missing an argument.", ChatColor.RED);
			return false;
		}
		
		if(requiresMap && playerOnly && (cs instanceof Player) && !(((Player) cs).getItemInHand().getType() == Material.MAP)){
			sendMessage(cs, "You must be holding a map to use this command!", ChatColor.RED);
			return false;
		}
		
		return true;
	}
	
	public abstract boolean executeCommand(CommandSender cs, String[] args);
	
	// Eh. I don't want to have to keep rewriting the same thing over.
	protected void sendMessage(CommandSender cs, String str, ChatColor color){
		cs.sendMessage(color + "[ImgMap] " + str);
	}
	
	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
		if(preCommand(cs, args)){
			return executeCommand(cs, args);
		}
		
		return false;
	}
	
}

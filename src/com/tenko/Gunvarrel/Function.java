package com.tenko.Gunvarrel;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;

import com.tenko.objs.PlayerData;
import com.tenko.utils.PlayerUtils;

/**
 * Easy implementation. I'm lazy. Too lazy.
 * Duhuhu. Kthxbai.
 * @author Tsunko
 */
public abstract class Function implements CommandExecutor {
	
	protected boolean successful = false;
	public String result = "No result! Contact author if there is supposed to be one!";
	protected PlayerData data;

	@Override
	public abstract boolean onCommand(CommandSender cs, Command c, String l, String[] args);
	
	public boolean end(CommandSender cs){
		cs.sendMessage((successful ? ChatColor.BLUE : ChatColor.RED) + "[ImgMap] " + result);
		return successful;
	}
	
	public boolean end(CommandSender cs, Command c){
		cs.sendMessage((successful ? ChatColor.BLUE : ChatColor.RED) + "[ImgMap] " + result);
		cs.sendMessage(ChatColor.RED + "Usage: " + c.getUsage());
		return successful;
	}
	
	public PlayerData getData(){
		return this.data;
	}
	
	protected final PlayerData validateInput(CommandSender cs){
		ItemStack equipped = PlayerUtils.resolveToPlayer(cs).getItemInHand();
		
		if(equipped.getType() == Material.MAP){
			MapView viewport = Bukkit.getServer().getMap(equipped.getDurability());
			
			if(viewport != null){
				Iterator<MapRenderer> mr = viewport.getRenderers().iterator();
				while(mr.hasNext()){
					viewport.removeRenderer(mr.next());
				}
				
				viewport.setScale(Scale.FARTHEST);
				
				return data = new PlayerData(PlayerUtils.resolveToPlayer(cs), equipped, viewport);
			}
		}
		cs.sendMessage(ChatColor.RED + "[ImgMap] The currently equipped item is not a map!");
		return null;
	}
}

package com.tenko.cmdexe;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.tenko.ImgMap;
import com.tenko.rendering.ImageRenderer;
import com.tenko.utils.PlayerUtils;
import com.tenko.utils.URLUtils;

public class MapCommandExe implements CommandExe {
	
	public MapCommandExe(CommandSender cs, String[] args){
		Execute(cs, args);
	}
	
	@Override
	public void Execute(CommandSender cs, String[] args) {
		Player thePlayer = PlayerUtils.resolveToPlayer(cs);
		ItemStack equipped = thePlayer.getItemInHand();

		if(equipped.getType() != Material.MAP){
			cs.sendMessage(ChatColor.RED + "[ImgMap] The currently equipped item is not a map!");
			return;
		}

		if(!URLUtils.compatibleImage(args[0])){
			cs.sendMessage(ChatColor.RED + "[ImgMap] The specified image is not compatible!");
			return;
		}

		MapView viewport = Bukkit.getServer().getMap(equipped.getDurability());

		for(MapRenderer mr : viewport.getRenderers()){
			viewport.removeRenderer(mr);
		}
		
		viewport.addRenderer(new ImageRenderer(ImgMap.getPlugin(), args[0]));
		cs.sendMessage(ChatColor.GREEN + "[ImgMap] Rendering " + args[0]);
	}

	@Override
	public String getCommand() {
		return "map";
	}
}

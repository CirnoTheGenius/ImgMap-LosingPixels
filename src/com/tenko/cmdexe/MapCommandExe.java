package com.tenko.cmdexe;

import java.io.IOException;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;

import com.tenko.ImgMap;
import com.tenko.rendering.ImageRenderer;
import com.tenko.utils.DataUtils;
import com.tenko.utils.PlayerUtils;
import com.tenko.utils.URLUtils;

public class MapCommandExe implements CommandExe {
	
	/**
	 * "/map" command
	 * @param cs - Command sender.
	 * @param args - Arguments.
	 * @throws IOException
	 */
	public MapCommandExe(CommandSender cs, String[] args) throws IOException{
		Execute(cs, args);
	}

	@Override
	public void Execute(CommandSender cs, String[] args) throws IOException {
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
		
		viewport.setScale(Scale.FARTHEST);
		viewport.addRenderer(new ImageRenderer(args[0]));
		cs.sendMessage(ChatColor.GREEN + "[ImgMap] Rendering " + args[0]);
		
		if(ArrayUtils.contains(args, "-p")){
			DataUtils.deleteSlideshow(equipped.getDurability());
			DataUtils.replace(ImgMap.getList(), equipped.getDurability(), args[0]);
			cs.sendMessage(ChatColor.BLUE + "[ImgMap] Successfully saved this map's data!");
		}
	}

	@Override
	public String getCommand() {
		return "map";
	}
}

package com.tenko.cmdexe;

import java.io.IOException;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;

import com.tenko.ImgMap;
import com.tenko.rendering.SlideshowRenderer;
import com.tenko.utils.DataUtils;
import com.tenko.utils.PlayerUtils;
import com.tenko.utils.URLUtils;

public class SlideshowCommandExe implements CommandExe {

	@Override
	public void Execute(CommandSender cs, String[] args) throws IOException {
		Player thePlayer = PlayerUtils.resolveToPlayer(cs);
		ItemStack equipped = thePlayer.getItemInHand();
		
		//Yow. This may cause a lot of bandwith issues and lag.
		for(String urls : args){
			if(!URLUtils.compatibleImage(urls)){
				cs.sendMessage(ChatColor.RED + "[ImgMap] The specified image is not compatible!");
				return;
			}
		}
		
		MapView viewport = Bukkit.getServer().getMap(equipped.getDurability());
		
		for(MapRenderer mr : viewport.getRenderers()){
			viewport.removeRenderer(mr);
		}
		
		viewport.setScale(Scale.FARTHEST);
		viewport.addRenderer(new SlideshowRenderer(args, 2));
		cs.sendMessage(ChatColor.GREEN + "[ImgMap] Rendering " + args[0]);
		
		if(ArrayUtils.contains(args, "-p")){
			DataUtils.checkFile(String.valueOf(equipped.getDurability()), "SlideshowData");
			DataUtils.writeArray(ImgMap.getSlideshowFile(equipped.getDurability()), args);
			cs.sendMessage(ChatColor.BLUE + "[ImgMap] Successfully saved this map's data!");
		}
	}

	@Override
	public String getCommand() {
		return "slideshow";
	}
	
	
	
}

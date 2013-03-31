package com.tenko.cmdexe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
	
	public SlideshowCommandExe(CommandSender cs, String[] args) throws IOException{
		Execute(cs, args);
	}
	
	@Override
	public void Execute(CommandSender cs, String[] args) throws IOException {
		Player thePlayer = PlayerUtils.resolveToPlayer(cs);
		ItemStack equipped = thePlayer.getItemInHand();
		ArrayList<String> arguments = new ArrayList<String>();
		
		for(String arg : args){
			if(!arg.startsWith("-")){
				arguments.add(arg);
			}
		}
		
		float waitTime = Float.valueOf(arguments.remove(0));
		
		//Yow. This may cause a lot of bandwith issues and lag.
		for(String urls : arguments){
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
		String[] urls = Arrays.copyOf(arguments.toArray(), arguments.toArray().length, String[].class);
		viewport.addRenderer(new SlideshowRenderer(urls, waitTime));
		
		StringBuffer listOfURLs = new StringBuffer();
		for(String url : urls){
			listOfURLs.append(url + ", ");
		}
		
		cs.sendMessage(ChatColor.GREEN + "[ImgMap] Rendering " + listOfURLs.toString().substring(0, listOfURLs.length() - 2));
		
		if(ArrayUtils.contains(args, "-p")){
			File slideshowFile = ImgMap.getSlideshowFile(equipped.getDurability());
			arguments.remove("-p");
			DataUtils.blankFile(slideshowFile);
			DataUtils.write(slideshowFile, String.valueOf(waitTime));
			DataUtils.writeArray(slideshowFile, Arrays.copyOf(arguments.toArray(), arguments.size(), String[].class));
			cs.sendMessage(ChatColor.BLUE + "[ImgMap] Successfully saved this map's data!");
		}
	}

	@Override
	public String getCommand() {
		return "slideshow";
	}
}
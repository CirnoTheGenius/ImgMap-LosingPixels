package com.tenko.Gunvarrel.Parts;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.google.common.io.Files;
import com.tenko.ImgMap;
import com.tenko.Gunvarrel.Function;
import com.tenko.rendering.SlideshowRenderer;
import com.tenko.utils.DataUtils;
import com.tenko.utils.URLUtils;

public class SlideshowCommand extends Function {

	@Override
	public boolean onCommand(CommandSender cs, Command c, String l,	String[] args) {
		super.validateInput(cs, args);
		
		boolean isPermament = false;
		
		ArrayList<String> urls = new ArrayList<String>();
		ArrayList<String> locations = new ArrayList<String>();
		
		for(String arg : args){
			if(!arg.startsWith("-")){
				urls.add(arg);
			} else if(arg.equalsIgnoreCase("-p")){
				isPermament = true;
				continue;
			}
		}
		
		float waitTime;
		
		try {
			float tmpWaitTime = Float.valueOf(urls.remove(0));
			waitTime = tmpWaitTime > 0 ? tmpWaitTime : -1;
		} catch (NumberFormatException e){
			result = "Invalid value for <time>. Must be a number.";
			return end(cs, c);
		}
		
		for(String url : urls){
			try {
				URL urlz = new URL(url);
				if(!URLUtils.compatibleImage(urlz)){
					result = "The image: \"" + url + "\" is not compatible!";
					return end(cs);
				}
				locations.add(url);
			} catch (MalformedURLException e){
				try {
					File f = new File(ImgMap.getPlugin().getDataFolder(), "maps/" + url);
					if(f.exists()){
						locations.add(f.getAbsolutePath());
					}
				} catch (SecurityException e2){
					result = "Please give in URLs or paths relative to plugins/ImgMap/maps/";
					end(cs);
				}
			}
		}
		
		super.getData().getMap().addRenderer(new SlideshowRenderer(locations.toArray(new String[locations.size()]), waitTime));
		result = "Rendering slideshow!";
		successful = true;
		
		if(isPermament){
			try {
				File slideshowFile = ImgMap.getSlideshowFile(super.getData().getStack().getDurability());
				//Update timestamp.
				Files.touch(slideshowFile);
				
				String[] lines = new String[locations.size()];
				lines[0] = String.valueOf(waitTime);
				for(int i=1; i < lines.length; i++){
					lines[i] = locations.get(i);
				}
				
				DataUtils.writeArray(slideshowFile, lines);
				
				cs.sendMessage(ChatColor.BLUE + "[ImgMap] Successfully saved this map's data!");
			} catch (IOException e) {
				cs.sendMessage(ChatColor.RED + "[ImgMap] Failed to write to slideshow file!");
				e.printStackTrace();
			}
		}
		
		return end(cs);
	}

}

package com.tenko.Gunvarrel.Parts;

import java.io.IOException;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.tenko.ImgMap;
import com.tenko.Gunvarrel.Function;
import com.tenko.rendering.ImageRenderer;
import com.tenko.utils.DataUtils;
import com.tenko.utils.URLUtils;

/**
 * 
 * The first and base command for ImgMap.
 * @author Tsunko
 *
 */
public class MapCommand extends Function {

	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args) {
		//I wonder if it's necessary to even check anymore.
		if(c.getName().equalsIgnoreCase("map")){
			String location = "";
            String url;

			if(args.length < 1){
				result = "Not enough arguments!";
				return end(cs, c);
			} else if(cs.equals(Bukkit.getConsoleSender())){
				result = "You must be a player!";
				return end(cs);
			} else {
				url = args[0];
				if(URLUtils.isLocal(url)){
                    try {
                        location = URLUtils.getLocal(url);
                    } catch (Exception e){
                        result = "Weird result! The file apperently exists, but it actually doesn't!";
                    }
				} else if(!URLUtils.compatibleImage(url)){
                    result = "That image isn't compatible!";
                    return end(cs);
				}
				
				super.validateInput(cs);
				super.getData().getMap().addRenderer(new ImageRenderer(location));

				if(ArrayUtils.contains(args, "-p")){
					try {
						DataUtils.deleteSlideshow(super.getData().getStack().getDurability());
						DataUtils.replace(ImgMap.getList(), super.getData().getStack().getDurability(), URLUtils.isLocal(url) ? args[0] : location);
						cs.sendMessage(ChatColor.BLUE + "[ImgMap] Successfully saved this map's data!");
					} catch (IOException e) {
						cs.sendMessage(ChatColor.RED + "[ImgMap] Failed to save the map's data!");
						e.printStackTrace();
					}
				}

                result = "Rendering " + args[0];
                this.successful = true;
                return end(cs);
			}
		}
		
		return end(cs);
	}
	
	
	
	
}

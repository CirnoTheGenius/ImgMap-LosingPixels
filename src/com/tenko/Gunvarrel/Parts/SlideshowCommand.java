package com.tenko.Gunvarrel.Parts;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.tenko.Gunvarrel.Function;
import com.tenko.rendering.SlideshowRenderer;
import com.tenko.utils.ImageUtils;
import com.tenko.utils.MapDataUtils;

public class SlideshowCommand extends Function {

	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
		try {
			Player plyr = (Player)cs;
			boolean isPermament = false;
			
			ArrayList<String> urls = new ArrayList<String>();
			
			if(args.length == 0){
				notifySender(cs, "You didn't provide any arguments to use!", Result.FAILURE);
				return true;
			}

			if(!plyr.getItemInHand().getType().equals(Material.MAP)){
				notifySender(cs, "The currently equipped item isn't a map!", Result.FAILURE);
				return true;
			}
			
			short mapId = plyr.getItemInHand().getDurability();
			MapView view = Bukkit.getMap(mapId);
			
			if(args[0].equalsIgnoreCase("add")){
				//
				MapRenderer mr = view.getRenderers().get(0);
				if(mr instanceof SlideshowRenderer){
					SlideshowRenderer sr = (SlideshowRenderer)mr;
					
					for(int i=1; i < args.length; i++){
						sr.getUrls().add(args[i]);
					}
					
					notifySender(cs, "Sucessfully added all data!", Result.SUCCESS);
					return true;
				}
			}
			
			float waitTime = Float.valueOf(args[0]);
			Object[] tmp = ArrayUtils.remove(args, 0);
			String[] urlSArray = Arrays.copyOf(tmp, tmp.length, String[].class);
			
			//Heh, the old source code called for 2 for-loops. I just made it one long one.
			for(String arg : urlSArray){
				if(!arg.startsWith("-")){
					if(ImageUtils.isLocal(arg)){
						urls.add(ImageUtils.getLocalImage(arg).toURI().toURL().toExternalForm());
					} else if(ImageUtils.isImageCompatible(arg)){
						urls.add(arg);
					} else {
						notifySender(cs, "The image " + arg + " isn't compatible. Skipping over it.", Result.INFO);
					}
				} else if(arg.equalsIgnoreCase("-p")){
					isPermament = true;
				}
			}
			
			setRenderer(view, new SlideshowRenderer(urls, waitTime));
			notifySender(cs, "Slideshow has started!", Result.SUCCESS);
			
			if(isPermament){
				File slideshowFile = MapDataUtils.getSlideshowFile(mapId);
				if(MapDataUtils.addArray(slideshowFile, urls)){
					notifySender(cs, "Sucessfully saved map data!", Result.SUCCESS);					
				} else {
					notifySender(cs, "Failed to save data!", Result.FAILURE);					
				}
			}
			
			return true;
		} catch (ClassCastException e){
			notifySender(cs, "You must be a player!", Result.FAILURE);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return false;
	}



}

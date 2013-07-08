package com.tenko.Gunvarrel.Parts;

import java.io.IOException;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;

import com.tenko.Gunvarrel.Function;
import com.tenko.rendering.ImageRenderer;
import com.tenko.utils.ImageUtils;
import com.tenko.utils.MapDataUtils;

public class MapCommand extends Function {

	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
		try {
			Player plyr = (Player)cs;

			if(args.length == 0){
				notifySender(cs, "You didn't provide an URL to use!", Result.FAILURE);
				return true;
			}

			if(!plyr.getItemInHand().getType().equals(Material.MAP)){
				notifySender(cs, "The currently equipped item isn't a map!", Result.FAILURE);
				return true;
			}

			short mapId = plyr.getItemInHand().getDurability();
			MapView view = Bukkit.getMap(mapId);
			String location = "";
			
			if(ImageUtils.isLocal(args[0])){
				setRenderer(view, new ImageRenderer(ImageUtils.getLocalImage(args[0]).toURI().toURL().toExternalForm()));	
			} else {
				if(ImageUtils.isImageCompatible(args[0])){
					setRenderer(view, new ImageRenderer(args[0]));
				} else {
					notifySender(cs, "That image isn't compatible!", Result.FAILURE);					
					return true;
				}
			}
			
			if(super.getLastRenderer() instanceof ImageRenderer){
				location = ((ImageRenderer)super.getLastRenderer()).theUrl;
				notifySender(cs, "Currently rendering " + args[0], Result.SUCCESS);
			} else {
				//Theoretically, this block should never happen. But let's just check to make sure.
				location = args[0];
				notifySender(cs, "Couldn't get the URL of the renderer, saving may fail." + args[0], Result.INFO);
			}
			
			if(ArrayUtils.contains(args, "-p")){
				MapDataUtils.attemptDeleteSlideshow(mapId);
				if(MapDataUtils.add(mapId, location)){
					notifySender(cs, "Sucessfully saved map data!", Result.SUCCESS);					
				} else {
					notifySender(cs, "Failed to save data!", Result.FAILURE);					
				}
			}

			return true;
		} catch (ClassCastException e){ 
			notifySender(cs, "You must be a player to use this command.", Result.FAILURE);
		} catch (IOException e){
			notifySender(cs, "There was a read/write error with the provided URL! See console for more details.", Result.FAILURE);
			e.printStackTrace();
		}

		return true;
	}

}
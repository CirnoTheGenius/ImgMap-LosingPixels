package com.tenko.cmdexe;

import java.io.IOException;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.tenko.ImgMap;
import com.tenko.rendering.ImageRenderer;
import com.tenko.utils.DataUtils;
import com.tenko.utils.URLUtils;

public class MapCommandExe extends CommandExe {

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
		String location = "";

		if(URLUtils.compatibleImage(args[0])){
			location = args[0];
		} else if(!(location = URLUtils.isLocal(cs, args[0])).isEmpty()){
			//Do nothing, since it set the location here :3
		} else {
			cs.sendMessage(ChatColor.RED + "[ImgMap] The image specificed isn't compatible.");
			return;
		}
		
		//Validate after.
		validateInput(cs, args);
		
		getData().getMap().addRenderer(new ImageRenderer(location));
		cs.sendMessage(ChatColor.GREEN + "[ImgMap] Rendering " + args[0]);

		if(ArrayUtils.contains(args, "-p")){
			DataUtils.deleteSlideshow(getData().getStack().getDurability());
			DataUtils.replace(ImgMap.getList(), getData().getStack().getDurability(), args[0]);
			cs.sendMessage(ChatColor.BLUE + "[ImgMap] Successfully saved this map's data!");
		}
	}

	@Override
	public String getCommand() {
		return "map";
	}
}

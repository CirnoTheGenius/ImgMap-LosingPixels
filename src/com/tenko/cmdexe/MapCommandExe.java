package com.tenko.cmdexe;

import java.io.File;
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
		super.validateInput(cs, args);

		String location = "";

		if(args[0].startsWith("http://") && !URLUtils.compatibleImage(args[0])){
			cs.sendMessage(ChatColor.RED + "[ImgMap] The specified image is not compatible!");
			location = args[0];
			return;
		} else {
			try {
				// Check if really subdir (not sure if this is safe).
				File base = new File(ImgMap.getPlugin().getDataFolder(), "maps");
				File child = new File(base, args[0]);
				if (!child.getCanonicalPath().startsWith(base.getCanonicalPath())) {
					throw new SecurityException("Someone tried to do something nasty.");
				}
				location = child.getAbsolutePath();
			} catch (SecurityException e) {
				cs.sendMessage(ChatColor.RED + "[ImgMap] Please give in a URL or a path relative to plugins/ImgMap/maps/");
				return;
			}
		}

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

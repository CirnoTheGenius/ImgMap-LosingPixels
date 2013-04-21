package com.tenko.cmdexe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.google.common.io.Files;
import com.tenko.ImgMap;
import com.tenko.rendering.SlideshowRenderer;
import com.tenko.utils.DataUtils;
import com.tenko.utils.URLUtils;

public class SlideshowCommandExe extends CommandExe {

	public SlideshowCommandExe(CommandSender cs, String[] args) throws IOException{
		Execute(cs, args);
	}

	@Override
	public void Execute(CommandSender cs, String[] args) throws IOException {
		validateInput(cs, args);

		// read the URLs and check if the slideshow is to be permanent
		boolean isPermanent = false;
		ArrayList<String> urls = new ArrayList<String>();
		for(String arg : args){
			if(!arg.startsWith("-")){
				urls.add(arg);
			} else {
				if(arg.equalsIgnoreCase("-p")){
					isPermanent=true;
				}
			}
		}

		// Check if waitTime is valid and give a message if not.
		float waitTime;

		try {
			// remove waitTime
			waitTime = Float.valueOf(urls.remove(0));
		} catch (NumberFormatException e) {
			waitTime = -1;
		}

		if (waitTime <= 0) {
			cs.sendMessage(ChatColor.RED + "[ImgMap] Invalid value for <time>. Must be a positive number.");
			return;
		}

		ArrayList<String> locations = new ArrayList<String>();

		for(String url : urls){
			if (url.startsWith("http://")) {
				//Yow. This may cause a lot of bandwith issues and lag.
				if(!URLUtils.compatibleImage(url)){
					// Tell the user which image is not compatible.
					cs.sendMessage(ChatColor.RED + "[ImgMap] The image: \"" + url + "\" is not compatible!");
					//Let's not add it!
					continue;
				}
				locations.add(url);
			} else {
				try {
					// Check if really subdir (not sure if this is safe).
					File base=new File(ImgMap.getPlugin().getDataFolder(), "maps");
					File child=new File(base, url);
					if (!child.getCanonicalPath().startsWith(base.getCanonicalPath())) {
						throw new SecurityException("Someone tried to do something nasty.");
					}
					locations.add(child.getAbsolutePath());
				} catch (SecurityException e) {
					cs.sendMessage(ChatColor.RED + "[ImgMap] Please give in URLs or paths relative to plugins/ImgMap/maps/");
					return;
				}
			}
		}

		getData().getMap().addRenderer(new SlideshowRenderer(locations.toArray(new String[locations.size()]), waitTime));

		StringBuffer listOfURLs = new StringBuffer();
		Iterator<String> iter = urls.iterator();

		while (iter.hasNext()) {
			listOfURLs.append(iter.next());

			if (iter.hasNext()){
				listOfURLs.append(", ");
			}
		}

		cs.sendMessage(ChatColor.GREEN +"[ImgMap] Rendering "+ listOfURLs.toString());

		if(isPermanent){
			File slideshowFile = ImgMap.getSlideshowFile(getData().getStack().getDurability());

			Files.touch(slideshowFile);

			String[] lines = new String[1 + urls.size()];
			lines[0] = String.valueOf(waitTime);
			int i=1;
			for(String l : urls) {
				lines[i] = l;
				i++;
			}

			DataUtils.writeArray(slideshowFile, lines);
			cs.sendMessage(ChatColor.BLUE + "[ImgMap] Successfully saved this map's data!");
		}
	}

	@Override
	public String getCommand() {
		return "slideshow";
	}
}

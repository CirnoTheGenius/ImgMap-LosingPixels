package com.tenko.Gunvarrel.Parts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.tenko.ImgMap;
import com.tenko.Gunvarrel.Function;

public class ImagesCommand extends Function {

	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args) {
		File imagesFolder = new File(ImgMap.getInstance().getDataFolder(), "images");
		notifySender(cs, "Contents of \"/images/\"", Result.INFO);
		
		try {
			for(String s : listDir(imagesFolder.getCanonicalFile().length(), imagesFolder)){
				cs.sendMessage(ChatColor.GOLD + s);
			}
		} catch (IOException e){
			notifySender(cs, "Failed to read the images directory.", Result.FAILURE);
			e.printStackTrace();
		}
		
		return true;
	}

	private final ArrayList<String> listDir(long l, File dir){
		ArrayList<String> output = new ArrayList<String>();
		try {
			for(File f : dir.listFiles()){
				if(f.isDirectory()){
					output.addAll(listDir(l, f));
				} else {
					output.add(f.getCanonicalPath().substring((int)(l+1)));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return output;
	}

}

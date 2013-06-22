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
		File images = new File(ImgMap.getPlugin().getDataFolder(), "images");

		cs.sendMessage(ChatColor.YELLOW + "[ImgMap] Contents of the \"images\" directory:");

		try {
			for(String s : listDir(images.getCanonicalPath().length(), images)){
				cs.sendMessage(ChatColor.YELLOW + s);
			}
		} catch (IOException e){
			cs.sendMessage(ChatColor.RED + "Failed to read directory!");
			e.printStackTrace();
		}
		return false;
	}

	private ArrayList<String> listDir(int l, File dir) throws IOException {
		ArrayList<String> output = new ArrayList<String>();
		for (File f : dir.listFiles()) {
			if(f.isDirectory()){
				output.addAll(listDir(dir.getCanonicalPath().length(), f));
			} else {
				output.add(f.getCanonicalPath().substring(l+1));
			}
		}
		return output;
	}



}

package com.tenko.cmdexe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.tenko.ImgMap;

public class ImagesCommandExe extends CommandExe {

	/**
	 * "/images" command
	 * @param cs - Command sender.
	 * @param args - Arguments.
	 * @throws IOException
	 */
	public ImagesCommandExe(CommandSender cs, String[] args) throws IOException{
		Execute(cs, args);
	}

	@Override
	public void Execute(CommandSender cs, String[] args) throws IOException {
		File images = new File(ImgMap.getPlugin().getDataFolder(), "images");

		cs.sendMessage(ChatColor.YELLOW + "[ImgMap] Contents of the \"images\" directory:");

		for(String s : listDir(images.getCanonicalPath().length(), images)){
			cs.sendMessage(ChatColor.YELLOW + s);
		}
	}
	
	private ArrayList<String> listDir(int l, File dir) throws IOException, SecurityException {
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

	@Override
	public String getCommand() {
		return "maps";
	}
}

package com.tenko.cmdexe;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.tenko.ImgMap;

public class MapsCommandExe implements CommandExe {

	/**
	 * "/map" command
	 * @param cs - Command sender.
	 * @param args - Arguments.
	 * @throws IOException
	 */
	public MapsCommandExe (CommandSender cs, String[] args) throws IOException{
		Execute(cs, args);
	}

	private ArrayList<String> listDir (File dir) throws IOException, SecurityException {
		ArrayList<String> output=new ArrayList<String>();
		int len=dir.getCanonicalPath().length();
		for (File f : dir.listFiles()) {
			if (f.isDirectory())
				output.addAll(listDir(f));
			else
				output.add(f.getCanonicalPath().substring(len,f.getCanonicalPath().length()));
		}
		return output;
	}

	@Override
	public void Execute(CommandSender cs, String[] args) throws IOException {
		/*
		 * TODO: print a list of valid image files in plugins/ImgMap/maps/.
		 */
		File maps = new File(ImgMap.getPlugin().getDataFolder(), "maps");

		cs.sendMessage(ChatColor.YELLOW +"[ImgMap] Contents of the maps directory:");
		try {
			for (String f : listDir(maps))
				cs.sendMessage(ChatColor.YELLOW +"- "+f);
		}
		catch (Exception e) {
			cs.sendMessage(ChatColor.RED +"An ERROR occurred while listing the directory.");
		}
	}

	@Override
	public String getCommand() {
		return "maps";
	}
}

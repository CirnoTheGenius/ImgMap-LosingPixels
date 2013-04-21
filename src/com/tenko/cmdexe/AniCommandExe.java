package com.tenko.cmdexe;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.tenko.rendering.AnimatedRenderer;

public class AniCommandExe extends CommandExe {

	public AniCommandExe(CommandSender cs, String[] args) throws IOException{
		Execute(cs, args);
	}
	
	@Override
	public void Execute(CommandSender cs, String[] args) throws IOException {
		validateInput(cs, args);
		this.getData().getMap().addRenderer(new AnimatedRenderer(args[0]));
		cs.sendMessage(ChatColor.GREEN + "[ImgMap] Rendering " + args[0]);
	}

	@Override
	public String getCommand() {
		return "ani";
	}

}

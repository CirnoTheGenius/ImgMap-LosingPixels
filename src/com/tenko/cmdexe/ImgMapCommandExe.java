package com.tenko.cmdexe;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.tenko.utils.PlayerUtils;

public class ImgMapCommandExe implements CommandExe {

	public ImgMapCommandExe(CommandSender cs, String[] args){
		Execute(cs, args);
	}

	@Override
	public void Execute(CommandSender cs, String[] args){
		Player plyr = PlayerUtils.resolveToPlayer(cs);
		plyr.sendMessage("ImgMap by Cirno/Tsunko");
		plyr.sendMessage("Version 2");
	}

	@Override
	public String getCommand() {
		return "imgmap";
	}

}
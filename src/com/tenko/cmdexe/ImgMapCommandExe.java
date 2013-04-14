package com.tenko.cmdexe;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.tenko.utils.PlayerUtils;

public class ImgMapCommandExe extends CommandExe {
	
	/**
	 * "/imap" and "/imgmap" command. Nothing really.
	 * @param cs - Command sender.
	 * @param args - Arguments.
	 */
	public ImgMapCommandExe(CommandSender cs, String[] args){
		Execute(cs, args);
	}
	
	/**
	 * TODO: Add in settings and options.
	 */
	@Override
	public void Execute(CommandSender cs, String[] args){
		Player plyr = PlayerUtils.resolveToPlayer(cs);
		plyr.sendMessage("ImgMap by Cirno/Tsunko");
		plyr.sendMessage("Version 2beta");
	}

	@Override
	public String getCommand() {
		return "imgmap";
	}



}
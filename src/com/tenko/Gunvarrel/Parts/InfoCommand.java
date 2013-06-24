package com.tenko.Gunvarrel.Parts;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.tenko.ImgMap;
import com.tenko.Gunvarrel.Function;

public class InfoCommand extends Function {

	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args) {
		cs.sendMessage(ChatColor.BLUE + "ImgMap version " + ImgMap.getPlugin().getDescription().getVersion());
		//That Unicode escaping is "Tsunko".
		cs.sendMessage("ImgMap by Cirno/\u3064\u3093\u5B50");
		return end(cs);
	}

}

package com.tenko.cmdexe;

import org.bukkit.command.CommandSender;

public interface CommandExe {
	
	public void Execute(CommandSender cs, String[] args);
	
	public String getCommand();
	
}
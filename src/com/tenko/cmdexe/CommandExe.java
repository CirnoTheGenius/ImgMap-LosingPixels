package com.tenko.cmdexe;

import java.io.IOException;

import org.bukkit.command.CommandSender;

public interface CommandExe {

	/**
	 * The function that executes the command.
	 * @param cs - The CommandSender, used to get a Player.
	 * @param args - The arguments.
	 * @throws IOException
	 */
	public void Execute(CommandSender cs, String[] args) throws IOException;

	/**
	 * Returns the command name. Not really in use.
	 * @return Command name.
	 */
	public String getCommand();

}
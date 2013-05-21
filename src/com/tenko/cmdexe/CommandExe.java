package com.tenko.cmdexe;

import java.io.IOException;
import java.io.InvalidClassException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;

import com.tenko.objs.PlayerData;
import com.tenko.utils.PlayerUtils;

public class CommandExe {

	/**
	 * The PlayerData
	 */
	protected PlayerData data;

	/**
	 * The function that executes the command.
	 * @param cs - The CommandSender, used to get a Player.
	 * @param args - The arguments.
	 * @throws IOException
	 */
	public void Execute(CommandSender cs, String[] args) throws IOException {};

	/**
	 * Returns the command name. Not really in use.
	 * @return Command name.
	 */
	public String getCommand(){
		return null;
	}

	/**
	 * Get the PlayerData that is received from validateInput()
	 * @return
	 */
	public PlayerData getData(){
		return this.data;
	}

	/**
	 * Validates user input.
	 * @param cs
	 * @param args
	 * @return
	 * @throws InvalidClassException
	 */
	protected final PlayerData validateInput(CommandSender cs, String[] args) throws InvalidClassException{
		Player thePlayer = PlayerUtils.resolveToPlayer(cs);
		ItemStack equipped = thePlayer.getItemInHand();
		
		if(equipped.getType() != Material.MAP){
			cs.sendMessage(ChatColor.RED + "[ImgMap] The currently equipped item is not a map!");
			return null;
		}

		MapView viewport = Bukkit.getServer().getMap(equipped.getDurability());

		for(MapRenderer mr : viewport.getRenderers()){
			viewport.removeRenderer(mr);
		}

		viewport.setScale(Scale.FARTHEST);

		if(thePlayer != null && equipped != null && viewport != null){
			data = new PlayerData(thePlayer, equipped, viewport);
			return data;
		} else {
			throw new InvalidClassException("One of the parameters sent to PlayerData was not expected!");
		}
	}
}
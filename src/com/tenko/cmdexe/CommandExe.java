package com.tenko.cmdexe;

import java.io.IOException;
import java.io.InvalidClassException;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
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
	public void Execute(CommandSender cs, String[] args) throws IOException {/**/}

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
	 * @return PlayerData containing the player, equipment, and viewport.
	 * @throws InvalidClassException
	 */
	protected final PlayerData validateInput(CommandSender cs, String[] args) throws InvalidClassException{
		ItemStack equipped = PlayerUtils.resolveToPlayer(cs).getItemInHand();
		
		if(equipped.getType() == Material.MAP){
			MapView viewport = Bukkit.getServer().getMap(equipped.getDurability());
			
			if(viewport != null){
				Iterator<MapRenderer> mr = viewport.getRenderers().iterator();
				while(mr.hasNext()){
					viewport.removeRenderer(mr.next());
				}
				
				viewport.setScale(Scale.FARTHEST);
				
				return data = new PlayerData(PlayerUtils.resolveToPlayer(cs), equipped, viewport);
			}
		}
		cs.sendMessage(ChatColor.RED + "[ImgMap] The currently equipped item is not a map!");
		return null;
	}
}
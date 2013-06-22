package com.tenko.objs;

import java.io.InvalidClassException;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

/**
 * Basically MC's equivalent of a Tuple; except this one only takes a player, item stack, and MapView.
 * @author Tsunko
 */
public class PlayerData {

	/**
	 * The player object.
	 */
	private final Player plyr;

	/**
	 * The item stack.
	 */
	private final ItemStack itmstk;

	/**
	 * The MapView.
	 */
	private final MapView viewport;

	/**
	 * Creates a new PlayrStack.
	 * @param plyr - The player
	 * @param itmstk - The ItemStack (Auto-converts NMS ItemStacks to Bukkit's.)
	 * @param viewport - A mapview.
	 * @throws InvalidClassException - Thrown when itmstk isn't an ItemStack.
	 */
	public PlayerData(Player plyr, ItemStack itmstk, MapView viewport){
		this.plyr = plyr;
		this.viewport = viewport;
		this.itmstk = (ItemStack)itmstk;
	}

	public Player getPlayer(){
		return plyr;
	}

	public ItemStack getStack(){
		return itmstk;
	}

	public MapView getMap(){
		return viewport;
	}
}

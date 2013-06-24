package com.tenko.objs;

import java.io.InvalidClassException;

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
	 * @param player - The player
	 * @param itemsatck - The ItemStack (Auto-converts NMS ItemStacks to Bukkit's.)
	 * @param view - A mapview.
	 * @throws InvalidClassException - Thrown when itmstk isn't an ItemStack.
	 */
	public PlayerData(Player player, ItemStack itemstack, MapView view){
		this.plyr = player;
		this.viewport = view;
		this.itmstk = itemstack;
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

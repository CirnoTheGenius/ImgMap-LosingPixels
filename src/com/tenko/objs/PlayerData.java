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
	public PlayerData(Player plyr, Object itmstk, MapView viewport) throws InvalidClassException{
		this.plyr = plyr;
		this.viewport = viewport;

		if(itmstk instanceof org.bukkit.inventory.ItemStack){
			this.itmstk = (ItemStack)itmstk;
		} else if(itmstk instanceof net.minecraft.server.v1_5_R3.ItemStack){
			net.minecraft.server.v1_5_R3.ItemStack nmsstack = (net.minecraft.server.v1_5_R3.ItemStack)itmstk;
			this.itmstk = new ItemStack(Material.getMaterial(nmsstack.id));
			this.itmstk.setAmount(nmsstack.count);
			this.itmstk.setDurability((short)nmsstack.getData());
		} else {
			throw new InvalidClassException("Itmstk is supposed to be a NMS ItemStack or Bukkit ItemStack, not a " + itmstk.getClass() + "!");
		}
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

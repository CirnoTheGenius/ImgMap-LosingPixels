package Lib;

import org.bukkit.craftbukkit.v1_4_R1.CraftWorld;

import net.minecraft.server.v1_4_R1.Item;
import net.minecraft.server.v1_4_R1.ItemStack;
import net.minecraft.server.v1_4_R1.WorldMap;


public class NMSLib {
	//NMSLib: NMS (Net-Minecraft-Server) library. 
	
	/**
	 * Used to create a NMS Item.
	 * @param i - The Item to make.
	 */
	public static ItemStack instanceItemStack(Item i){
		return new ItemStack(i);
	}
	
	/**
	 * Creates a WorldMap using w and i.
	 * @param w - Casted CraftWorld World.
	 * @param i - The ID
	 * @return A constructed and working WorldMap
	 */
	public static WorldMap instanceWorldMap(CraftWorld w, int i){
		return (WorldMap)(w.getHandle().a(WorldMap.class, "map_" + i));
	}
	
}

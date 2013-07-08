package com.tenko.Gunvarrel.Parts;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.tenko.Gunvarrel.Function;
import com.tenko.utils.MapDataUtils;

//A little different from MapCommand, since there's a lot of nasty class objects.
public class RestoreMapCommand extends Function {
	
	private Class<?> craftbukkitRenderer, nmsItem, nmsItemStack, nmsWorldMap, craftbukkitWorld, craftbukkitMapView;
	
	private final boolean canUse;
	
	public RestoreMapCommand(){
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		String version = packageName.substring(packageName.lastIndexOf(".") + 1);

		try {
			craftbukkitWorld = Class.forName("org.bukkit.craftbukkit." + version + ".CraftWorld");
			craftbukkitMapView = Class.forName("org.bukkit.craftbukkit." + version + ".map.CraftMapView");
			craftbukkitRenderer= Class.forName("org.bukkit.craftbukkit." + version + ".map.CraftMapRenderer");
			//NMS <3
			nmsItemStack = Class.forName("net.minecraft.server." + version + ".ItemStack");
			nmsItem = Class.forName("net.minecraft.server." + version + ".Item");
			nmsWorldMap = Class.forName("net.minecraft.server." + version + ".WorldMap");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		canUse = true;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
		if(!canUse){
			notifySender(cs, "This command appears to be broken. Please notify the developers about this issue and please provide your Bukkit build number.", Result.FAILURE);
			return true;
		} else if(!(cs instanceof Player)){
			notifySender(cs, "You must be a player!", Result.FAILURE);
			return true;
		}
		
		Player plyr = (Player)cs;
		
		if(plyr.getItemInHand().getType() != Material.MAP){
			notifySender(cs, "The currently equipped item is not a map!", Result.FAILURE);
			return true;
		}
		
		MapView viewport = Bukkit.getMap(plyr.getItemInHand().getDurability());
		
		try {
			//Version independent version-specific methods. Don't touch.
			//Bukkit version independent.
			Object craftbukkitWorldObj = craftbukkitWorld.cast(plyr.getWorld());
			Object nmsWorldObj = craftbukkitWorldObj.getClass().getMethod("getHandle").invoke(craftbukkitWorldObj);
			Object nmsItemStackObj = nmsItemStack.getConstructor(nmsItem).newInstance(nmsItem.getField("MAP").get(null));

			nmsItemStackObj.getClass().getMethod("setData", int.class).invoke(nmsItemStackObj, plyr.getItemInHand().getData().getData());

			short id = Short.valueOf(String.valueOf(nmsItemStackObj.getClass().getMethod("getData").invoke(nmsItemStackObj)));

			Object preMap = nmsWorldObj.getClass().getMethod("a", Class.class, String.class).invoke(nmsWorldObj, nmsWorldMap, "map_"+id);
			Object craftMapObj = craftbukkitRenderer.getConstructor(craftbukkitMapView, nmsWorldMap).newInstance(craftbukkitMapView.cast(viewport), nmsWorldMap.cast(preMap));

			setRenderer(viewport, (MapRenderer)craftMapObj);	
			
			if(!MapDataUtils.deleteMapData(id)){
				notifySender(cs, "Failed to clear data for id " + id, Result.SUCCESS);
			}

			notifySender(cs, "Cleared Map ID " + id, Result.SUCCESS);
			return true;
			//There are so many ways this could go wrong.
			//Looks like a wave.
		} catch(NoSuchFieldException e){
			e.printStackTrace();
		} catch (NoSuchMethodException e){
			e.printStackTrace();
		} catch (SecurityException e){
			e.printStackTrace();
		} catch (IllegalAccessException e){
			e.printStackTrace();
		} catch (IllegalArgumentException e){
			e.printStackTrace();
		} catch (InvocationTargetException e){
			e.printStackTrace();
		} catch (InstantiationException e){
			e.printStackTrace();
		}
		
		return false;
	}

}

package com.tenko.Gunvarrel.Parts;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.ObjectUtils.Null;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.tenko.Gunvarrel.Function;
import com.tenko.utils.PlayerUtils;

public class RestoreMapCommand extends Function {

	@Override
	public boolean onCommand(CommandSender cs, Command c, String l,	String[] args) {
		ItemStack equipped = PlayerUtils.resolveToPlayer(cs).getItemInHand();
		
		if(equipped.getType() != Material.MAP){
			result = "The currently equipped item is not a map!";
		}
		
		MapView viewport = Bukkit.getServer().getMap(equipped.getDurability());
		for(MapRenderer r : viewport.getRenderers()){
			viewport.removeRenderer(r);
		}
		
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		String version = packageName.substring(packageName.lastIndexOf(".") + 1);

		Class craftMap, item, itemStack, worldMap, craftWorld, craftView;
		try {
			//Bukkit version-dependent.
			craftWorld = Class.forName("org.bukkit.craftbukkit." + version + ".CraftWorld");
			craftView = Class.forName("org.bukkit.craftbukkit." + version + ".CraftMapView");
			craftMap = Class.forName("org.bukkit.craftbukkit." + version + ".map.CraftMapRenderer");
			//NMS <3
			itemStack = Class.forName("net.minecraft.server." + version + ".ItemStack");
			item = Class.forName("net.minecraft.server." + version + ".Item");
			worldMap = Class.forName("net.minecraft.server." + version + ".WorldMap");

			
			//Version independent version-specific methods. Don't touch.
			Object is = Null.class;
			
			for(Object o : item.getEnumConstants()){
				if(o.toString().equalsIgnoreCase("MAP"))
					is = itemStack.getConstructor(item).newInstance(item.cast(o));
			}
			
			Object map = (worldMap.cast(((craftWorld.cast(PlayerUtils.resolveToPlayer(cs).getWorld())))));
			Object handle = worldMap.getDeclaredMethod("getHandle").invoke(null, null);
			Object newMap = handle.getClass().getDeclaredMethod("a", Class.class, String.class).invoke(worldMap, "map_" + is.getClass().getDeclaredMethod("getData").invoke(null, null));
			viewport.addRenderer((MapRenderer)craftMap.getConstructor(craftView, worldMap).newInstance(craftView.cast(viewport), worldMap.cast(map)));
		
			result = "Cleared Map ID " + equipped.getDurability();
			successful = true;
			//There are so many ways this could go wrong.
		//Looks like a wave.
		} catch (ClassNotFoundException e){
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
		return end(cs);
	}

}

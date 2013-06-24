package com.tenko.Gunvarrel.Parts;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import com.tenko.ImgMap;
import com.tenko.utils.DataUtils;

import org.bukkit.Bukkit;
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

		Class<?> craftMap, item, itemStack, worldMap, craftWorld, craftView;
		try {
			//Version independent version-specific methods. Don't touch.
			//Bukkit version-dependent.
			craftWorld = Class.forName("org.bukkit.craftbukkit." + version + ".CraftWorld");
			craftView = Class.forName("org.bukkit.craftbukkit." + version + ".map.CraftMapView");
			craftMap = Class.forName("org.bukkit.craftbukkit." + version + ".map.CraftMapRenderer");
			//NMS <3
			itemStack = Class.forName("net.minecraft.server." + version + ".ItemStack");
			item = Class.forName("net.minecraft.server." + version + ".Item");
			worldMap = Class.forName("net.minecraft.server." + version + ".WorldMap");
			
			Object cbWorld = craftWorld.cast(PlayerUtils.resolveToPlayer(cs).getWorld());
			Object nmsWorld = cbWorld.getClass().getMethod("getHandle").invoke(cbWorld);
			Object nmsItemStack = itemStack.getConstructor(item).newInstance(item.getField("MAP").get(null));
			
			nmsItemStack.getClass().getMethod("setData", int.class).invoke(nmsItemStack, PlayerUtils.resolveToPlayer(cs).getItemInHand().getData().getData());

			int id = (Integer)nmsItemStack.getClass().getMethod("getData").invoke(nmsItemStack);
			
			Object preMap = nmsWorld.getClass().getMethod("a", Class.class, String.class).invoke(nmsWorld, worldMap, "map_"+id);
			Object craftMapObj = craftMap.getConstructor(craftView, worldMap).newInstance(craftView.cast(viewport), worldMap.cast(preMap));
			
			viewport.addRenderer((MapRenderer)craftMapObj);
            try {
                DataUtils.delete(ImgMap.getList(), id);
            } catch (IOException e){
                cs.sendMessage("Failed to clear data for id " + id);
                e.printStackTrace();
            }

			result = "Cleared Map ID " + equipped.getDurability();
			successful = true;
		//There are so many ways this could go wrong.
		//Looks like a wave.
		} catch (ClassNotFoundException e){
			e.printStackTrace();
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
		return end(cs);
	}

}

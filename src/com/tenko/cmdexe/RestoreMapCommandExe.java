package com.tenko.cmdexe;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import net.minecraft.server.v1_5_R3.Item;
import net.minecraft.server.v1_5_R3.ItemStack;
import net.minecraft.server.v1_5_R3.WorldMap;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_5_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_5_R3.map.CraftMapRenderer;
import org.bukkit.craftbukkit.v1_5_R3.map.CraftMapView;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.tenko.ImgMap;
import com.tenko.utils.DataUtils;
import com.tenko.utils.PlayerUtils;
/* Volatile imports. */

public class RestoreMapCommandExe extends CommandExe {

	/**
	 * "/rmap" and "/restoremap" command.
	 * @param cs - Command sender.
	 * @param args - Arguments.
	 * @throws IOException
	 */
	public RestoreMapCommandExe(CommandSender cs, String[] args) throws IOException{
		Execute(cs, args);
	}

	@Override
	public void Execute(CommandSender cs, String[] args) throws IOException {
		org.bukkit.inventory.ItemStack equipped = PlayerUtils.resolveToPlayer(cs).getItemInHand();

		if(equipped.getType() != Material.MAP){
			cs.sendMessage(ChatColor.RED + "[ImgMap] The currently equipped item is not a map!");
		}

		MapView viewport = Bukkit.getServer().getMap(equipped.getDurability());
		for(MapRenderer r : viewport.getRenderers()){
			viewport.removeRenderer(r);
		}

		ItemStack is = new ItemStack(Item.MAP);
		is.setData(equipped.getData().getData());

		//NMS <3
		WorldMap map = (WorldMap)(((CraftWorld)PlayerUtils.resolveToPlayer(cs).getWorld()).getHandle().a(WorldMap.class, "map_" + is.getData()));
		viewport.addRenderer(new CraftMapRenderer((CraftMapView)viewport, map));

		cs.sendMessage(ChatColor.GREEN + "[ImgMap] Cleared Map #" + is.getData() + "!");

		if(!ArrayUtils.contains(args, "-t")){
			DataUtils.delete(ImgMap.getList(), is.getData());
			DataUtils.deleteSlideshow(is.getData());
		}
	}

	@Override
	public String getCommand() {
		return "restoremap";
	}

	//Okay, this is incredibly stupid, but here goes nothing.
	@SuppressWarnings("rawtypes")
	public Class[] setVersion() throws IOException, ClassNotFoundException{
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Enumeration<URL> sources = cl.getResources("net/minecraft/server");
		ArrayList<File> directories = new ArrayList<File>();
		
		while(sources.hasMoreElements()){
			directories.add(new File(sources.nextElement().getFile()));
		}
		
		ArrayList<Class> classes = new ArrayList<Class>();
		
		for(File f : directories){
			classes.addAll(getClasses(f, "net/minecraft/server"));
		}
		
		return classes.toArray(new Class[classes.size()]);
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList<Class> getClasses(File f, String pkg) throws ClassNotFoundException{
		ArrayList<Class> classes = new ArrayList<Class>();
		if(!f.exists()){
			return classes;
		}
		for(File f2 : f.listFiles()){
            classes.add(Class.forName(pkg + '.' + f2.getName().substring(0, f2.getName().length() - 6)));
		}
		return classes;
	}
}
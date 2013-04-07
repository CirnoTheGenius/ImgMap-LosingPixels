package com.tenko.cmdexe;

import java.io.IOException;

/* Volatile imports. */
import net.minecraft.server.v1_5_R2.Item;
import net.minecraft.server.v1_5_R2.ItemStack;
import net.minecraft.server.v1_5_R2.WorldMap;
import org.bukkit.craftbukkit.v1_5_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_5_R2.map.CraftMapRenderer;
import org.bukkit.craftbukkit.v1_5_R2.map.CraftMapView;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.tenko.ImgMap;
import com.tenko.utils.DataUtils;
import com.tenko.utils.PlayerUtils;

public class RestoreMapCommandExe implements CommandExe {
	
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

}
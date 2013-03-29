package com.tenko.cmdexe;

import java.io.IOException;

import net.minecraft.server.v1_4_R1.Item;
import net.minecraft.server.v1_4_R1.ItemStack;
import net.minecraft.server.v1_4_R1.WorldMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_4_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_4_R1.map.CraftMapRenderer;
import org.bukkit.craftbukkit.v1_4_R1.map.CraftMapView;
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
		DataUtils.delete(ImgMap.getList(), is.getData());
	}

	@Override
	public String getCommand() {
		return "restoremap";
	}

}
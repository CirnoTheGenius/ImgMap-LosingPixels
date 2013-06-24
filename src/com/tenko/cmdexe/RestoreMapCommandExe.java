package com.tenko.cmdexe;

import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

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

//		ItemStack is = new ItemStack(Item.MAP);
//		is.setData(equipped.getData().getData());
//
//		//NMS <3
        /*
             CraftWorld = (CraftWorld)getWorld()
             World = craftWorld.getHandle()
             WorldMap = invoke a(class, data)
         */
//		WorldMap map = (WorldMap)(((CraftWorld)PlayerUtils.resolveToPlayer(cs).getWorld()).getHandle().a(WorldMap.class, "map_" + is.getData()));
//		viewport.addRenderer(new CraftMapRenderer((CraftMapView)viewport, map));
//
//		cs.sendMessage(ChatColor.GREEN + "[ImgMap] Cleared Map #" + is.getData() + "!");
//
//		if(!ArrayUtils.contains(args, "-t")){
//			DataUtils.delete(ImgMap.getList(), is.getData());
//			DataUtils.deleteSlideshow(is.getData());
//		}
	}
	
}
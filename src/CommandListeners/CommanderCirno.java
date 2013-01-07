package CommandListeners;

import net.minecraft.server.v1_4_6.WorldMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_4_6.CraftWorld;
import org.bukkit.craftbukkit.v1_4_6.map.CraftMapRenderer;
import org.bukkit.craftbukkit.v1_4_6.map.CraftMapView;
import org.bukkit.entity.Player;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import Lib.FileType;
import Lib.IcicalLib;
import Lib.NMSLib;
import Rendering.ImgRenderer;
import cirno.DataSaver;
import cirno.Nineball;
import org.bukkit.inventory.ItemStack;

public class CommanderCirno implements CommandExecutor {

	private final Nineball Cirno;
	private final DataSaver dataM;

	public CommanderCirno(Nineball cirno)
	{
		this.Cirno = cirno;
		this.dataM = cirno.ds;
	}

	public Player resolveToPlayer(CommandSender s){
		return Bukkit.getServer().getPlayer(s.getName());
	}

	public boolean onCommand(CommandSender s, Command c, String l, String[] args){
		Player plyr = resolveToPlayer(s);
		ItemStack item;
		MapView mapV;

		if(!(plyr instanceof Player)){
			plyr.sendMessage(ChatColor.RED + "[ImgMap] You must be a player to use this command!");
			return true;
		} else {
			item = plyr.getItemInHand();
		}

		if(item.getType() == Material.MAP){
			mapV = Bukkit.getServer().getMap(item.getDurability());
		} else {
			plyr.sendMessage(ChatColor.RED + "[ImgMap] You must be equipped with a map to use any ImgMap commands!");
			return true;
		}

		if(c.getName().startsWith("map")){
			if(plyr.hasPermission("imgmap.render") || s.isOp()){
				if(args.length < 1){
					plyr.sendMessage(ChatColor.RED + "[ImgMap] That command requires an argument!");
					return true;
				}
			} else {
				plyr.sendMessage(ChatColor.RED + "Cirno sad. Cirno want you to access command, but Cirno cannot let you. Cirno will leak tears :'(");
				return true;
			}

			for(MapRenderer r : mapV.getRenderers()){
				mapV.removeRenderer(r);
			}
			/*
			if(args[0].equalsIgnoreCase("slideshow") && args.length < 3){
				ArrayList<String> urls = new ArrayList<String>();
				for(int i=1; i < args.length - 1; i++){
					if(Lib.IcicalLib.compatibleImage(args[i])) urls.add(args[i]);
				}
				mapV.addRenderer(new SlideshowRenderer(urls, Integer.valueOf(args[args.length-1]), Cirno));
				return true;
			} else */

			if(IcicalLib.compatibleImage(args[0])){
				switch(IcicalLib.getFlagType(args)){
					case 0:
						mapV.addRenderer(new ImgRenderer(args[0], Cirno));
						if(this.Cirno.getConfig().getBoolean("MapsDefaultPermament")) {
							dataM.setMapData(item.getDurability(), args[0]);
						}
						break;
						
					case 1:
						mapV.addRenderer(new ImgRenderer(args[0], Cirno));
						dataM.setMapData(item.getDurability(), args[0]);	
						break;

					case 2:
						if(IcicalLib.isLocalOrInternet(args[0], Cirno).equals(FileType.LOCAL)){
							mapV.addRenderer(new ImgRenderer(IcicalLib.getLocalURL(args[0], Cirno), Cirno));
							if(this.Cirno.getConfig().getBoolean("MapsDefaultPermament")) {
								dataM.setMapData(item.getDurability(), args[0]);
							}
							break;
						} else {
							plyr.sendMessage(ChatColor.GREEN + "[ImgMap] " + args[0] + " does not exist locally!");
							return true;
						}
					case 3:
						if(IcicalLib.isLocalOrInternet(args[0], Cirno).equals(FileType.LOCAL)){
							mapV.addRenderer(new ImgRenderer(IcicalLib.getLocalURL(args[0], Cirno), Cirno));
							dataM.setMapData(item.getDurability(), args[0]);
							break;
						} else {
							plyr.sendMessage(ChatColor.GREEN + "[ImgMap] " + args[0] + " does not exist locally!");
							return true;
						}
				}

				plyr.sendMessage(ChatColor.GREEN + "[ImgMap] Now rendering " + args[0]);
			}
			
			return true;

		}

		if(c.getName().equalsIgnoreCase("restoremap")){
			if(plyr.hasPermission("imgmap.clear") || s.isOp()){
				if(args.length > 0){
					plyr.sendMessage(ChatColor.RED + "[ImgMap] That command requires no arguments!");
					return true;
				}
			} else {
				plyr.sendMessage(ChatColor.RED + "Cirno sad. Cirno want you to access command, but Cirno cannot let you. Cirno will leak tears :'(");
				return true;
			}

			for(MapRenderer r : mapV.getRenderers()){
				mapV.removeRenderer(r);
			}

			//Hooray! NMS! Making notes for what does what.
			//Initialize a new map from NMS code.
			net.minecraft.server.v1_4_6.ItemStack newMap = new net.minecraft.server.v1_4_6.ItemStack(net.minecraft.server.v1_4_6.Item.MAP);
			//Setting the map ID to the current map's ID.
			newMap.setData(item.getData().getData());
			//Initialize the new WorldMap
			WorldMap wm = NMSLib.instanceWorldMap((CraftWorld)plyr.getWorld(), newMap.getData());
			//Add a new CraftWorldRenderer (the old way of rendering maps).
			mapV.addRenderer(new CraftMapRenderer((CraftMapView)mapV, wm));
			//Attempt to delete any old data from DataSaver.
			dataM.delMapData(mapV.getId());
		}
		return false;
	}
}
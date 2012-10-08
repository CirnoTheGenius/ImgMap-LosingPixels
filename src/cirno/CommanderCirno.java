package cirno;

import java.net.URL;
import java.util.ArrayList;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Item;
import net.minecraft.server.World;
import net.minecraft.server.WorldMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.map.CraftMapView;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class CommanderCirno implements CommandExecutor {

	Nineball cirno;

	protected static MapRenderer normalrender;
	protected DataSaver ds;

	public CommanderCirno(Nineball cirno){
		this.cirno = cirno;
		this.ds = new DataSaver(cirno);
	}

	protected final String[] Formats = {"jpg","jpeg","png","gif","bmp"};

	public Boolean checkForImgType(String url, CommandSender s){
		if(url.contains("goo.gl") || url.contains("bit.ly") || url.contains("tinyurl.com")){
			return true;
		}
		for(String urls : Formats){
			if(url.endsWith(urls))
				return true;
		}
		cirno.getServer().getPlayer(s.getName()).sendMessage(ChatColor.RED + "[ImgMap] Image format not supported!");
		return false;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().startsWith("map")){
			if(sender.hasPermission("imgmap.render") || sender.isOp()){
				if(!(args.length >= 1)){
					sender.sendMessage(ChatColor.RED + "[ImgMap] That commands requires an argument! Eg. /map example.com/img.jpg");
				}
				if(!(sender instanceof Player)){
					return false;
				}
				ItemStack item = cirno.getServer().getPlayer(sender.getName()).getItemInHand();
				MapView map;
				ArrayList<URL> urls = new ArrayList<URL>();

				if(item.getType() == Material.MAP){
					map = Bukkit.getServer().getMap(item.getDurability());
					map.getRenderers().clear();
					/*if(map.getRenderers().get(0).getClass() != SlideshowRenderer.class || map.getRenderers().get(0).getClass() != ImgRenderer.class){
						normalrender = map.getRenderers().get(0);
					}*/
				} else {
					cirno.getServer().getPlayer(sender.getName()).sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "[ImgMap] That isn't a map item!");
					return true;
				}

				if(args[0].equalsIgnoreCase("slideshow")){
					for(int i=1; i < args.length - 1; i++){
						if(checkForImgType(args[i], sender)) try {
							urls.add(new URL(args[i]));
						}
						catch (Exception e){}
					}
					map.addRenderer(new SlideshowRenderer(urls, Integer.valueOf(args[args.length - 1]), cirno));
					return true;
				} else {
					if(checkForImgType(args[0], sender)){
						map.addRenderer(new ImgRenderer(args[0], cirno));
						ds.setMapData(item.getDurability(), args[0]);
						cirno.getServer().getPlayer(sender.getName()).sendMessage(ChatColor.GREEN + "[ImgMap] Now rendering " + args[0]);	
					}
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Cirno sad. Cirno want you to access command, but Cirno cannot let you. Cirno will leak tears :'(");
				return true;
			}
		}

		if(command.getName().equalsIgnoreCase("restoremap") && (sender.hasPermission("imgmap.clear") || sender.isOp())){
			/*ItemStack item = cirno.getServer().getPlayer(sender.getName()).getItemInHand();	
			if(item.getType() == Material.MAP && normalrender != null){
				MapView map = Bukkit.getServer().getMap(item.getDurability());
				try{ map.removeRenderer(map.getRenderers().get(0));  }catch(Exception e){}
				map.addRenderer(normalrender);
				return true;
			} else {
				cirno.getServer().getPlayer(sender.getName()).sendMessage(ChatColor.RED + "[ImgMap] Could not restore the normal rendering!");
				return true;
			}*/
			ItemStack item = cirno.getServer().getPlayer(sender.getName()).getItemInHand();	
			if(item.getType() == Material.MAP){
				MapView map = Bukkit.getServer().getMap(item.getDurability());
				map.getRenderers().clear();
				net.minecraft.server.ItemStack item2 = new net.minecraft.server.ItemStack(new CirnoItem(cirno.getServer().getPlayer(sender.getName()).getItemInHand().getTypeId()));
		        WorldMap worldmap = (WorldMap)((net.minecraft.server.World)((CraftWorld)cirno.getServer().getPlayer(sender.getName()).getWorld()).getHandle()).a(WorldMap.class, "map_" + item2.getData());
				map.addRenderer(new RegularRenderer((CraftMapView)map, new WorldMap("map_" + item.getData())));
				return true;
			} else {
				cirno.getServer().getPlayer(sender.getName()).sendMessage(ChatColor.RED + "[ImgMap] Could not restore the normal rendering!");
				return true;
			}
		}
		return true;
	}



}

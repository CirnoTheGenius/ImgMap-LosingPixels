package net.yukkuricraft.tenko;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import net.minecraft.util.org.apache.commons.lang3.ArrayUtils;
import net.yukkuricraft.tenko.objs.Database;
import net.yukkuricraft.tenko.render.GifRenderer;
import net.yukkuricraft.tenko.render.ImageRenderer;
import net.yukkuricraft.tenko.render.RenderUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;
import org.bukkit.plugin.java.JavaPlugin;

public class ImgMap extends JavaPlugin {
	
	private static Logger pluginLogger;
	private Database database; // Just living in the database~ Wo-oh!
	private File localImages = new File(this.getDataFolder(), "localImgs");
	
	@Override
	public void onEnable(){
		ImgMap.pluginLogger = this.getLogger();
		this.getDataFolder().mkdir();
		this.database = new Database(new File(this.getDataFolder(), "maps.dat"));
		this.database.loadImages();
	}
	
	@Override
	public void onDisable(){
		ImgMap.pluginLogger = null;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender cs, Command c, String label, String[] args){
		if(c.getName().equalsIgnoreCase("drawimage")){
			if(!c.testPermission(cs)){
				return true;
			}
			
			if(cs instanceof Player){
				if(args.length > 0){
					Player plyr = (Player) cs;
					
					if(plyr.getItemInHand().getType() == Material.MAP){
						MapView view = Bukkit.getMap(plyr.getItemInHand().getDurability());
						RenderUtils.removeRenderers(view);
						
						ImageRenderer renderer;
						
						try{
							if(ArrayUtils.contains(args, "-l")){
								renderer = new ImageRenderer(new File(this.localImages, args[0]));
							}else{
								renderer = new ImageRenderer(args[0]);
							}
						}catch (IOException e){
							cs.sendMessage(ChatColor.RED + "[ImgMap] An error occured! Is the URL correct?");
							e.printStackTrace();
							return true;
						}
						
						view.addRenderer(renderer);
						for(Player player : plyr.getWorld().getPlayers()){
							player.sendMap(view);
						}
						
						cs.sendMessage(ChatColor.AQUA + "[ImgMap] Rendering " + args[0] + "!");
						
						if(ArrayUtils.contains(args, "-s")){
							this.database.saveImage(view.getId(), args[0], false);
							cs.sendMessage(ChatColor.AQUA + "[ImgMap] Saved information for ID#" + view.getId() + "!");
						}
					}else{
						cs.sendMessage(ChatColor.RED + "[ImgMap] You must be holding a map!");
					}
					return true;
				}
			}else{
				cs.sendMessage(ChatColor.RED + "[ImgMap] You need to be a player!");
			}
		}else if(c.getName().equalsIgnoreCase("drawanimatedimage")){
			if(!c.testPermission(cs)){
				return true;
			}
			
			if(cs instanceof Player){
				if(args.length > 0){
					Player plyr = (Player) cs;
					
					if(plyr.getItemInHand().getType() == Material.MAP){
						MapView view = Bukkit.getMap(plyr.getItemInHand().getDurability());
						RenderUtils.removeRenderers(view);
						GifRenderer renderer;
						
						try{
							if(ArrayUtils.contains(args, "-l")){
								renderer = new GifRenderer(new File(this.localImages, args[0]), view.getId());
							}else{
								renderer = new GifRenderer(args[0], view.getId());
							}
						}catch (IOException e){
							cs.sendMessage(ChatColor.RED + "[ImgMap] An error occured! Is the URL correct?");
							e.printStackTrace();
							return true;
						}
						
						view.addRenderer(renderer);
						cs.sendMessage(ChatColor.AQUA + "[ImgMap] Rendering " + args[0] + "!");
						
						if(ArrayUtils.contains(args, "-s")){
							this.database.saveImage(view.getId(), args[0], true);
							this.saveConfig();
							cs.sendMessage(ChatColor.AQUA + "[ImgMap] Saved information for ID#" + view.getId() + "!");
						}
					}else{
						cs.sendMessage(ChatColor.RED + "[ImgMap] You must be holding a map!");
					}
					return true;
				}
			}else{
				cs.sendMessage(ChatColor.RED + "[ImgMap] You need to be a player!");
			}
		}else if(c.getName().equalsIgnoreCase("clearmap")){
			if(!c.testPermission(cs)){
				return true;
			}
			
			if(cs instanceof Player){
				Player plyr = (Player) cs;
				
				if(plyr.getItemInHand().getType() == Material.MAP){
					MapView view = Bukkit.getMap(plyr.getItemInHand().getDurability());
					RenderUtils.removeRenderers(view);
					view.addRenderer(RenderUtils.getRendererForWorld(plyr.getItemInHand(), plyr.getWorld()));
					this.getConfig().set("MapId." + view.getId(), null);
					this.saveConfig();
					cs.sendMessage(ChatColor.AQUA + "[ImgMap] Restored default map rendering for map ID#" + view.getId() + "!");
				}else{
					cs.sendMessage(ChatColor.RED + "[ImgMap] You must be holding a map!");
				}
				return true;
			}else{
				cs.sendMessage(ChatColor.RED + "[ImgMap] You need to be a player!");
			}
		}else if(c.getName().equalsIgnoreCase("fixmap")){
			if(!c.testPermission(cs)){
				return true;
			}
			
			if(args.length > 0){
				try{
					Player plyr = (Player) cs;
					short id = Short.parseShort(args[0]);
					MapView view = Bukkit.getMap(id);
					ItemStack map = new ItemStack(Material.MAP);
					map.setDurability(id);
					map.setAmount(1);
					RenderUtils.removeRenderers(view);
					view.addRenderer(RenderUtils.getRendererForWorld(map, plyr.getWorld()));
					cs.sendMessage(ChatColor.AQUA + "[ImgMap] Attempted to remove all renderers for map ID#" + args[0]);
				}catch (NumberFormatException e){
					cs.sendMessage(ChatColor.RED + "[ImgMap] That isn't a number!");
				}
				return true;
			}else{
				cs.sendMessage(ChatColor.RED + "[ImgMap] Please provide a map id!");
			}
		}else if(c.getName().equalsIgnoreCase("getmap")){
			if(!c.testPermission(cs)){
				return true;
			}
			
			Player plyr = (Player) cs;
			ItemStack stack = new ItemStack(Material.MAP, 1);
			stack.setDurability(Short.valueOf(args[0]));
			plyr.setItemInHand(stack);
		}
		
		return false;
	}
	
	public static void logMessage(String message){
		ImgMap.pluginLogger.info(message);
	}
	
}

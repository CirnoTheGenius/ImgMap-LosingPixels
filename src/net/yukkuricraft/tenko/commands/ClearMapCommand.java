package net.yukkuricraft.tenko.commands;

import net.yukkuricraft.tenko.objs.Database;
import net.yukkuricraft.tenko.render.RenderUtils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

public class ClearMapCommand extends AbstractCommandHandler {
	
	public ClearMapCommand() {
		super(false, false, 0, "imgmap.clearmap");
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean executeCommand(CommandSender cs, String[] args){
		MapView view = null;
		ItemStack stack = null;
		
		if(args.length > 0){
			if(StringUtils.isNumeric(args[0])){
				view = Bukkit.getMap(Short.valueOf(args[0]));
				stack = new ItemStack(Material.MAP);
				stack.setDurability(view.getId());
				stack.setAmount(1);
			}else{
				this.sendMessage(cs, "You must provide a proper map ID!", ChatColor.RED);
				return true;
			}
		}else{
			if(cs instanceof Player){
				Player plyr = (Player) cs;
				view = Bukkit.getMap(plyr.getItemInHand().getDurability());
				stack = plyr.getItemInHand();
			}else{
				this.sendMessage(cs, "You cannot use this command without being a player OR providing a map ID as an argument.", ChatColor.RED);
				return true;
			}
		}
		
		if((view != null) && (stack != null)){
			RenderUtils.removeRenderers(view);
			view.addRenderer(RenderUtils.getRendererForWorld(stack, Bukkit.getWorlds().get(0)));
			Database.deleteImage(view.getId());
			this.sendMessage(cs, "Successfully cleared custom map data from map ID#" + view.getId(), ChatColor.GREEN);
			return true;
		}else{
			this.sendMessage(cs, "Something went wrong! View == null? " + (view == null) + " Stack == null? " + (stack == null), ChatColor.RED);
			return true;
		}
	}
	
}

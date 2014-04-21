package net.yukkuricraft.tenko.commands;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import net.yukkuricraft.tenko.render.RenderUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

public class FixMapCommand extends AbstractCommandHandler {
	
	public FixMapCommand() {
		super(false, false, 1);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean executeCommand(CommandSender cs, String[] args){
		if(StringUtils.isNumeric(args[0])){
			short id = Short.parseShort(args[0]);
			MapView view = Bukkit.getMap(id);
			ItemStack map = new ItemStack(Material.MAP);
			map.setDurability(id);
			map.setAmount(1);
			RenderUtils.removeRenderers(view);
			view.addRenderer(RenderUtils.getRendererForWorld(map, Bukkit.getWorlds().get(0)));
			sendMessage(cs, "Attempted to remove all renderers for map ID#" + id, ChatColor.YELLOW);
			return true;
		}else{
			sendMessage(cs, "Please provide a number.", ChatColor.RED);
		}
		
		return false;
	}
	
}

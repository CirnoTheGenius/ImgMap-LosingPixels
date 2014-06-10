package net.yukkuricraft.tenko.commands;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GetMapCommand extends AbstractCommandHandler {
	
	public GetMapCommand() {
		// The only one with true and false params.
		super(true, false, 1, "imgmap.debug.getmap");
	}
	
	@Override
	public boolean executeCommand(CommandSender cs, String[] args){
		if(StringUtils.isNumeric(args[0])){
			Player plyr = (Player) cs;
			ItemStack stack = new ItemStack(Material.MAP, 1);
			stack.setDurability(Short.valueOf(args[0]));
			plyr.setItemInHand(stack);
			return true;
		}else{
			sendMessage(cs, "Please provide a number.", ChatColor.RED);
		}
		
		return false;
	}
	
}

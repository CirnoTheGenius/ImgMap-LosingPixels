package net.yukkuricraft.tenko.commands;

import java.lang.reflect.Field;

import net.yukkuricraft.tenko.gbemulator.Input;
import net.yukkuricraft.tenko.render.GameRenderer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;

public class EmuActionCommand extends AbstractCommandHandler {
	
	public EmuActionCommand() {
		super(true, true, 1, "imgmap.gameboy");
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean executeCommand(CommandSender cs, String[] args){
		MapView view = Bukkit.getMap(((Player) cs).getItemInHand().getDurability());
		GameRenderer render = (GameRenderer) view.getRenderers().get(0);
		
		if(args[0].equalsIgnoreCase("save")){
			render.getMPU().save((Player) cs);
			cs.sendMessage(ChatColor.GREEN + "Saved!");
			return true;
		}else if(args[0].equalsIgnoreCase("load")){
			render.getMPU().load((Player) cs);
			cs.sendMessage(ChatColor.GREEN + "Loaded!");
			return true;
		}else if(args[0].equalsIgnoreCase("holdinput")){
			render.inverseHold();
			cs.sendMessage(ChatColor.GREEN + (render.shouldHoldInput() ? "Holding input!" : "Input is now click-per-input"));
			return true;
		}
		
		try{
			Input input = render.getMPU().getInputHandler().getInput();
			if(render.shouldHoldInput() && (args[0].equalsIgnoreCase("down") || args[0].equalsIgnoreCase("up") || args[0].equalsIgnoreCase("left") || args[0].equalsIgnoreCase("right"))){
				boolean change = !(boolean) Input.class.getField("h" + args[0]).get(input);
				
				if(change){
					for(Field field : Input.class.getFields()){
						if(field.getName().startsWith("h")){
							field.set(input, false);
						}
					}
				}
				
				Input.class.getField("h" + args[0]).set(input, change);
			}
			
			Input.class.getField(args[0]).set(render.getMPU().getInputHandler().getInput(), true);
		}catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e){
			e.printStackTrace();
		}
		return true;
	}
}

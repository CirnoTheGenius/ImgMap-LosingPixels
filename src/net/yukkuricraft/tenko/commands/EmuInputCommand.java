package net.yukkuricraft.tenko.commands;

import net.yukkuricraft.tenko.render.GameRenderer;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;

public class EmuInputCommand extends AbstractCommandHandler {
	
	public EmuInputCommand() {
		super(true, true, 1);
	}
	
	@Override
	public boolean executeCommand(CommandSender cs, String[] args) {
		@SuppressWarnings("deprecation")
		MapView view = Bukkit.getMap(((Player) cs).getItemInHand().getDurability());
		GameRenderer render = (GameRenderer) view.getRenderers().get(0);
		// render.getCPU().getGUI().input(GameBoyInput.valueOf(args[0].toUpperCase()));
		render.getCPU().joypadInt();
		return true;
	}
	
}

package net.yukkuricraft.tenko.commands;

import net.yukkuricraft.tenko.render.GameRenderer;
import net.yukkuricraft.tenko.render.RenderUtils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;

public class EmulateGBCommand extends AbstractCommandHandler {
	
	public EmulateGBCommand() {
		super(true, true, 1, "imgmap.gameboy");
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean executeCommand(CommandSender cs, String[] args){
		MapView view = Bukkit.getMap(((Player) cs).getItemInHand().getDurability());
		RenderUtils.removeRenderers(view);
		view.addRenderer(new GameRenderer(args[0], cs.getName()));
		// Workaround to using JSON without NMS hooks or anything.
		tellraw(cs, "{\"extra\":[{\"text\":\"\u2588--\"},{\"text\":\"-U-\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/emuaction up\"}},{\"text\":\"--\u2588\"},{\"text\":\"\u24B6\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/emuaction a\"}}],\"text\":\"\"}");
		tellraw(cs, "{\"extra\":[{\"text\":\"\u2588\"},{\"text\":\"-L-\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/emuaction left\"}},{\"text\":\"-\"},{\"text\":\"-R-\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/emuaction right\"}},{\"text\":\"\u2588\"},{\"text\":\"\u24B7\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/emuaction b\"}}],\"text\":\"\"}");
		tellraw(cs, "{\"extra\":[{\"text\":\"\u2588--\"},{\"text\":\"-D-\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/emuaction down\"}},{\"text\":\"--\u2588\"},{\"text\":\"SRT\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/emuaction start\"}},{\"text\":\" \"},{\"text\":\"SEL\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/emuaction select\"}}],\"text\":\"\"}");
		return false;
	}
	
	private void tellraw(CommandSender cs, String json){
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + cs.getName() + " " + json);
	}

}

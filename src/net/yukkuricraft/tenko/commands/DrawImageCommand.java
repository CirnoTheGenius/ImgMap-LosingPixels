package net.yukkuricraft.tenko.commands;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import net.minecraft.util.org.apache.commons.lang3.ArrayUtils;
import net.yukkuricraft.tenko.ImgMap;
import net.yukkuricraft.tenko.objs.Database;
import net.yukkuricraft.tenko.render.ImageRenderer;
import net.yukkuricraft.tenko.render.RenderUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;

public class DrawImageCommand extends AbstractCommandHandler {
	
	public DrawImageCommand() {
		super(true, true, 1, "imgmap.drawimage");
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean executeCommand(CommandSender cs, String[] args){
		Player plyr = (Player) cs;
		MapView viewport = Bukkit.getMap(plyr.getItemInHand().getDurability());
		ImageRenderer renderer;
		
		try{
			URL toDraw = ArrayUtils.contains(args, "-l") ? new File(ImgMap.getLocalImagesDir(), args[0]).toURL() : new URL(args[0]);
			renderer = new ImageRenderer(toDraw);
			RenderUtils.removeRenderers(viewport);
			viewport.addRenderer(renderer);
			
			for(Player onlinePlyr : Bukkit.getOnlinePlayers()){
				if(onlinePlyr.getWorld().equals(plyr.getWorld())){
					onlinePlyr.sendMap(viewport);
				}
			}
			
			cs.sendMessage(ChatColor.AQUA + "[ImgMap] Rendering " + args[0] + "!");
			
			if(ArrayUtils.contains(args, "-s")){
				Database.saveImage(viewport.getId(), toDraw.toExternalForm(), false);
				cs.sendMessage(ChatColor.AQUA + "[ImgMap] Saved information for ID#" + viewport.getId() + "!");
			}
			return true;
		}catch (IOException e){
			this.sendMessage(cs, "Encountered error while grabbing the image.", ChatColor.RED);
			e.printStackTrace();
			return true;
		}
	}
	
}

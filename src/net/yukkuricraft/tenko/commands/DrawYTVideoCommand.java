package net.yukkuricraft.tenko.commands;

import java.io.File;

import net.yukkuricraft.tenko.ImgMap;
import net.yukkuricraft.tenko.render.DummyRenderer;
import net.yukkuricraft.tenko.render.RenderUtils;
import net.yukkuricraft.tenko.video.YTAPIVideoObj;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;

public class DrawYTVideoCommand extends AbstractCommandHandler {
	
	public DrawYTVideoCommand() {
		super(true, true, 1);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean executeCommand(CommandSender cs, String[] args){
		if(ImgMap.allowVideos()){
			MapView view = Bukkit.getMap(((Player) cs).getItemInHand().getDurability());
			RenderUtils.removeRenderers(view);
			YTAPIVideoObj obj = new YTAPIVideoObj(new File(ImgMap.getLocalVideosDir(), args[0] + ".gif"), args[0], view.getId());
			
			obj.startDownload(cs);
			cs.sendMessage(ChatColor.AQUA + "[ImgMap] Downloading " + args[0] + "! Please wait.");
			view.addRenderer(new DummyRenderer());
			return true;
		}else{
			cs.sendMessage(ChatColor.RED + "[ImgMap] Videos are disabled by default! Enable them in the configuration file.");
			return true;
		}
	}
	
}

package net.yukkuricraft.tenko;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.yukkuricraft.tenko.commands.AbstractCommandHandler;
import net.yukkuricraft.tenko.commands.ClearMapCommand;
import net.yukkuricraft.tenko.commands.DrawAnimatedImageCommand;
import net.yukkuricraft.tenko.commands.DrawImageCommand;
import net.yukkuricraft.tenko.commands.DrawYTVideoCommand;
import net.yukkuricraft.tenko.commands.EmuInputCommand;
import net.yukkuricraft.tenko.commands.FixMapCommand;
import net.yukkuricraft.tenko.commands.GetMapCommand;
import net.yukkuricraft.tenko.objs.Database;
import net.yukkuricraft.tenko.render.GameRenderer;
import net.yukkuricraft.tenko.render.RenderUtils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;
import org.bukkit.plugin.java.JavaPlugin;

public class ImgMap extends JavaPlugin {
	
	private static Logger pluginLogger;
	
	private static File localImages;
	private static File localVideos;
	private static File ffmpeg;
	
	private static boolean allowVideos = false;
	
	private Map<String, AbstractCommandHandler> handlers = new HashMap<String, AbstractCommandHandler>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 0xDEADBEEF;
		
		{
			this.put("clearmap", new ClearMapCommand());
			this.put("drawanimatedimage", new DrawAnimatedImageCommand());
			this.put("drawimage", new DrawImageCommand());
			this.put("drawytvideo", new DrawYTVideoCommand());
			this.put("fixmap", new FixMapCommand());
			this.put("getmap", new GetMapCommand());
			this.put("emuinput", new EmuInputCommand());
		}
	};
	
	@Override
	public void onEnable(){
		ImgMap.localImages = new File(this.getDataFolder(), "localImgs");
		ImgMap.localVideos = new File(this.getDataFolder(), "localVids");
		ImgMap.pluginLogger = this.getLogger();
		
		this.getDataFolder().mkdir();
		Database.loadImages();
		
		if(!ImgMap.localImages.exists()){
			ImgMap.localImages.mkdir();
		}
		
		if(!ImgMap.localVideos.exists()){
			ImgMap.localVideos.mkdir();
		}
		
		this.saveDefaultConfig();
		ImgMap.allowVideos = this.getConfig().getBoolean("AllowVideos");
		
		if(ImgMap.allowVideos){
			ImgMap.ffmpeg = new File(ImgMap.localVideos, "ffmpeg.exe");
			if(ImgMap.ffmpeg.exists()){
				ImgMap.logMessage("Detected Windows FFmpeg! Videos are safe to use!");
			}else{
				ImgMap.ffmpeg = new File(ImgMap.localVideos, "ffmpeg");
				if(ImgMap.ffmpeg.exists()){
					ImgMap.logMessage("Detected Linux FFmpeg! Videos are safe to use!");
				}else{
					ImgMap.logMessage("Did not detect FFmpeg! Don't use videos!");
				}
			}
		}
		
		ImgMap.logMessage("Successfully loaded ImgMap!");
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
		AbstractCommandHandler handler = this.handlers.get(c.getName().toLowerCase());
		
		if(c.getName().equalsIgnoreCase("v/")){
			MapView view = Bukkit.getMap(((Player) cs).getItemInHand().getDurability());
			RenderUtils.removeRenderers(view);
			view.addRenderer(new GameRenderer());
		}
		
		if(handler != null){
			return handler.onCommand(cs, c, l, args);
		}
		
		return false;
	}
	
	public static File getFFmpeg(){
		return ImgMap.ffmpeg;
	}
	
	public static File getLocalImagesDir(){
		return ImgMap.localImages;
	}
	
	public static File getLocalVideosDir(){
		return ImgMap.localVideos;
	}
	
	public static boolean allowVideos(){
		return ImgMap.allowVideos;
	}
	
	@Override
	public void onDisable(){
		ImgMap.pluginLogger = null;
		ImgMap.ffmpeg = null;
	}
	
	public static void logMessage(String message){
		ImgMap.pluginLogger.info(message);
	}
	
}

package net.yukkuricraft.tenko;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.yukkuricraft.tenko.commands.*;
import net.yukkuricraft.tenko.objs.Database;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ImgMap extends JavaPlugin {
	
	private static Logger pluginLogger;
	private static File ffmpeg;
	
	private static File localImages, localVideos, localROMs;
	
	private static boolean allowVideos = false;
	
	private Map<String, AbstractCommandHandler> handlers = new HashMap<String, AbstractCommandHandler>();
	
	{
		handlers.put("clearmap", new ClearMapCommand());
		handlers.put("drawanimatedimage", new DrawAnimatedImageCommand());
		handlers.put("drawimage", new DrawImageCommand());
		handlers.put("drawytvideo", new DrawYTVideoCommand());
		handlers.put("fixmap", new FixMapCommand());
		handlers.put("getmap", new GetMapCommand());
		handlers.put("emulategb", new EmulateGBCommand());
		handlers.put("emuaction", new EmuActionCommand());
	}
	
	@Override
	public void onEnable(){
		pluginLogger = this.getLogger();
		this.getDataFolder().mkdir();
		localImages = dir("localImgs");
		localVideos = dir("localVids");
		localROMs = dir("localROMs");
		
		Database.loadImages();
		
		this.saveDefaultConfig();
		allowVideos = this.getConfig().getBoolean("AllowVideos");
		
		if(allowVideos){
			ffmpeg = new File(this.getDataFolder(), "ffmpeg.exe");
			if(ffmpeg.exists()){
				logMessage("Detected Windows FFmpeg! Videos are safe to use!");
			}else{
				ffmpeg = new File(this.getDataFolder(), "ffmpeg");
				if(ffmpeg.exists()){
					logMessage("Detected Linux FFmpeg! Videos are safe to use!");
				}else{
					logMessage("Did not detect FFmpeg! YouTube \"streaming\" has been disabled.");
					allowVideos = false;
				}
			}
		}
		
		logMessage("Successfully loaded ImgMap!");
	}
	
	private File dir(String dir){
		File f = new File(this.getDataFolder(), dir);
		if(!f.exists()){
			f.mkdir();
		}
		return f;
	}
	
	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
		AbstractCommandHandler handler = this.handlers.get(c.getName().toLowerCase());
		
		if(handler != null){
			handler.onCommand(cs, c, l, args);
			return true;
		}
		
		return false;
	}
	
	public static File getFFmpeg(){
		return ffmpeg;
	}
	
	public static File getLocalImagesDir(){
		return localImages;
	}
	
	public static File getLocalROMsDir(){
		return localROMs;
	}
	
	public static File getLocalVideosDir(){
		return localVideos;
	}
	
	public static boolean allowVideos(){
		return allowVideos;
	}
	
	@Override
	public void onDisable(){
		pluginLogger = null;
	}
	
	public static void logMessage(String message){
		pluginLogger.info(message);
	}
	
}

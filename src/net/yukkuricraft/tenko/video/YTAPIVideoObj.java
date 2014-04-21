package net.yukkuricraft.tenko.video;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import net.yukkuricraft.tenko.ImgMap;
import net.yukkuricraft.tenko.render.GifRenderer;
import net.yukkuricraft.tenko.threading.CallbackThread;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.map.MapView;

public class YTAPIVideoObj implements RemoteVideoObj {
	
	private File saveLocation;
	private String id;
	private short mapId;
	
	public YTAPIVideoObj(File saveTo, String ytID, short mapId) {
		this.id = ytID;
		this.saveLocation = saveTo;
		this.mapId = mapId;
	}
	
	private class DownloadRunnable implements Runnable {
		
		@Override
		public void run(){
			try{
				ProcessBuilder builder = new ProcessBuilder();
				builder.command(ImgMap.getFFmpeg().getAbsolutePath(), "-i", "http://www.ytapi.com/api/" + YTAPIVideoObj.this.id + "/direct/160/", "-r", "10", "-threads", "0", "-vf", "scale=128:128,format=rgb8,format=rgb24", "-y", YTAPIVideoObj.this.saveLocation.getAbsolutePath());
				builder.redirectErrorStream(true);
				Process process = builder.start();
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line;
				while((line = reader.readLine()) != null){
					ImgMap.logMessage("FFmpeg -> " + line);
				}
				reader.close();
			}catch (IOException e){
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public void startDownload(final CommandSender cs){
		CallbackThread.create(new DownloadRunnable(), new Runnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run(){
				MapView viewport = Bukkit.getMap(YTAPIVideoObj.this.mapId);
				GifRenderer renderer = null;
				
				try{
					cs.sendMessage(ChatColor.AQUA + "[ImgMap] Completed downloading " + YTAPIVideoObj.this.id + ". Please wait as the plugin now must cache the video.");
					renderer = new GifRenderer(viewport.getId(), YTAPIVideoObj.this.saveLocation.toURL());
					viewport.addRenderer(renderer);
				}catch (IOException e){
					cs.sendMessage(ChatColor.RED + "[ImgMap] Completed downloading " + YTAPIVideoObj.this.id + ", but we failed at adding the new renderer!");
					viewport.removeRenderer(renderer);
					e.printStackTrace();
				}
			}
			
		}).start();
	}
}

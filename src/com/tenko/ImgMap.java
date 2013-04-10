package com.tenko;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.Files;
import com.tenko.cmdexe.CommanderCirno;
import com.tenko.rendering.ImageRenderer;
import com.tenko.rendering.SlideshowRenderer;
import com.tenko.threading.MapThreadGroup;
import com.tenko.threading.SlideshowThread;
import com.tenko.utils.DataUtils;
import com.tenko.utils.ImageUtils;

/**
 * ImgMap - Maps become picture frames!
 * @author Tsunko
 * @version 2 Alpha
 */
public class ImgMap extends JavaPlugin {

	/**
	 * Static plugin. Unsafe, probably.
	 */
	static ImgMap pl;

	/**
	 * Command handler for all the commands.
	 */
	private CommanderCirno cc = new CommanderCirno();

	/**
	 * Slideshow threading group.
	 */
	private final static MapThreadGroup group = new MapThreadGroup();

	/**
	 * Ignore this. I use this to test the utils and functions.
	 */
	public static void main(String[] args) throws Exception {

	}

	/**
	 * Let's start it!
	 * Get chance and luck!
	 */
	@Override
	public void onEnable(){
		String[] cmds = new String[]{
				"map", 
				"smap", 
				"imap", "imgmap",
				"restoremap", "rmap"
		};

		pl = this;

		//Usage
		getCommand("map").setUsage(ChatColor.BLUE + "Usage: /map <url>");
		getCommand("smap").setUsage(ChatColor.BLUE + "Usage: /smap <time> <url1> [url2] [url3] and so on.");

		getCommand("imap").setUsage(ChatColor.BLUE + "Usage: /imap");
		getCommand("imgmap").setUsage(ChatColor.BLUE + "Usage: /imgmap");

		getCommand("restoremap").setUsage(ChatColor.BLUE + "Usage: /restoremap");
		getCommand("rmap").setUsage(ChatColor.BLUE + "Usage: /rmap");

		//Setting executors and permission errors.
		for(String cmd : cmds){
			//You get a cookie if you can guess the type of person I'm referring to.
			//Original idea for perms error: "[ImgMap] I-It's not like I can't let you do that or anything! I-I'm just trying to protect you!"
			getCommand(cmd).setPermissionMessage(ChatColor.RED + "[ImgMap] You cannot use this command for permission reasons!");
			getCommand(cmd).setExecutor(cc);
		}

		// This is rather suicidel.
		new Thread(){
			@Override
			public void run(){
				try {
					DataUtils.initialize();
					
					for(String s : Files.readLines(getList(), Charset.defaultCharset())){
						String url = s.substring(s.indexOf(":")+1, s.length());
						short id = Short.valueOf(s.substring(0, s.indexOf(":")));
						
						MapView viewport = Bukkit.getServer().getMap(id);
						
						for(MapRenderer mr : viewport.getRenderers()){
							viewport.removeRenderer(mr);
						}

						viewport.addRenderer(new ImageRenderer(url));
					}

					for(File f : new File(ImgMap.getPlugin().getDataFolder().getAbsolutePath() + "/SlideshowData/").listFiles()){
						short id = Short.valueOf(f.getName().substring(0, f.getName().indexOf(".")));
						MapView viewport = Bukkit.getServer().getMap(id);

						for(MapRenderer mr : viewport.getRenderers()){
							viewport.removeRenderer(mr);
						}

						List<String> lines = Files.readLines(f, Charset.defaultCharset());
						float waitTime = Float.valueOf(lines.remove(0));
						String[] urls = new String[lines.size()];
						lines.toArray(urls);

						viewport.addRenderer(new SlideshowRenderer(urls, waitTime));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
		System.out.println(Bukkit.getServer().getVersion());
		
	}

	public void onDisable(){
		SlideshowThread[] activeThreads = new SlideshowThread[group.activeCount()];
		group.enumerate(activeThreads);
		for(SlideshowThread t : activeThreads){
			t.stopThread();
		}
	}

	/**
	 * Returns the plugin defined by "pl".
	 * @return The plugin.
	 */
	public static ImgMap getPlugin(){
		return pl;
	}

	/**
	 * Gets the file and returns a File object.
	 * @return The Maps.list file.
	 */
	public static File getList(){
		return new File(ImgMap.getPlugin().getDataFolder(), "Maps.list");
	}

	public static File getSlideshowFile(int id){
		return new File(ImgMap.getPlugin().getDataFolder().getAbsolutePath() + "/SlideshowData/", String.valueOf(id + ".slideshow"));
	}

	public static MapThreadGroup getThreadGroup(){
		return group;
	}
}
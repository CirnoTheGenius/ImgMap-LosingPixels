package com.tenko;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.annotations.Beta;
import com.tenko.cmdexe.CommanderCirno;
import com.tenko.threading.MapThreadGroup;
import com.tenko.threading.PersistencyThread;
import com.tenko.threading.SlideshowThread;

/**
 * ImgMap - Maps become picture frames!
 * @author Tsunko
 * @version 2 Beta (Wow, I haven't changed that in a while.)
 */
@Beta
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
	 * Let's start it!
	 * Get chance and luck!
	 */
	@Override
	public void onEnable(){
		String[] cmds = new String[]{
				"map",
				"images",
				"smap",
				"imap", "imgmap",
				"restoremap", "rmap",
				"ani"
		};

		pl = this;

		//Usage
		getCommand("map").setUsage(ChatColor.BLUE + "Usage: /map <url|file>");
		getCommand("images").setUsage(ChatColor.BLUE + "Usage: /images");
		getCommand("smap").setUsage(ChatColor.BLUE + "Usage: /smap <time> <url1|file> [url2|file] [url3|file] and so on.");
		getCommand("ani").setUsage(ChatColor.BLUE + "Usage: /ani <url|file>");

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
		
		PersistencyThread pt = new PersistencyThread();
		pt.start();
	}

	@Override
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

	/**
	 * Get slideshow by ID.
	 * @param id - The Map ID.
	 * @return A file for the Slideshow.
	 */
	public static File getSlideshowFile(int id){
		return new File(ImgMap.getPlugin().getDataFolder().getAbsolutePath() + "/SlideshowData/", String.valueOf(id + ".slideshow"));
	}

	/**
	 * Returns the custom threadgroup.
	 * @return MapThreadGroup.
	 */
	public static MapThreadGroup getThreadGroup(){
		return group;
	}
}

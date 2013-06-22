package com.tenko.Gunvarrel;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;

import com.tenko.ImgMap;
import com.tenko.Gunvarrel.Function;

/**
 * 
 * Project Gunvarrel - A new base for my plugins.
 * Feel free to implement, I can care less. Just make
 * sure the package name is "Gunvarrel" so I can get 
 * some credit (as if this will actually work)
 * 
 * If you've watched Robotics;Notes, you understand
 * the reference. Duhuhu.
 * @author Tsunko
 *
 */
public class Gunvarrel {
	
	private final ArrayList<Function> parts;
	
	public Gunvarrel(){
		parts = new ArrayList<Function>();
	}
	
	public boolean add(Class<? extends Function> f, String... commands){
		try {
			Function newFunction = f.newInstance();
			parts.add(newFunction);
			
			for(String cmd : commands){
				PluginCommand com = ImgMap.getPlugin().getCommand(cmd);
				com.setExecutor(newFunction);
				com.setPermissionMessage(ChatColor.RED + "[ImgMap] You cannot use this command for permission reasons!");
				com.setPermission("imgmap.command."+cmd);
			}
			
			return true;
		} catch (InstantiationException e){
			e.printStackTrace();
		} catch (IllegalAccessException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean remove(Class<? extends Function> f){
		for(Function funky : parts){
			if(funky.getClass().equals(f)){
				parts.remove(f);
			}
		}
		return false;
	}
	
}

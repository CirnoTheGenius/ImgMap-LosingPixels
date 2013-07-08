package com.tenko.Gunvarrel;

import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;

import com.tenko.rendering.SlideshowRenderer;

public abstract class Function implements CommandExecutor {
	
	public enum Result {
		SUCCESS, INFO, FAILURE
	}
	
	private MapRenderer lastRenderer;
	
	@Override
	public abstract boolean onCommand(CommandSender cs, Command c, String l, String[] args);
	
	public void notifySender(CommandSender cs, String message, Result r){
		switch(r){
			case SUCCESS:{
				cs.sendMessage(ChatColor.BLUE + "[ImgMap] " + message);
				return;
			}
			
			case INFO:{
				cs.sendMessage(ChatColor.GOLD + "[ImgMap] " + message);
				return;
			}
			
			case FAILURE:{
				cs.sendMessage(ChatColor.RED + "[ImgMap] " + message);
				return;
			}
			
			default:
				cs.sendMessage(ChatColor.BLUE + "[ImgMap] " + message);
				return;
		}
	}
	
	public void setRenderer(MapView view, MapRenderer render){
		Iterator<MapRenderer> renderers = view.getRenderers().iterator();
		while(renderers.hasNext()){
			MapRenderer mr = renderers.next();
			if(mr instanceof SlideshowRenderer){
				((SlideshowRenderer)mr).thread.stopThread();
			}
			view.removeRenderer(mr);
		}
		
		view.setScale(Scale.FARTHEST);
		view.addRenderer(render);
		
		this.lastRenderer = render;
	}
	
	public MapRenderer getLastRenderer(){
		return lastRenderer;
	}
}

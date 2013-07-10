package com.tenko.test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.map.MapRenderer;

import com.google.common.io.Files;
import com.tenko.utils.MapDataUtils;

public class MapListener implements Listener {
	
	private static List<String> data;
	
	public MapListener(){
		try {
			data = Files.readLines(MapDataUtils.getList(), Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateList(){
		try {
			data = Files.readLines(MapDataUtils.getList(), Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void mapInit(org.bukkit.event.player.PlayerItemHeldEvent e){
		e.get
		for(String line : data){
			if(line.startsWith(e.getMap().getId() + ":")){
				for(MapRenderer mr : e.getMap().getRenderers()){
					e.getMap().removeRenderer(mr);
				}
				break;
			}
		}
	}
	
}

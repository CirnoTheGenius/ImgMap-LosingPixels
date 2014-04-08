package net.yukkuricraft.tenko.render;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class DummyRenderer extends MapRenderer {

	@Override
	public void render(MapView view, MapCanvas canvas, Player plyr){
		return; //It's a dummy!
	}

}

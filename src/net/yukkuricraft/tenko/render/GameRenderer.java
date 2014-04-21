package net.yukkuricraft.tenko.render;

import net.yukkuricraft.tenko.oldemu.CPU;
import net.yukkuricraft.tenko.oldemu.GUI;

import org.bukkit.entity.Player;
import org.bukkit.map.*;

public class GameRenderer extends MapRenderer {
	
	private boolean hasRendered = false;
	private CPU cpu;
	
	@Override
	public void render(final MapView view, MapCanvas canvas, final Player plyr) {
		if (this.hasRendered) {
			return;
		}
		
		this.hasRendered = true;
		
		Thread thread = new Thread(new Runnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				GameRenderer.this.cpu = new CPU("./red.gb", new GUI(), plyr, view.getId());
				GameRenderer.this.cpu.start();
			}
			
		});
		thread.start();
	}
	
	public CPU getCPU() {
		return this.cpu;
	}
}

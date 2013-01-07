package Rendering;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import Lib.IcicalLib;
import cirno.Nineball;

public class ImgRenderer extends MapRenderer {
	String URL;
	boolean flagRender = false;
	Nineball cirno;

	public ImgRenderer(String url, Nineball cirno){
		this.URL = url;
		this.cirno = cirno;
	}

	public void render(MapView map, final MapCanvas canvas, final Player player){
		if(this.URL != null && !this.flagRender){
			this.cirno.getServer().getScheduler().scheduleSyncDelayedTask(this.cirno, new Runnable(){
				public void run(){
					try {
						canvas.drawImage(0, 0, IcicalLib.resizeImage(ImgRenderer.this.URL));
					} catch (Exception e) {
						player.sendMessage(ChatColor.RED + "[ImgMap] Failed to read image!");
						e.printStackTrace();
					}
				}
			});
			this.flagRender = true;
		}
	}


}
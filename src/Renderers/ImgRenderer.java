package Renderers;

import java.net.URL;
import javax.imageio.ImageIO;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import cirno.Nineball;

public class ImgRenderer extends MapRenderer{
	
	private final String URL;
	private final Nineball cirno;
	
	boolean flagRender = false;
	
	public ImgRenderer(String url, Nineball cirno){
		URL = url;
		this.cirno = cirno;
	}

	@Override
	public void render(MapView map, final MapCanvas canvas, final Player player) {
		if(URL != null && flagRender == false){
			cirno.getServer().getScheduler().scheduleAsyncDelayedTask(cirno, new Runnable() {
				public void run(){
					try {
						canvas.drawImage(0, 0, Lib.ImageManipulation.resizeImage(ImageIO.read(new URL(URL))));
					} catch(Exception e){
						player.sendMessage(ChatColor.RED + "[ImgMap] Failed to read image!");
						e.printStackTrace();
					}
				}
			});
			flagRender = true;
		}
	}
}


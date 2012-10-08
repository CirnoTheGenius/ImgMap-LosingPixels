package cirno;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class ImgRenderer extends MapRenderer{
	String URL;
	boolean flagRender = false;
	Nineball cirno;

	public ImgRenderer(String url, Nineball cirno){
		URL = url;
		this.cirno = cirno;
	}

	@Override
	public void render(MapView map, final MapCanvas canvas, final Player player) {
		// Check if url is nothing and if the image is already rendered
		// Note that this flagRender var is local , so it does not affect other images
		if(URL != null && flagRender == false){
			cirno.getServer().getScheduler().scheduleAsyncDelayedTask(cirno, new Runnable() {
				public void run() {
					try{
						canvas.drawImage(0, 0, resizeImage(ImageIO.read(new URL(URL))));
					} catch(Exception e){
						player.sendMessage(ChatColor.RED + "[ImgMap] Failed to read image!");
						e.printStackTrace();
					}
				}
			});
			flagRender = true;
		}
	}


	public Image resizeImage(BufferedImage originalImage){
		BufferedImage resizedImage = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, 128, 128, null);
		g.finalize();
		g.dispose();
		resizedImage.flush();
		return resizedImage;
	}
}


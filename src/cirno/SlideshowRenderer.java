package cirno;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class SlideshowRenderer extends MapRenderer {
	ArrayList<URL> URLs;
	boolean flagRender = false;
	TimerThread stopWatch;

	public SlideshowRenderer(ArrayList<URL> url, int waitTime){
		URLs = url;
		stopWatch = new TimerThread(waitTime);
	}

	@Override
	public void render(MapView map, final MapCanvas canvas, final Player player) {
		try{
			// Check if url is nothing and if the image is already rendered
			// Note that this flagRender var is local , so it does not affect other images
			for(int i=0; i < URLs.size() + 1; i++){
				if(!URLs.isEmpty() && flagRender == false){
					flagRender = true;
					try {
						/*
						Experimental code. Probably won't run properly.
						Will probably freeze the main thread.
						*/
						canvas.drawImage(0, 0, resizeImage(ImageIO.read(URLs.get(i))));
						boolean waited = stopWatch.time(stopWatch.waitTime).finished;
						while(!waited){}
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
		} catch(Exception e){
			e.printStackTrace();
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


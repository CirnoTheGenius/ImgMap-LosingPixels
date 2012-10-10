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
	final ArrayList<URL> URLs;
	boolean flagRender = false;
	Nineball cirno;
	final int waitTime;

	public SlideshowRenderer(ArrayList<URL> url, int waitTime, Nineball cirno){
		URLs = url;
		this.waitTime = waitTime;
		this.cirno = cirno;
	}

	@Override
	public void render(MapView map, final MapCanvas canvas, final Player player) {
		try{
			if(!URLs.isEmpty() && flagRender == false){
				new CirnoThread(cirno.tg, "Slideshow Renderer"){
					public void run(){
						if(running) try {
							for(int i=0; i < URLs.size(); i++){
								canvas.drawImage(0, 0, resizeImage(ImageIO.read(URLs.get(i))));
								sleep(waitTime*1000);
							}
							this.run();
							flagRender = true;
						} catch (Throwable e) {
							e.printStackTrace();
						} else {
							this.stopRunning();
						}
					}
				}.start();
			}
			flagRender = true;
		} catch(Exception e){}
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


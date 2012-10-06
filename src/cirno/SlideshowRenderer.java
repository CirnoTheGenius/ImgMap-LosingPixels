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
	final TimerThread stopWatch;
	Nineball cirno;
	final int waitTime;
	boolean running = false;

	@SuppressWarnings("deprecation")
	public SlideshowRenderer(ArrayList<URL> url, int waitTime, Nineball cirno){
		URLs = url;
		stopWatch = new TimerThread(waitTime);
		this.waitTime = waitTime;
		stopWatch.stop();
		this.cirno = cirno;
		running = true;
	}

	@Override
	public void render(MapView map, final MapCanvas canvas, final Player player) {
		try{
			// Check if url is nothing and if the image is already rendered
			// Note that this flagRender var is local , so it does not affect other images
			if(!URLs.isEmpty() && flagRender == false){
				new Thread(cirno.tg, "Slideshow Renderer"){
					@SuppressWarnings("deprecation")
					public void run(){
						if(running){
							try {
								System.out.print(URLs.size());
								/*
									Experimental code. Probably won't run properly.
									Will probably freeze the main thread.
								 */
								System.out.print("[Debugging ImgMap] Drawing maps");

								for(int i=0; i < URLs.size(); i++){
									System.out.print(URLs.get(i).toExternalForm());
									canvas.drawImage(0, 0, resizeImage(ImageIO.read(URLs.get(i))));
									sleep(waitTime*1000);
								}
								//System.out.print("[Debugging ImgMap] Starting stopWatch Thread");
								System.out.print("[Debugging ImgMap] Finished and restarting loop.");
								this.run();
								flagRender = true;
							} catch (Throwable e) {
								e.printStackTrace();
							}
						} else {
							this.stop();
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


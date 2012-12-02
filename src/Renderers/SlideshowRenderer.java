package Renderers;

import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import Threading.CirnoThread;
import cirno.Nineball;

public class SlideshowRenderer extends MapRenderer {
	
	private final ArrayList<URL> URLs;
	private boolean flagRender = false;
	private final Nineball cirno;
	private final int waitTime;
	
	public SlideshowRenderer(ArrayList<URL> url, int waitTime, Nineball cirno){
		URLs = url;
		this.waitTime = waitTime;
		this.cirno = cirno;
	}

	@Override
	public void render(MapView map, final MapCanvas canvas, final Player player) {
		try{
			if(!URLs.isEmpty() && flagRender == false){
				CirnoThread ct = new CirnoThread(cirno.tg, "Slideshow Renderer"){
					public void run(){
						if(running) try {
							for(int i=0; i < URLs.size(); i++){
								canvas.drawImage(0, 0, Lib.ImageManipulation.resizeImage(ImageIO.read(URLs.get(i))));
								sleep(waitTime*1000);
							}
							this.run();
							flagRender = true;
						} catch (Exception e) {
							e.printStackTrace();
						} else {
							this.stopRunning();
						}
					}
				};
				cirno.tg.addToList(ct);
				ct.start();
			}
			flagRender = true;
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}


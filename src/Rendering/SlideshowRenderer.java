package Rendering;

import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import Lib.IcicalLib;
import Threading.CirnoThread;
import cirno.Nineball;

public class SlideshowRenderer extends MapRenderer {
	private final ArrayList<String> URLs;
	private boolean flagRender = false;
	private final int waitTime;

	public SlideshowRenderer(ArrayList<String> url, int waitTime, Nineball cirno){
		this.URLs = url;
		this.waitTime = waitTime;
	}

	public void render(MapView map, final MapCanvas canvas, Player player){
		try {
			if(!this.URLs.isEmpty() && !this.flagRender){
				flagRender = true;
				new CirnoThread("Slideshow Renderer"){
					public void run(){
						if(this.running) try {
							for (int i = 0; i < SlideshowRenderer.this.URLs.size(); i++){
								canvas.drawImage(0, 0, IcicalLib.resizeImage(URLs.get(i)));
								sleep(SlideshowRenderer.this.waitTime * 1000);
							}
							run();
						} catch (Throwable e) {
							e.printStackTrace();
						} else {
							stopRunning();
						}
					}
				}.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
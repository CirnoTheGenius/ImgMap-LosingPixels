package net.yukkuricraft.tenko.render;

import java.awt.image.BufferedImage;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Stack;

import net.yukkuricraft.tenko.ImgMap;
import net.yukkuricraft.tenko.threading.AbstractSafeRunnable;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class SlideshowRenderer extends MapRenderer {
	
	private boolean hasRendered = false;
	private Thread slideshowThread;
	private SoftReference<List<BufferedImage>> images;
	private int length;
	
	public SlideshowRenderer(int length, List<BufferedImage> images) {
		this.images = new SoftReference<>(images);
		this.length = length;
	}
	
	@Override
	public void render(MapView view, MapCanvas canvas, Player player){
		if(!this.hasRendered && this.slideshowThread == null){
			if(this.images.get() != null){
				this.slideshowThread = new Thread(new SlideshowRunnable(canvas, this.images.get(), this.length));
			}else{
				ImgMap.logMessage("Race Condition: The SoftReference to images was garbage collected too early!");
			}
			this.hasRendered = true;
		}
	}
	
	private class SlideshowRunnable extends AbstractSafeRunnable {
		
		private Stack<BufferedImage> images;
		private int lengthInMilli;
		private MapCanvas canvas;
		
		public SlideshowRunnable(MapCanvas canvas, List<BufferedImage> images, int timeLength) {
			images = new Stack<>();
			images.addAll(images);
			this.canvas = canvas;
			this.lengthInMilli = timeLength;
		}
		
		@Override
		public void running(){
			BufferedImage image = this.images.pop();
			this.canvas.drawImage(0, 0, image);
			this.images.push(image);
			try{
				Thread.sleep(this.lengthInMilli);
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
}

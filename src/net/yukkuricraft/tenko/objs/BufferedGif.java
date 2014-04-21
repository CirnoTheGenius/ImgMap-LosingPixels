package net.yukkuricraft.tenko.objs;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;

import net.yukkuricraft.tenko.render.RenderUtils;
import net.yukkuricraft.tenko.render.SunReader;

import org.bukkit.map.MapPalette;

public class BufferedGif {
	
	private ImageReader reader;
	private byte[][][] frames;
	private URL url;
	private int delayBetweenFrames = -1;
	private int frameCount = -1;
	private boolean useSun;
	
	public BufferedGif(URL toDraw, boolean useSun) {
		this.url = toDraw;
		this.useSun = useSun;
	}
	
	@SuppressWarnings("deprecation")
	public void bufferData() throws IOException, InterruptedException{
		InputStream io = this.url.openStream();
		ImageInputStream stream = ImageIO.createImageInputStream(io);
		
		// Fixed vs Cached...
		// ExecutorService executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		ExecutorService executors = Executors.newCachedThreadPool();
		
		this.reader = this.useSun ? SunReader.setupImageReader() : ImageIO.getImageReadersBySuffix("GIF").next();
		this.reader.setInput(stream, false, false);
		this.frameCount = this.reader.getNumImages(true);
		this.frames = new byte[this.frameCount][128][128];
		
		for(int index = 0; index < this.frames.length; index++){
			final BufferedImage image = BufferedGif.this.reader.read(index);
			
			// Crude way of working around the whole "variable must be final" thing.
			final int indexCopy = index;
			
			executors.execute(new Runnable() {
				@Override
				public void run(){
					// inb4fails
					RenderUtils.resizeImageNoEditing(image);
					
					for(int x = 0; x < 128; x++){
						for(int y = 0; y < 128; y++){
							try{
								BufferedGif.this.frames[indexCopy][x][y] = MapPalette.matchColor(new Color(image.getRGB(x, y)));
							}catch (ArrayIndexOutOfBoundsException e){
								BufferedGif.this.frames[indexCopy][x][y] = MapPalette.TRANSPARENT;
							}
						}
					}
				}
			});
		}
		
		executors.shutdown();
		executors.awaitTermination(1, TimeUnit.HOURS); // Overly stupid to have an hour.
		this.getMilliDelay();
		stream.close();
		io.close();
	}
	
	public byte[][][] getFrames(){
		return this.frames;
	}
	
	public int getMilliDelay() throws IOException{
		if(this.delayBetweenFrames == -1){
			IIOMetadata meta = this.reader.getImageMetadata(0);
			IIOMetadataNode root = (IIOMetadataNode) meta.getAsTree(meta.getNativeMetadataFormatName());
			for(int index = 0; index < root.getLength(); index++){
				if(root.item(index).getNodeName().compareToIgnoreCase("GraphicControlExtension") == 0){
					return this.delayBetweenFrames = Integer.parseInt(((IIOMetadataNode) root.item(index)).getAttribute("delayTime"));
				}
			}
		}
		return this.delayBetweenFrames;
	}
}

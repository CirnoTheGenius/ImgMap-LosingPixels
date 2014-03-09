package net.yukkuricraft.tenko.objs;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

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
	
	public BufferedGif(String url, boolean useSun) {
		try{
			this.url = new URL(url);
			this.useSun = useSun;
		}catch (MalformedURLException e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public BufferedGif(File file, boolean useSun) {
		try{
			this.url = file.toURL();
			this.useSun = useSun;
		}catch (MalformedURLException e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void bufferData() throws IOException{
		InputStream io = this.url.openStream();
		ImageInputStream stream = ImageIO.createImageInputStream(io);
		
		this.reader = this.useSun ? SunReader.setupImageReader() : ImageIO.getImageReadersBySuffix("GIF").next();
		this.reader.setInput(stream);
		this.frameCount = this.reader.getNumImages(true);
		this.frames = new byte[this.frameCount][128][128];
		
		for(int index = 0; index < this.frameCount; index++){
			BufferedImage image = this.reader.read(index);
			RenderUtils.resizeImage(image);
			
			for(int x = 0; x < 128; x++){
				for(int y = 0; y < 128; y++){
					try{
						this.frames[index][x][y] = MapPalette.matchColor(new Color(image.getRGB(x, y)));
					}catch (ArrayIndexOutOfBoundsException e){
						this.frames[index][x][y] = MapPalette.TRANSPARENT;
						// ImgMap.logMessage("Failed to match byte color for coords x=" + x + " y=" + y + "! Defaulted to transparent!");
					}
				}
			}
		}
		
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

package net.yukkuricraft.tenko.render;

import java.io.IOException;

import javax.imageio.ImageReader;

import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.gif.GIFImageReaderSpi;

public class SunReader {
	
	public static ImageReader setupImageReader(){
		return new GIFImageReader(new GIFImageReaderSpi());
	}
	
	public static byte[][][] getFrames(GifRenderer gifRenderer, String url) throws IOException{
		GIFImageReader reader = new GIFImageReader(new GIFImageReaderSpi());
		return RenderUtils.getGifColors(reader);
	}
	
}

package net.yukkuricraft.tenko.render;

import javax.imageio.ImageReader;

import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.gif.GIFImageReaderSpi;

public class SunReader {
	
	public static ImageReader setupImageReader(){
		return new GIFImageReader(new GIFImageReaderSpi());
	}
	
}

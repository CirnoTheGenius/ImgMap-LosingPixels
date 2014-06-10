package net.yukkuricraft.tenko.objs;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import net.yukkuricraft.tenko.render.RenderUtils;

import org.bukkit.map.MapPalette;

import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.gif.GIFImageReaderSpi;

//Listening to Freedom Dive...
//zxzxzxzxzxzxzxzxzxz
public class BufferedGif {
	
	// byte[frame][x][y]
	private byte[][][] frames;
	private final URL url;
	private final Color[] colors = stealColors();
	
	public BufferedGif(URL url) {
		this.url = url;
	}
	
	public byte[][][] buffer(){
		try{
			ExecutorService executors = Executors.newCachedThreadPool();
			
			final ImageReader ir = new GIFImageReader(new GIFImageReaderSpi());
			InputStream s1 = url.openStream();
			ImageInputStream stream = ImageIO.createImageInputStream(s1);
			ir.setInput(stream);
			frames = new byte[ir.getNumImages(true)][128][128];
			
			for(int i = 0; i < frames.length; i++){
				final int iCopy = i;
				
				executors.execute(new Runnable() {
					
					@Override
					public void run(){
						try{
							BufferedImage frame = ir.read(iCopy);
							RenderUtils.resizeImageNoEditing(frame);
							byte[][] mcFrame = frames[iCopy];
							
							for(int x = 0; x < 128; x++){
								for(int y = 0; y < 128; y++){
									try{
										mcFrame[x][y] = matchColor(frame.getRGB(x, y));
									}catch (ArrayIndexOutOfBoundsException e){
										try{
											mcFrame[x][y] = frames[iCopy - 1][x][y];
										}catch (ArrayIndexOutOfBoundsException e2){
											mcFrame[x][y] = 0;
										}
									}
								}
							}
						}catch (IOException e){
							e.printStackTrace();
						}
					}
					
				});
			}
			
			executors.shutdown();
			executors.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // Have fun waiting 292.277 years!
			stream.flush();
			stream.close();
			s1.close();
		}catch (Throwable e){
			e.printStackTrace();
		}
		
		return frames;
	}
	
	public void checkNullFields(Object obj) throws Throwable{
		checkNullFields(obj, null);
	}
	
	public void checkNullFields(Object obj, Object parent) throws Throwable{
		List<Field> fields = new ArrayList<Field>();
		
		for(Field field : obj.getClass().getFields()){
			fields.add(field);
		}
		
		for(Field field : obj.getClass().getDeclaredFields()){
			if(!fields.contains(field)){
				fields.add(field);
			}
		}
		
		for(Field field : fields){
			Object fget = field.get(obj);
			if(fget == null){
				System.out.println("Field " + field.getName() + " for object " + obj + (parent == null ? " with parent " + parent : "") + " was null.");
				continue;
			}
			
			if(!fget.getClass().isPrimitive()){
				checkNullFields(fget, obj);
			}
		}
	}
	
	// A bit confusing, but c1 is the int representation of the Color from frame.getRGB()
	// Why did I make such a function?
	// Because I hate how the old one created a Color object that was thrown away at the end.
	private double getDistance(int c1, Color c2){
		c1 = 0xff000000 | c1;
		double rmean = ((c1 >> 16) + c2.getRed()) / 2.0;
		double r = ((c1 >> 16) & 0xFF) - c2.getRed();
		double g = ((c1 >> 8) & 0xFF) - c2.getGreen();
		int b = ((c1 >> 0) & 0xFF) - c2.getBlue();
		double weightR = 2 + rmean / 256.0;
		double weightG = 4.0;
		double weightB = 2 + (255 - rmean) / 256.0;
		return weightR * r * r + weightG * g * g + weightB * b * b;
	}
	
	public byte matchColor(int c1){
		double best = -1;
		int index = 0;
		for(int i = 4; i < colors.length; i++){
			double calc = getDistance(c1, colors[i]);
			if(best == -1 || calc < best){
				best = calc;
				index = i;
			}
		}
		
		return (byte) (index < 128 ? index : -129 + (index - 127));
	}
	
	// Steal the colors from MapPalette instead of copy-pasting it.
	public Color[] stealColors(){
		try{
			Field field = MapPalette.class.getDeclaredField("colors");
			field.setAccessible(true);
			return (Color[]) field.get(null);
		}catch (Throwable e){
			e.printStackTrace();
			return null;
		}
	}
}

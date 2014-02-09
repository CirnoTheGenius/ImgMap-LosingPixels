package net.yukkuricraft.tenko.render;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageReader;

import net.minecraft.server.v1_7_R1.ItemWorldMap;
import net.yukkuricraft.tenko.ImgMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_7_R1.map.CraftMapRenderer;
import org.bukkit.craftbukkit.v1_7_R1.map.CraftMapView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class RenderUtils {
	
	public static void resizeImage(BufferedImage image){
		Graphics2D resizer = image.createGraphics();
		// ??? Article said that it "increased image quality". Read: rip heap
		resizer.setComposite(AlphaComposite.Src);
		resizer.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		resizer.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		resizer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		resizer.drawImage(image, 0, 0, 128, 128, null);
		resizer.dispose();
	}
	
	@SuppressWarnings("deprecation")
	public static byte[][][] getGifColors(ImageReader reader) throws IOException{
		int frameCount = reader.getNumImages(true);
		byte[][][] bytes = new byte[frameCount][128][128];
		BufferedImage[] images = new BufferedImage[frameCount];
		
		for(int i = 0; i < frameCount; i++){
			BufferedImage image = reader.read(i);
			RenderUtils.resizeImage(image);
			images[i] = image;
		}
		
		for(int index = 0; index < frameCount; index++){
			BufferedImage image = images[index];
			
			for(int x = 0; x < 128; x++){
				for(int y = 0; y < 128; y++){
					try{
						Color color = new Color(image.getRGB(x, y));
						bytes[index][x][y] = MapPalette.matchColor(color);
					}catch (ArrayIndexOutOfBoundsException e){
						bytes[index][x][y] = MapPalette.TRANSPARENT;
						ImgMap.logMessage("Failed to match byte color for coords x=" + x + " y=" + y + "! Defaulted to transparent!");
					}
				}
			}
		}
		
		return bytes;
	}
	
	@SuppressWarnings("deprecation")
	public static byte[][] getColors(BufferedImage image) throws IOException{
		byte[][] bytes = new byte[128][128];
		
		RenderUtils.resizeImage(image);
		
		for(int x = 0; x < 128; x++){
			for(int y = 0; y < 128; y++){
				try{
					Color color = new Color(image.getRGB(x, y));
					bytes[x][y] = MapPalette.matchColor(color);
				}catch (ArrayIndexOutOfBoundsException e){
					bytes[x][y] = MapPalette.TRANSPARENT;
					ImgMap.logMessage("Failed to match byte color for coords x=" + x + " y=" + y + "! Defaulted to transparent!");
				}
			}
		}
		
		return bytes;
	}
	
	public static void removeRenderers(MapView view){
		if(view == null){
			return;
		}
		
		Iterator<MapRenderer> iter = view.getRenderers().iterator();
		while(iter.hasNext()){
			MapRenderer mr = iter.next();
			view.removeRenderer(mr);
			
			if(mr instanceof GifRenderer){
				((GifRenderer)mr).stopRendering();
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public static MapRenderer getRendererForWorld(ItemStack bukkitStack, World world){
		net.minecraft.server.v1_7_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(bukkitStack);
		CraftMapView view = (CraftMapView)Bukkit.getMap(bukkitStack.getDurability());
		MapRenderer worldRenderer = new CraftMapRenderer(view, ((ItemWorldMap)nmsStack.getItem()).getSavedMap(nmsStack, ((CraftWorld)world).getHandle()));
		return worldRenderer;
	}
	
	public static void scheduleMimeCheck(String url){
		
	}
	
}

package net.yukkuricraft.tenko.render;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import net.minecraft.server.v1_7_R3.ItemWorldMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_7_R3.map.CraftMapRenderer;
import org.bukkit.craftbukkit.v1_7_R3.map.CraftMapView;
import org.bukkit.inventory.ItemStack;
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
	
	public static void resizeImageNoEditing(BufferedImage image){
		Graphics2D resizer = image.createGraphics();
		resizer.drawImage(image, 0, 0, 128, 128, null);
		resizer.dispose();
	}
	
	public static void removeRenderers(MapView view){
		if(view == null){
			return;
		}
		
		Iterator<MapRenderer> iter = view.getRenderers().iterator();
		while(iter.hasNext()){
			MapRenderer mr = iter.next();
			view.removeRenderer(mr);
			
			if(mr instanceof StoppableRenderer){
				((StoppableRenderer) mr).stopRendering();
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public static MapRenderer getRendererForWorld(ItemStack bukkitStack, World world){
		net.minecraft.server.v1_7_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(bukkitStack);
		CraftMapView view = (CraftMapView) Bukkit.getMap(bukkitStack.getDurability());
		MapRenderer worldRenderer = new CraftMapRenderer(view, ((ItemWorldMap) nmsStack.getItem()).getSavedMap(nmsStack, ((CraftWorld) world).getHandle()));
		return worldRenderer;
	}
	
}

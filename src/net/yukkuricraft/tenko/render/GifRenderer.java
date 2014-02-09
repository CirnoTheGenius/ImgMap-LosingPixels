package net.yukkuricraft.tenko.render;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;

import net.minecraft.server.v1_7_R1.PacketPlayOutMap;
import net.minecraft.server.v1_7_R1.PlayerConnection;
import net.yukkuricraft.tenko.ImgMap;
import net.yukkuricraft.tenko.threading.AbstractSafeRunnable;

import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class GifRenderer extends MapRenderer {
	
	private boolean useSunInternalGifReader = false;
	private PacketPlayOutMap[][] cache;
	private int delayMilli;
	private boolean hasRendered;
	private AbstractSafeRunnable renderRunnable;
	private final int tolerance = 10;
	private boolean ready = false;
	
	// Experimental packet caching.
	// The world is waiting~
	public GifRenderer(String str, short id) throws IOException {
		try{
			Class.forName("com.sun.imageio.plugins.gif.GIFImageReader");
			Class.forName("com.sun.imageio.plugins.gif.GIFImageReaderSpi");
			ImgMap.logMessage("Using Sun GIF reader.");
			this.useSunInternalGifReader = true;
		}catch (ClassNotFoundException e){
			ImgMap.logMessage("Using homemade GIF reader.");
		}
		
		this.setupCache(str, id);
	}
	
	public void setupCache(String str, short id) throws IOException{
		Thread cacheThread = new Thread(new CachingRunnable(id, str));
		cacheThread.start();
	}
	
	// 3d arrays... ooo... aaaa...
	public byte[][][] getImages(String url) throws IOException{
		ImageReader reader;
		InputStream is = null;
		ImageInputStream stream = null;
		
		if(this.useSunInternalGifReader){
			// NOTE: This is likely unstable, but maybe faster.
			// Due to the nature of com.sun.* classes, this may
			// not exist on all platforms.
			reader = SunReader.setupImageReader();
		}else{
			reader = ImageIO.getImageReadersBySuffix("GIF").next();
		}
		
		try{
			is = new URL(url).openStream();
			stream = ImageIO.createImageInputStream(is);
			reader.setInput(stream);
			this.setDelay(reader);
			return RenderUtils.getGifColors(reader);
		}catch (IOException e){
			e.printStackTrace();
			return new byte[0][0][0];
		}finally{
			try{
				stream.close();
				is.close();
			}catch (NullPointerException e){
				ImgMap.logMessage("Failed to nab the images from " + url + "!");
			}
		}
	}
	
	public void setDelay(ImageReader reader) throws IOException{
		IIOMetadata imageMetaData = reader.getImageMetadata(0);
		String metaFormatName = imageMetaData.getNativeMetadataFormatName();
		IIOMetadataNode root = (IIOMetadataNode)imageMetaData.getAsTree(metaFormatName);
		IIOMetadataNode graphicsControlExtensionNode = GifRenderer.getNode(root, "GraphicControlExtension");
		this.delayMilli = Integer.valueOf(graphicsControlExtensionNode.getAttribute("delayTime"));
	}
	
	private static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName){
		int nNodes = rootNode.getLength();
		for (int i = 0; i < nNodes; i++){
			if(rootNode.item(i).getNodeName().equalsIgnoreCase(nodeName)){
				return (IIOMetadataNode)rootNode.item(i);
			}
		}
		
		IIOMetadataNode node = new IIOMetadataNode(nodeName);
		rootNode.appendChild(node);
		return node;
	}
	
	@Override
	public void render(MapView view, MapCanvas canvas, Player player){
		if(this.hasRendered || !this.ready){
			return;
		}
		
		Thread thread = new Thread(this.renderRunnable = new AnimationRunnable(player));
		thread.start();
		this.hasRendered = true;
	}
	
	public void stopRendering(){
		if(this.renderRunnable == null){
			System.out.println("Called to stop renderer on " + this.toString() + " but returned had a null renderRunnable!");
		}else{
			// Safely stops it.
			this.renderRunnable.stopRunning();
			System.out.println("Safely stopped runnable.");
			if(this.renderRunnable.isRunning()){
				System.out.println("renderRunnable is still running!");
			}
		}
	}
	
	// Spinning thing: /drawanimatedimage http://www.thisiscolossal.com/wp-content/uploads/2013/01/4.gif
	// Transforming gel: /drawanimatedimage http://i.imgur.com/vzzoyGs.gif
	private class AnimationRunnable extends AbstractSafeRunnable {
		
		// Likely only shaving off a couple of nano seconds.
		private PlayerConnection connection;
		
		public AnimationRunnable(Player plyr) {
			this.connection = ((CraftPlayer)plyr).getHandle().playerConnection;
		}
		
		@Override
		public void running(){
			for (PacketPlayOutMap[] element : GifRenderer.this.cache){
				if(this.connection.isDisconnected()){
					this.stopRunning();
					// Release the connection.
					this.connection = null;
				}
				
				for (PacketPlayOutMap packet : element){
					if(packet != null){
						this.connection.sendPacket(packet);
					}
				}
				
				try{
					Thread.sleep(GifRenderer.this.delayMilli * 10);
				}catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	private class CachingRunnable implements Runnable {
		
		private final String str;
		private short id;
		
		public CachingRunnable(short id, String str) {
			this.str = str;
		}
		
		@Override
		public void run(){
			byte[][] last = null;
			byte[][][] allImageBytes = null;
			try{
				allImageBytes = GifRenderer.this.getImages(this.str);
			}catch (IOException e){
				return;
			}
			
			// Supposed to be 128 packets.
			GifRenderer.this.cache = new PacketPlayOutMap[allImageBytes.length][128];
			
			// Loop through all images.
			for (int index = 0; index < GifRenderer.this.cache.length; index++){
				// The current 128x128 image.
				byte[][] current = allImageBytes[index];
				
				// At index 0, last would be null.
				if(index == 0){
					last = current;
					continue;
				}
				
				// Begin row.
				for (int x = 0; x < 128; x++){
					byte[] packetBytes = null;
					boolean changed = false;
					boolean floppedToBeginning = false;
					
					for (int y = 0; y < 128; y++){
						byte currentColor = current[x][y];
						byte lastColor = last[x][y];
						
						// Don't set changed to true again; just pointless.
						if(Math.abs(currentColor - lastColor) > GifRenderer.this.tolerance && !changed){
							changed = true;
						}
						
						if(changed){
							if(!floppedToBeginning){
								y = 0;
								packetBytes = new byte[131];
								// Why was I doing this 128 times?
								packetBytes[1] = (byte)x;
								floppedToBeginning = true;
								// Don't return because we take advantage of the fact that y is 0.
							}
							
							packetBytes[y + 3] = current[x][y];
						}
					}
					
					if(packetBytes != null){
						GifRenderer.this.cache[index][x] = new PacketPlayOutMap(this.id, packetBytes);
					}
				}
				
				// I dunno what this does; I think it's supposed to remove
				// that folder named "system32" I keep getting told to
				// delete because it causes lag on Windows systems.
				// I just copied and pasted it because I'm mlg tier
				// anonomoose h4x0r. 1v1 me irl quikskop nasa ehue
				last = current;
				// If you don't get the joke above, it represents
				// a typical inexperienced script kiddie. The
				// "1v1..." quote is just a joke on how Brazilian
				// hackers mistargeted NASA thinking it was the NSA.
				// To BR. Hackers: Close enough.
				// In reality, this just sets the last to current.
				// (because, you know, we're done with the current one?)
			}
			
			// Ready to rumble!
			GifRenderer.this.ready = true;
		}
	}
	
}

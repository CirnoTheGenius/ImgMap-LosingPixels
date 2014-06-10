package net.yukkuricraft.tenko.threading;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import net.minecraft.server.v1_7_R3.PacketPlayOutMap;
import net.yukkuricraft.tenko.gbemulator.GPU;
import net.yukkuricraft.tenko.render.RenderUtils;

import org.bukkit.map.MapPalette;

public class DispatchScreenRunnable extends GroupWatchRunnable {
	
	private BufferedImage frame = new BufferedImage(GPU.WIDTH, GPU.HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] data = ((DataBufferInt) frame.getRaster().getDataBuffer()).getData();
	private int id;
	private GPU gpu;
	
	public DispatchScreenRunnable(int id, GPU gpu) {
		this.id = id; // It's a map ID! I swear!
		this.gpu = gpu;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void running() throws Throwable{
		checkState();
		System.arraycopy(gpu.getCurrentFrame(), 0, data, 0, data.length);
		RenderUtils.resizeImage(frame);
		
		flushChannels();
		// TODO: Find a way to optimize this.
		for(int x = 0; x < 128; x++){
			byte[] packetData = new byte[131];
			packetData[1] = (byte) x;
			
			for(int y = 0; y < 128; y++){
				packetData[y + 3] = MapPalette.matchColor(new Color(frame.getRGB(x, y)));
			}
			
			// It's async, so this should be fine.
			writeChannels(new PacketPlayOutMap(id, packetData));
		}
		flushChannels();
		
		try{
			Thread.sleep(20);
		}catch (InterruptedException e){
			e.printStackTrace();
		}
	}
}

package net.yukkuricraft.tenko.threading;

import net.minecraft.server.v1_7_R3.PacketPlayOutMap;
import net.yukkuricraft.tenko.ImgMap;
import net.yukkuricraft.tenko.objs.BufferedGif;
import net.yukkuricraft.tenko.render.GifRenderer;

public class CachingRunnable implements Runnable {
	
	private GifRenderer renderer;
	private BufferedGif gif;
	private int id;
	
	public CachingRunnable(int id, GifRenderer renderer) {
		this.renderer = renderer;
		this.id = id;
		this.gif = renderer.getBufferedGif();
	}
	
	@Override
	public void run(){
		byte[][][] frames = gif.buffer();
		
		if(frames == null){
			ImgMap.logMessage("Failed to buffer frames.");
			return;
		}
		
		PacketPlayOutMap[][] packets = this.renderer.initializeCache(frames.length);
		byte[][] lastFrame = frames[0];
		
		// Initialize the first frame.
		packets[0] = new PacketPlayOutMap[128];
		for(int x = 0; x < 128; x++){
			byte[] packetData = new byte[131];
			packetData[1] = (byte) x;
			System.arraycopy(frames[0][x], 0, packetData, 3, 131);
			
			packets[0][x] = new PacketPlayOutMap(id, packetData);
		}
		
		// Render the rest of the frames.
		for(int index = 1; index < frames.length; index++){
			byte[][] currentFrame = frames[index];
			
			for(int x = 0; x < 128; x++){
				byte[] packetData = this.getChangesFromColumn(x, lastFrame[x], currentFrame[x]);
				if(packetData != null){
					if(packets[index] == null){
						packets[index] = new PacketPlayOutMap[128];
					}
					
					packets[index][x] = new PacketPlayOutMap(this.id, packetData);
				}
			}
			
			lastFrame = currentFrame;
		}
		
		this.renderer.MissionStarto();
	}
	
	public byte[] getChangesFromColumn(int row, byte[] prev, byte[] curr){
		for(int y = 0; y < 128; y++){
			if(Math.abs((prev[y] & 0xFF) - (curr[y] & 0xFF)) > GifRenderer.TOLERANCE){
				byte[] packetData = new byte[131];
				packetData[1] = (byte) row;
				
				System.arraycopy(curr, 0, packetData, 3, curr.length);
				return packetData;
			}
		}
		
		return null;
	}
	
}

package net.yukkuricraft.tenko.threading;

import java.io.IOException;

import net.minecraft.server.v1_7_R1.PacketPlayOutMap;
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
		// This can't be byte[0][0][0] because if we catch IO, we return.
		byte[][][] frames;
		try{
			this.gif.bufferData();
			this.renderer.setMillisecondDelay(this.gif.getMilliDelay());
			frames = this.gif.getFrames();
		}catch (IOException e){
			e.printStackTrace();
			return;
		}
		
		// Supposed to be 128 packets.
		PacketPlayOutMap[][] packets = this.renderer.initializeCache(frames.length);
		byte[][] lastFrame = frames[0];
		
		for(int index = 1; index < frames.length; index++){
			byte[][] currentFrame = frames[index];
			
			for(int row = 0; row < 128; row++){
				byte[] packetData = this.getChangesFromColumn(row, lastFrame[row], currentFrame[row]);
				
				if(packetData != null){
					if(packets[index] == null){
						packets[index] = new PacketPlayOutMap[128];
					}
					
					packets[index][row] = new PacketPlayOutMap(this.id, packetData);
				}
			}
		}
		
		// Ready to rumble!
		System.out.println("Let's do this!");
		this.renderer.setCache(packets);
		this.renderer.MissionStarto();
	}
	
	public byte[] getChangesFromColumn(int row, byte[] prev, byte[] curr){
		for(int y = 0; y < 128; y++){
			if(Math.abs(prev[y] - curr[y]) > GifRenderer.TOLERANCE){
				byte[] packetData = new byte[131];
				packetData[1] = (byte) row;
				
				for(int index = 0; index < 128; index++){
					packetData[index + 3] = curr[index];
				}
				
				return packetData;
			}
		}
		
		return null;
	}
}

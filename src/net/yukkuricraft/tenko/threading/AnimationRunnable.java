package net.yukkuricraft.tenko.threading;

import net.minecraft.server.v1_7_R3.PacketPlayOutMap;

public class AnimationRunnable extends GroupWatchRunnable {
	
	// Likely only shaving off a couple of nano seconds.
	// Edit: Switched PlayerConnection -> Getting the channel directly!
	// Less nanoseconds!
	private PacketPlayOutMap[][] cache;
	private int delay;
	private int index = 0;
	
	public AnimationRunnable(PacketPlayOutMap[][] cache, int delay) {
		try{
			this.cache = cache;
			this.delay = delay;
		}catch (Throwable t){
			t.printStackTrace();
			this.disposeEarly();
		}
	}
	
	@Override
	public void stopRunning(){
		super.stopRunning();
		
		this.cache = null;
	}
	
	@Override
	public void running(){
		checkState();
		this.index++;
		
		if(this.index >= this.cache.length){
			this.stopRunning();
		}
		
		PacketPlayOutMap[] cachedFrame = this.cache[this.index];
		if(cachedFrame != null){
			flushChannels();
			for(int i = 0; i < 128; i++){
				PacketPlayOutMap packet = cachedFrame[i];
				if(packet != null){
					writeChannels(packet);
				}
				
				if(i == 63){
					flushChannels();
				}
			}
			flushChannels();
		}
		
		try{
			Thread.sleep(this.delay * 10);
		}catch (InterruptedException e){
			e.printStackTrace();
		}
	}
}

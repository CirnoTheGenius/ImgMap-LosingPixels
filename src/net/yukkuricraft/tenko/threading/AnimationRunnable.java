package net.yukkuricraft.tenko.threading;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R2.NetworkManager;
import net.minecraft.server.v1_7_R2.PacketPlayOutMap;
import net.minecraft.util.io.netty.channel.Channel;

import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class AnimationRunnable extends AbstractSafeRunnable {

	// Likely only shaving off a couple of nano seconds.
	// Edit: Switched PlayerConnection -> Getting the channel directly!
	// Less nanoseconds!
	private Channel oniichan; // Hey guys, I just signed my soul to the devil!
	private PacketPlayOutMap[][] cache;
	private int delay;
	private int index = 0;

	public AnimationRunnable(Player plyr, PacketPlayOutMap[][] cache, int delay) {
		try{
			NetworkManager netty = ((CraftPlayer) plyr).getHandle().playerConnection.networkManager;
			Field field = NetworkManager.class.getDeclaredField("m");
			field.setAccessible(true);
			this.oniichan = (Channel) field.get(netty);
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
		this.oniichan = null;
	}

	@Override
	public void running(){
		if(!this.oniichan.isOpen() || !this.isRunning()){
			this.stopRunning();
		}
		
		index++;

		if(index >= cache.length){
			return;
		}

		PacketPlayOutMap[] cachedFrame = cache[index];
		if(cachedFrame != null){
			for(PacketPlayOutMap packet : cachedFrame){
				if(packet != null){
					AnimationRunnable.this.oniichan.write(packet);
				}
			}
			
			this.oniichan.flush();
		}
		
		try{
			Thread.sleep(this.delay * 10);
		}catch (InterruptedException e){
			e.printStackTrace();
		}
	}
}

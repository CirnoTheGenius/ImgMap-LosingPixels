package net.yukkuricraft.tenko.threading;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R1.NetworkManager;
import net.minecraft.server.v1_7_R1.PacketPlayOutMap;
import net.minecraft.util.io.netty.channel.Channel;
import net.minecraft.util.io.netty.channel.ChannelFuture;

import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class AnimationRunnable extends AbstractSafeRunnable {
	
	// Likely only shaving off a couple of nano seconds.
	// Edit: Switched PlayerConnection -> Getting the channel directly!
	// Less nanoseconds!
	private Channel oniichan; // Hey guys, I just signed my soul to the devil!
	private PacketPlayOutMap[][] cache;
	private int delay;
	
	public AnimationRunnable(Player plyr, PacketPlayOutMap[][] cache, int delay) {
		try{
			NetworkManager netty = ((CraftPlayer) plyr).getHandle().playerConnection.networkManager;
			Field field = NetworkManager.class.getDeclaredField("k");
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
	public void running(){
		for(PacketPlayOutMap[] element : this.cache){
			if(element != null){
				for(PacketPlayOutMap packet : element){
					if(packet != null){
						if(!this.oniichan.isOpen()){
							this.stopRunning();
						}
						
						ChannelFuture future = AnimationRunnable.this.oniichan.writeAndFlush(packet);
						future.awaitUninterruptibly();
					}
				}
			}
			
			try{
				Thread.sleep(this.delay * 10);
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
	}
}

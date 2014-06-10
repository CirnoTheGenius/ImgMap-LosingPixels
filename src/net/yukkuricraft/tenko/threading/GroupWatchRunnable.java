package net.yukkuricraft.tenko.threading;

import java.lang.reflect.Field;
import java.util.*;

import net.minecraft.server.v1_7_R3.NetworkManager;
import net.minecraft.server.v1_7_R3.Packet;
import net.minecraft.util.io.netty.channel.Channel;

import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public abstract class GroupWatchRunnable extends AbstractSafeRunnable {
	
	// Why a HashSet and not a list?
	// List.contains() is O(n)
	// Set.contains() is O(1)
	private Set<UUID> watching = new HashSet<>();
	// If you ask why I'm using a Channel and not a HashMap:
	// Because I can.
	private Channel[] oneechans = new Channel[2];
	private int nextIndex = 0;
	
	public void addPlayer(Player plyr){
		if(!(plyr instanceof CraftPlayer)){
			System.out.println("Recieved weird Player type! Is this CraftBukkit?");
			this.disposeEarly();
			return;
		}
		
		// They're MY Onee-chans!
		synchronized(oneechans){
			trimOneechans();
			NetworkManager manager = ((CraftPlayer) plyr).getHandle().playerConnection.networkManager;
			
			try{
				Field mField = NetworkManager.class.getDeclaredField("m");
				mField.setAccessible(true);
				Channel chan = (Channel) mField.get(manager);
				
				// This is silly
				if(nextIndex + 1 >= oneechans.length){
					// Allocate 3 more slots.
					Channel[] newOneechans = new Channel[oneechans.length + 3];
					// According here: http://stackoverflow.com/questions/18638743/is-it-better-to-use-system-arraycopy-than-a-fast-for-loop-for-copying-array
					// Manual for-loops are faster than System.arraycopy for Objects.
					// Strange, huh?
					for(int i = 0; i < oneechans.length; i++){
						newOneechans[i] = oneechans[i];
					}
					oneechans = newOneechans;
					newOneechans = null; // Notice me, GC-senpai!
				}
				
				oneechans[nextIndex] = chan;
				watching.add(plyr.getUniqueId());
				for(int i = oneechans.length - 1; i > 0; i--){
					if(oneechans[i] != null){
						nextIndex = i + 1;
						return;
					}
				}
			}catch (ReflectiveOperationException e){
				e.printStackTrace();
			}
		}
	}
	
	public boolean isPlayerWatching(Player plyr){
		return watching.contains(plyr.getUniqueId());
	}
	
	public void checkState(){
		for(int index = 0; index < oneechans.length; index++){
			if(oneechans[index] != null && oneechans[index].isOpen()){
				return;
			}
		}
		
		this.stopRunning();
	}
	
	@Override
	public void stopRunning(){
		super.stopRunning();
		oneechans = null;
	}
	
	public void writeChannels(Packet packet){
		for(int i = 0; i < oneechans.length; i++){
			if(oneechans[i] != null && oneechans[i].isOpen()){
				oneechans[i].write(packet);
			}
		}
	}
	
	public void flushChannels(){
		for(int i = 0; i < oneechans.length; i++){
			if(oneechans[i] != null && oneechans[i].isOpen()){
				oneechans[i].flush();
			}
		}
	}
	
	public void trimOneechans(){
		
		for(int index = 0; index < oneechans.length; index++){
			Channel chan = oneechans[index];
			if(!chan.isOpen() || !chan.isWritable()){
				oneechans[index] = null;
			}
		}
		
		Arrays.sort(oneechans, new Comparator<Channel>() {
			
			@Override
			public int compare(Channel oniichan, Channel oneechan){
				if(oniichan == null){
					return 1;
				}
				if(oneechan == null){
					return -1;
				}
				return 1;
			}
			
		});
	}
	
}

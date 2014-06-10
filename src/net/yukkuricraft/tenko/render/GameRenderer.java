package net.yukkuricraft.tenko.render;

import java.io.FileNotFoundException;

import net.yukkuricraft.tenko.gbemulator.Z80MPU;
import net.yukkuricraft.tenko.threading.DispatchScreenRunnable;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.map.*;

public class GameRenderer extends MapRenderer implements StoppableRenderer {
	
	private boolean hasRendered = false;
	private Z80MPU mpu;
	private DispatchScreenRunnable uiThread;
	private boolean inputHolds;
	private String romName;
	private String controller;
	
	public GameRenderer(String romName, String controller) {
		this.romName = romName;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void render(final MapView view, MapCanvas canvas, final Player plyr){
		if(!this.hasRendered){
			this.hasRendered = true;
			mpu = new Z80MPU();
			try{
				mpu.loadROM(romName);
			}catch (Exception e){
				if(e instanceof FileNotFoundException){
					plyr.sendMessage(ChatColor.RED + "Couldn't find ROM by name " + romName);
				}else{
					plyr.sendMessage(ChatColor.RED + "Failed to start emulation.");
					e.printStackTrace();
				}
				
				view.addRenderer(new DummyRenderer());
				view.removeRenderer(this);
				return;
			}
			mpu.initialize();
			mpu.start();
			uiThread = new DispatchScreenRunnable(view.getId(), mpu.getGPU());
			uiThread.addPlayer(plyr);
			uiThread.start();
		}
		
		if(!uiThread.isPlayerWatching(plyr)){
			uiThread.addPlayer(plyr);
		}
	}
	
	public boolean shouldHoldInput(){
		return inputHolds;
	}
	
	public void inverseHold(){
		inputHolds = !inputHolds;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void stopRendering(){
		mpu.stop();
		uiThread.stopRunning();
	}
	
	public String getController(){
		return controller;
	}
	
	public Z80MPU getMPU(){
		return this.mpu;
	}
}

package net.yukkuricraft.tenko.threading;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractSafeRunnable implements Runnable {
	
	// More fanciness.
	private AtomicBoolean isRunning = new AtomicBoolean(false);
	
	@Override
	public void run(){
		this.isRunning.set(true);
		
		while(this.isRunning.get()){
			if(this.isRunning.get() == false){
				break;
			}
			
			this.running();
		}
	}
	
	public void stopRunning(){
		this.isRunning.compareAndSet(true, false);
	}
	
	public boolean isRunning(){
		return this.isRunning.get();
	}
	
	public abstract void running();
	
}

package net.yukkuricraft.tenko.threading;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractSafeRunnable implements Runnable {
	
	// More fanciness.
	private AtomicBoolean isRunning = new AtomicBoolean(false);
	private Thread thread;
	
	@Override
	public void run(){
		if(this.isRunning == null){
			return; // Huzzah!
		}
		
		this.isRunning.set(true);
		
		while(this.isRunning.get()){
			try{
				this.running();
			}catch (Throwable e){
				e.printStackTrace();
				break;
			}
		}
	}
	
	public Thread start(){
		if(this.thread == null){
			this.thread = new Thread(this);
			this.thread.start();
		}
		return this.thread;
	}
	
	public Thread getThread(){
		return this.thread;
	}
	
	public void disposeEarly(){
		this.isRunning = null;
	}
	
	public void stopRunning(){
		this.isRunning.set(false);
	}
	
	public boolean isRunning(){
		return this.isRunning.get();
	}
	
	public abstract void running() throws Throwable;
	
}

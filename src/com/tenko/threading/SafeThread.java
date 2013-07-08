package com.tenko.threading;

public abstract class SafeThread extends Thread {
	
	protected boolean running;
	
	public SafeThread(String name){
		super(name);
	}
	
	public boolean isRunning(){
		return running;
	}
	
	//Allow other threads to implement a "clean-up" routine.
	public abstract void stopThread();
	
	@Override
	public abstract void run();
}

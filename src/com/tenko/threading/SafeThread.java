package com.tenko.threading;

public class SafeThread extends Thread {
	
	protected boolean running;
	
	public SafeThread(String name){
		super(name);
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public void stopThread(){
		running = false;
	}
	
}

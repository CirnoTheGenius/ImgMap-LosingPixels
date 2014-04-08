package net.yukkuricraft.tenko.threading;

public class CallbackThread extends Thread {
	
	public static Thread create(final Runnable runnable, final Runnable finished){
		return new Thread(new Runnable() {
			
			@Override
			public void run(){
				runnable.run();
				finished.run();
			}
			
		});
	}
	
}

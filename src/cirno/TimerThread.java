package cirno;

import java.util.concurrent.atomic.AtomicBoolean;

public class TimerThread extends Thread {

	int waitTime;

	AtomicBoolean finished = new AtomicBoolean(false);

	public TimerThread(int waitTime){
		this.waitTime = waitTime;
	}

	@Override
	public void run(){
		System.out.print("[Debugging ImgMap] Started waiting");
		time(waitTime);
	}

	public void time(long waitTime){
		if(finished.get() == false) try {
			System.out.print("[Debugging ImgMap] Waiting");
			sleep(waitTime);
			System.out.print("[Debugging ImgMap] Wait complete.");
			finished.set(true);
			System.out.print("[Debugging ImgMap] Set 'finished' to true.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}


	public void reset(int waitTime){
		this.waitTime = waitTime;
		finished.set(false);
	}
}

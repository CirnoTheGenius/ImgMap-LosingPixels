package cirno;

public class TimerThread extends Thread {

	long waitTime;
	boolean finished;
	
	public TimerThread(long waitTime){
		this.waitTime = waitTime;
	}

	public TimerThread time(long waitTime){
		try {
			this.wait(waitTime);
			this.finished = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	
	public void reset(long waitTime){
		this.waitTime = waitTime;
		this.finished = false;
	}
}

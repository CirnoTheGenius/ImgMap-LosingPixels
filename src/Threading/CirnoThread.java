package Threading;

public class CirnoThread extends Thread{
	protected boolean running;

	public CirnoThread(String string){
		super(string);
	}

	public void stopRunning(){
		this.running = false;
	}

	public void start(){
		this.running = true;
		super.start();
	}
}
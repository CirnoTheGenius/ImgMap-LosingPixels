package Threading;


public class CirnoThread extends Thread {
	
	/**
	 * 
	 * A safer version of threads by Cirno.
	 * 
	 */
	
	protected boolean running;
	
	public CirnoThread(CirnoThreadGroup tg, String string) {
		super(tg, string);
	}
	
	public void stopRunning(){
		this.running = false;
	}
	
	@Override
	public void start(){
		this.running = true;
		super.start();
	}
	
}

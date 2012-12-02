package Threading;

import java.util.ArrayList;


public class CirnoThreadGroup extends ThreadGroup {

	ArrayList<CirnoThread> threads = new ArrayList<CirnoThread>();
	
	public CirnoThreadGroup(String s) {
		super(s);
	}
	
	public void addToList(CirnoThread t){
		threads.add(t);
	}
	
	public void stopRunning(){
		for(CirnoThread ct : threads){
			ct.running = false;
		}
	}
	
	public void start() {
		for(CirnoThread ct : threads){
			ct.start();
		}
	}
}

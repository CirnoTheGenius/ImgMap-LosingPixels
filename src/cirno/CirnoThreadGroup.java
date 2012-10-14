package cirno;

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
		for(int i=0; i < threads.size(); i++){
			threads.get(i).running = false;
		}
	}
	
	public void start() {
		for(CirnoThread ct : threads){
			ct.start();
		}
	}
	
}

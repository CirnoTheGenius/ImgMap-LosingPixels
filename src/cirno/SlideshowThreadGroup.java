package cirno;

import java.util.ArrayList;

public class SlideshowThreadGroup extends ThreadGroup {

	ArrayList<SlideshowRenderer> threads = new ArrayList<SlideshowRenderer>();
	
	public SlideshowThreadGroup(String s) {
		super(s);
	}
	
	public void addToList(SlideshowRenderer t){
		threads.add(t);
	}
	
	public void stopRunning(){
		for(int i=0; i < threads.size(); i++){
			threads.get(i).running = false;
		}
	}
	
}

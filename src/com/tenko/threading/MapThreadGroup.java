package com.tenko.threading;

import java.util.ArrayList;
import java.util.List;

public class MapThreadGroup {
	
	private List<SlideshowThread> st = new ArrayList<SlideshowThread>();
	
	/**
	 * Group class for SlideshowThreads. Greatly going to regret doing this.
	 */
	public MapThreadGroup() {}
	
	public List<SlideshowThread> getThreads(){
		return st;
	}
	
	public void enumerate(SlideshowThread[] dest){
		SlideshowThread[] src = st.toArray(new SlideshowThread[st.size()]);
		System.arraycopy(src, 0, dest, 0, dest.length);
	}
	
	public int activeCount(){
		int i = 0;
		for(SlideshowThread t : st){
			if(t.amIAlive()){
				i++;
			}
		}
		return i;
	}
}
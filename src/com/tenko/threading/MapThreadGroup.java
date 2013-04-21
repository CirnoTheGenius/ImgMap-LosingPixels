package com.tenko.threading;

import java.util.ArrayList;
import java.util.List;

public class MapThreadGroup {

	/**
	 * List of threads.
	 */
	private List<SlideshowThread> st = new ArrayList<SlideshowThread>();

	/**
	 * Group class for SlideshowThreads. Greatly going to regret doing this.
	 */
	public MapThreadGroup() {}

	/**
	 * Get the a list of threads.
	 * @return The list of threads running in this thread group.
	 */
	public List<SlideshowThread> getThreads(){
		return st;
	}

	/**
	 * Returns an array of threads.
	 * @param dest - The destination to copy the list to.
	 */
	public void enumerate(SlideshowThread[] dest){
		SlideshowThread[] src = st.toArray(new SlideshowThread[st.size()]);
		System.arraycopy(src, 0, dest, 0, dest.length);
	}

	/**
	 * Returns an active count of threads.
	 * @return An integer with the amount of active threads.
	 */
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
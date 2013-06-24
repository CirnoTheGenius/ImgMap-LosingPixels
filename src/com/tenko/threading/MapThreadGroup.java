package com.tenko.threading;

import java.util.ArrayList;
import java.util.List;

public class MapThreadGroup {

	/**
	 * List of threads.
	 */
	private List<SafeThread> st = new ArrayList<SafeThread>();

	/**
	 * Group class for SlideshowThreads. Greatly going to regret doing this.
	 */
	public MapThreadGroup() {}

	/**
	 * Get the a list of threads.
	 * @return The list of threads running in this thread group.
	 */
	public List<SafeThread> getThreads(){
		return st;
	}

	/**
	 * Stops all threads.
	 */
	public void stopThreads(){
		for(SafeThread t : st){
			t.stopThread();
		}
	}

}
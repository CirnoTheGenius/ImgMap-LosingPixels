package com.tenko.threading;

import java.util.ArrayList;
import java.util.List;

public class MapThreadGroup {

	private List<SafeThread> st = new ArrayList<SafeThread>();

	public MapThreadGroup() {}

	public List<SafeThread> getThreads(){
		return st;
	}

	public void stopThreads(){
		for(SafeThread t : st){
			t.stopThread();
		}
	}

}
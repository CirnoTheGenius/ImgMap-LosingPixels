package com.tenko.volatileNMS;

import java.util.HashMap;

public class NBTDataGroup {
	
	private HashMap<String, Object> values;
	
	public NBTDataGroup(){
		values = new HashMap<String, Object>();
	}
	
	public boolean storeVariable(String name, Object value){
		if(!values.containsKey(name)){
			values.put(name, value);	
			return true;
		}
		
		return false;
	}
}

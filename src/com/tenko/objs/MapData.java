package com.tenko.objs;

public class MapData {
	
	private final String url;
	private final short id;
	
	private MapData(short mapId, String theURL){
		this.url = theURL;
		this.id = mapId;
	}
	
	public static MapData convertData(String s) throws InvalidMapDataException{
		if(s.indexOf(":") > -1 && s.split(":")[1].length() > 0){
			try {
				short id = Short.valueOf(s.split(":")[0]);
				String url = s.split(":")[1];
				
				return new MapData(id, url);
			} catch (NumberFormatException e){
				//Yup. If we catch an exception, just throw this one instead.
				throw new InvalidMapDataException("The string provided doesn't appear to be in the proper format!");
			}
		}
		
		throw new InvalidMapDataException("The string provided doesn't appear to be in the proper format!");
	}
	
	public String getUrl(){
		return url;
	}
	
	public short getId(){
		return id;
	}
	
	public static class InvalidMapDataException extends Exception {
		InvalidMapDataException(String reason){
			super(reason);
		}
	}
}
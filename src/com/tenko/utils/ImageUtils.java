package com.tenko.utils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.tenko.ImgMap;

public class ImageUtils {
	
	public static File getLocalImage(String imageName){
		File[] dir = new File(ImgMap.getInstance().getDataFolder(), "images").listFiles();
		
		for(File f : dir){
			if(f.getName().equalsIgnoreCase(imageName)){
				return f;
			}
		}
		
		return null;
	}
	
	public static boolean isLocal(String fileName){
		return getLocalImage(fileName) != null;
	}
	
	public static boolean isImageCompatible(String url){
		try {
			URL theURL = new URL(url);
			HttpURLConnection con = (HttpURLConnection)theURL.openConnection();
			con.setRequestMethod("HEAD");
			con.connect();
			String toReturn = con.getContentType();
			con.disconnect();

			return toReturn.startsWith("image");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
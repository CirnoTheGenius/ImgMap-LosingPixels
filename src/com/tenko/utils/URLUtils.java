package com.tenko.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class URLUtils {

	public static boolean compatibleImage(String s){
		try {
			String contentType = getContentType(new URL(s));
			if(contentType.startsWith("image")){
				return true;
			}
		} catch (MalformedURLException e){
			//Do nothing, since it's most likely the users fault.
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

	private final static String getContentType(URL theURL) throws IOException {
		HttpURLConnection con = (HttpURLConnection)theURL.openConnection();
		con.setRequestMethod("HEAD");
		con.connect();
		String toReturn = con.getContentType();
		con.disconnect();
		return toReturn;
	}

}
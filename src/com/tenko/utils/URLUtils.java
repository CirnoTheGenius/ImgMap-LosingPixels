package com.tenko.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * URLUtils used in ImgMap for content-type checking.
 * @author Tsunko
 */
public class URLUtils {
	
	/**
	 * Checks to see if the image is a proper type. Currently uses Content-types.
	 * @param s - The URL to try to check.
	 * @return
	 */
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
	
	/**
	 * Gets the content type. Do not use outside of this class. No point.
	 * @param theURL - The URL object.
	 * @return A string featuring the content-type.
	 * @throws IOException
	 */
	private final static String getContentType(URL theURL) throws IOException {
		HttpURLConnection con = (HttpURLConnection)theURL.openConnection();
		con.setRequestMethod("HEAD");
		con.connect();
		String toReturn = con.getContentType();
		con.disconnect();
		return toReturn;
	}

}
package com.tenko.utils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.tenko.ImgMap;

/**
 * URLUtils used in ImgMap for content-type checking.
 * @author Tsunko
 */
public class URLUtils {

	public static String getLocal(String localName) throws SecurityException, MalformedURLException {
		String tmp = "";
		
		File[] dir = new File(ImgMap.getPlugin().getDataFolder(), "images").listFiles();
		
		for(File f : dir){
			if(f.getName().equalsIgnoreCase(localName)){
				tmp = f.toURI().toURL().toExternalForm();
				break;
			}
		}
		
		return tmp;
	}

	public static boolean isLocal(String fileName){
		try {
			return !getLocal(fileName).isEmpty();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	/**
	 * Checks to see if the image is a proper type. Currently uses Content-types.
	 * @param s - The URL to try to check.
	 * @return True if the image is compatible; false otherwise.
	 */
	public static boolean compatibleImage(String s){
		try {
			String contentType = getContentType(new URL(s));
			if(contentType.startsWith("image")){
				return true;
			}
		} catch (MalformedURLException e){
			return false;
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Checks to see if the image is a proper type. Currently uses Content-types.
	 * @param s - The URL to try to check.
	 * @return True if the image is compatible; false otherwise.
	 */
	public static boolean compatibleImage(URL u){
		return compatibleImage(u.toExternalForm());
	}

	/**
	 * Gets the content type. Do not use outside of this class. No point.
	 * @param theURL - The URL object.
	 * @return A string featuring the content-type.
	 * @throws IOException
	 */
	private static String getContentType(URL theURL) throws IOException {
		HttpURLConnection con = (HttpURLConnection)theURL.openConnection();
		con.setRequestMethod("HEAD");
		con.connect();
		String toReturn = con.getContentType();
		con.disconnect();

		return toReturn;
	}

}
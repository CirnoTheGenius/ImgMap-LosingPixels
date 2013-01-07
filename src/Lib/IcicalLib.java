package Lib;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.imageio.ImageIO;

import cirno.Nineball;

public class IcicalLib {
	private final static String[] Formats = { "jpg", "jpeg", "png", "gif", "bmp" };

	//IcicalLib: A baka library by Cirno.
	
	public static Image resizeImage(String s){
		BufferedImage resizedImage = new BufferedImage(128, 128, 2);
		try {
			BufferedImage originalImage = ImageIO.read(new URL(s));
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, 128, 128, null);
			g.finalize();
			g.dispose();
			resizedImage.flush();
		} catch (IOException e){
			e.printStackTrace();
		}
		return resizedImage;
	}

	//I'm going to regret doing this.
	public static boolean compatibleImage(String s){
		if(s.contains("bit.ly") || s.contains("tinyurl.com") || s.contains("goo.gl")){
			return true;
		}
		for(String suffix : Formats) if(s.toLowerCase().endsWith(suffix)){
			return true;
		}
		return false;
	}

	//Until I can find a better way to do this, keep using this.
	public static int getFlagType(String[] args){
		boolean perm = false;
		boolean local = false;
		
		for(String l : args){
			if(l.equalsIgnoreCase("-p")){
				perm = true;
			}
			
			if(l.equalsIgnoreCase("-l")){
				local = true;
			}
		}

		int i = 0;
		
		if(perm){
			i = 1;
		} else if(local){
			i = 2;
		} else if(perm && local){
			i = 3;
		}
		
		return i;
	}
	
	public static FileType isLocalOrInternet(String s, Nineball c){
		try {
			for(File f : c.getDataFolder().listFiles()){
				if(f.getName().equalsIgnoreCase(s)){
					return FileType.LOCAL;
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}

		return FileType.INTERNET;
	}
	
	public static String getLocalURL(String s, Nineball c){
		try {
			for(File f : c.getDataFolder().listFiles()){
				if(f.getName().equalsIgnoreCase(s)){
					return f.toURI().toURL().toString();
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
}

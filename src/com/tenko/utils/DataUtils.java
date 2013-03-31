package com.tenko.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.tenko.ImgMap;

/**
 * DataUtils used in ImgMap in order to modify Maps.list
 * @author Tsunko
 */
public class DataUtils {
	
	//Format for reading and writing where id=(the map id) and where string=(the map's image)
	//id:string
	
	/**
	 * Checks to make sure the data folder exists.
	 * @throws IOException
	 */
	public static void checkDataFolder() throws IOException {
		ImgMap.getPlugin().getDataFolder().mkdir();
	}
	
	/**
	 * Checks a folder via mkir().
	 * @param s - The name of the folder to be created (Will be located under /plugins/ImgMap/)
	 * @throws IOException
	 */
	public static void checkFolder(String s) throws IOException {
		new File(ImgMap.getPlugin().getDataFolder() + "/"+s+"/").mkdir(); 
	}
	
	/**
	 * Checks a file via createNewFile().
	 * @param s - The name of the file to be created (Will be located under /plugins/ImgMap/)
	 * @return The file we were trying to check.
	 * @throws IOException
	 */
	public static File checkFile(String s) throws IOException {
		File f = new File(ImgMap.getPlugin().getDataFolder().getAbsolutePath(), s);
		f.createNewFile();
		return f;
	}
	
	/**
	 * Checks a file in the given directory via createNewFile().
	 * @param s - The name of the file to be created.
	 * @param dir - The directory the file is located in (Will be inside of /plugins/ImgMap/)
	 * @return The file we were trying to check.
	 * @throws IOException
	 */
	public static File checkFile(String s, String dir) throws IOException {
		File f = new File(ImgMap.getPlugin().getDataFolder().getAbsolutePath() + "/"+dir+"/", s);
		f.createNewFile();
		return f;
	}
	
	/**
	 * Just calls BufferedWriter to write a line and then create a new line, then closes the stream. Always appends.
	 * @param f - The file.
	 * @param s - The string.
	 * @throws IOException
	 */
	public static void write(File f, String s) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
		bw.write(s);
		bw.newLine();
		bw.close();
	}
	
	/**
	 * Writes an entire array to the given file.
	 * @param f - The file.
	 * @param s - The string array.
	 * @throws IOException
	 */
	public static void writeArray(File f, String[] s) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
		
		for(String l : s){
			bw.write(l);
			bw.newLine();
		}
		
		bw.close();
	}
	
	/**
	 * Writes a comment. Non-functional, do not use.
	 * @param f - The file.
	 * @param s - Comment to add.
	 * @throws IOException
	 */
	//public static void writeComment(File f, String s) throws IOException {
	//	write(f, "#" + s);
	//}
	
	/**
	 * Writes data to the file.
	 * @param f - The file.
	 * @param i - The map ID.
	 * @param s - The data/URL associated.
	 * @throws IOException
	 */
	public static void write(File f, int i, String s) throws IOException {
		write(f, i + ":" + s);
	}
	
	/**
	 * Replaces the data of src with dest.
	 * @param f - The file.
	 * @param src - The map ID that is to be replaced.
	 * @param dest - The new data to put in.
	 * @throws IOException
	 */
	public static void replace(File f, int src, String dest) throws IOException {
		ArrayList<String> contents = new ArrayList<String>();

		//Reading
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;

		while((line = br.readLine()) != null){
			contents.add(line.startsWith(String.valueOf(src)) ? src+":"+dest : line);
		}

		br.close();

		//Writing
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		
		for(String l : contents){
			bw.write(l);
			bw.newLine();
		}
		
		bw.close();
	}
	
	/**
	 * Loops through the file, via getLines(f), and checks every line to see if src exists already. If not, append to the end.
	 * @param f - The file
	 * @param src - The map ID that is to be replaced or to be appended.
	 * @param dest - The new data to be put in or to be appended.
	 * @throws IOException
	 */
	public static void replaceOrWrite(File f, int src, String dest) throws IOException {
		boolean isExisting = false;
		
		for(String l : getLines(f)){
			if(l.startsWith(String.valueOf(src))){
				isExisting = true;
				replace(f, src, dest);
				break;
			}
		}
		
		if(isExisting){
			write(f, src, dest);
		}
	}
	
	/**
	 * Removes any data associated with target.
	 * @param f - The file.
	 * @param target - The target data to be removed.
	 * @throws IOException
	 */
	public static void delete(File f, int target) throws IOException {
		ArrayList<String> contents = new ArrayList<String>();

		//Reading
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;

		while((line = br.readLine()) != null){
			if(!line.startsWith(String.valueOf(target))){
				contents.add(line);
			}
		}

		br.close();

		//Writing
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));

		for(String l : contents){
			bw.write(l);
			bw.newLine();
		}

		bw.close();
	}
	
	public static void deleteSlideshow(int id) throws IOException {
		ImgMap.getSlideshowFile(id).delete();
	}
	
	/**
	 * Returns an Iterable array of Strings inside of a file.
	 * @param f - The file.
	 * @return An Iterable array.
	 * @throws IOException
	 */
	public static List<String> getLines(File f) throws IOException {
		ArrayList<String> contents = new ArrayList<String>();
		
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;
		
		while((line = br.readLine()) != null){
			contents.add(line);
		}
		
		br.close();
		return contents;
	}
	
	/**
	 * Returns the data associated with the target.
	 * @param f - The file.
	 * @param target - The target data to get.
	 * @return A string containing the URL with the target data.
	 * @throws IOException
	 */
	public static String get(File f, int target) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;
		
		while((line = br.readLine()) != null){
			if(line.startsWith(String.valueOf(target))){
				br.close();
				return line.substring(line.indexOf(":")+1, line.length());
			}
		}

		br.close();
		return null;
	}
	
	/**
	 * Empties an entire file's contents.
	 * @param f - The file to blank.
	 * @throws IOException 
	 */
	public static void blankFile(File f) throws IOException{
		f.delete();
		f.createNewFile();
	}
}

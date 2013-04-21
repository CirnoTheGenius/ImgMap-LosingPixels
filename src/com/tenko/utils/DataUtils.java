package com.tenko.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import com.google.common.io.Files;
import com.tenko.ImgMap;

/**
 * DataUtils used in ImgMap in order to modify Slideshow data and Maps.list.
 * This time, I will attempt to use every pregiven library included only in CraftBukkit, such as com.google.common.io.
 * @author Tsunko
 */
public class DataUtils {

	/**
	 * Check if the main Plugin folder exists, then checks if the Maps.list file and Slideshow directory exist.
	 * @throws IOException
	 */
	public static void initialize() throws IOException {
		ImgMap.getPlugin().getDataFolder().mkdir();
		new File(ImgMap.getPlugin().getDataFolder(), "Maps.list").createNewFile();
		new File(ImgMap.getPlugin().getDataFolder(), "SlideshowData").mkdir();
		new File(ImgMap.getPlugin().getDataFolder(), "images").mkdir();
	}

	/**
	 * appends id:url to the file. Never used outside; locally only.
	 * @param f - The file to write to.
	 * @param id - The map ID.
	 * @param url - The URL to write.
	 * @throws IOException
	 */
	private static void append(File f, int id, String url) throws IOException{
		Files.append(id+":"+url+System.getProperty("line.separator"), f, Charset.defaultCharset());
	}

	/**
	 * Writes s to file f.
	 * @param f - The file to write to.
	 * @param s - The string to write.
	 * @throws IOException
	 */
	public static void write(File f, String s) throws IOException{
		BufferedWriter bw = Files.newWriter(f, Charset.defaultCharset());
		bw.write(s);
		bw.newLine();
		bw.flush();
		bw.close();
	}

	/**
	 * Writes an entire array to the given file.
	 * @param f - The file.
	 * @param s - The string array.
	 * @throws IOException
	 */
	public static void writeArray(File f, String[] s) throws IOException {
		BufferedWriter bw = Files.newWriter(f, Charset.defaultCharset());

		for(String l : s){
			bw.write(l);
			bw.newLine();
		}

		bw.flush();
		bw.close();
	}

	/**
	 * Replaces the data of src with dest. Do not use outside of this class.
	 * @param f - The file.
	 * @param src - The map ID that is to be replaced.
	 * @param dest - The new data to put in.
	 * @throws IOException
	 */
	private static void replace(int src, String dest, File f) throws IOException{
		List<String> contents = Files.readLines(f, Charset.defaultCharset());
		BufferedWriter bw = Files.newWriter(f, Charset.defaultCharset());

		for(String l : contents){
			bw.write(l.startsWith(String.valueOf(src)+":") ? src+":"+dest : l);
			bw.newLine();
		}

		bw.flush();
		bw.close();
	}

	/**
	 * Checks every line in the file to see if src exists already. If not, append to the end.
	 * @param f - The file
	 * @param src - The map ID that is to be replaced or to be appended.
	 * @param dest - The new data to be put in or to be appended.
	 * @throws IOException
	 */
	public static void replace(File f, int src, String dest) throws IOException {
		List<String> contents = Files.readLines(f, Charset.defaultCharset());
		boolean isExisting = false;

		for(String l : contents){
			if(l.startsWith(String.valueOf(src)+":")){
				isExisting = true;
				replace(src, dest, f);
			}
		}

		if(!isExisting){
			append(f, src, dest);
		}
	}

	/**
	 * Removes any data associated with target.
	 * @param f - The file.
	 * @param target - The target data to be removed.
	 * @throws IOException
	 */
	public static void delete(File f, int target) throws IOException {
		List<String> contents = Files.readLines(f, Charset.defaultCharset());
		BufferedWriter bw = Files.newWriter(f, Charset.defaultCharset());

		for(String l : contents){
			if(!l.startsWith(String.valueOf(target)+":")){
				bw.write(l);
				bw.newLine();
			}
		}

		bw.flush();
		bw.close();
	}
	
	/**
	 * Does what is says on the can.
	 * @param id - The map ID
	 */
	public static void deleteSlideshow(int id) {
		ImgMap.getSlideshowFile(id).delete();
	}
}

package com.tenko.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.tenko.ImgMap;

public class DataUtils {
	
	//Format:
	//id (integer):url
	
	public static void checkFolder() throws IOException {
		ImgMap.getPlugin().getDataFolder().mkdir();
	}
	
	public static File createFile(String s) throws IOException {
		File f = new File(ImgMap.getPlugin().getDataFolder().getAbsolutePath(), s);
		f.createNewFile();
		return f;
	}

	public static void write(File f, String s, boolean append) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(f, append));
		bw.write(s);
		bw.newLine();
		bw.close();
	}
	
	public static void write(File f, int i, String s, boolean append) throws IOException {
		write(f, i + ":" + s, append);
	}

	public static void replace(File f, String src, String dest) throws IOException {
		ArrayList<String> contents = new ArrayList<String>();

		//Reading
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;

		while((line = br.readLine()) != null){
			contents.add(line.startsWith(src) ? dest : line);
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

	public static void delete(File f, String target) throws IOException {
		ArrayList<String> contents = new ArrayList<String>();

		//Reading
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;

		while((line = br.readLine()) != null){
			if(!line.startsWith(target)){
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
	
	public static String get(File f, String target) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;

		while((line = br.readLine()) != null){
			if(!line.startsWith(target)){
				br.close();
				return target;
			}
		}
		
		br.close();
		return null;
	}
	
	public static Iterable<String> getLines(File f) throws IOException {
		ArrayList<String> contents = new ArrayList<String>();

		//Reading
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;

		while((line = br.readLine()) != null){
			contents.add(line);
		}
		
		br.close();
		
		return contents;
	}
	
	public static int getSize(File f) throws IOException {
		int size = 0;
		
		BufferedReader br = new BufferedReader(new FileReader(f));
		while(br.readLine() != null){
			size++;
		}
		br.close();
		
		return size;
	}
}

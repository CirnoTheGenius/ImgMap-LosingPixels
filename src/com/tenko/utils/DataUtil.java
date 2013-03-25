package com.tenko.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

public class DataUtil {

	private final JavaPlugin jplugin;

	/**
	 * Class utility to edit a general files.
	 * I would probably use this universally in all plugins.
	 */
	public DataUtil(JavaPlugin m){
		jplugin = m;
	}

	public boolean checkFile(String fileName){
		return new File(jplugin.getDataFolder(), fileName).exists();
	}

	public File createFile(String fileName){
		File f = null;
		try {
			f = new File(jplugin.getDataFolder(), fileName);
			f.createNewFile();
		} catch (Exception e){
			e.printStackTrace();
		}
		return f;
	}

	public void setAttributeData(String attribute, String data, File f) throws IOException, FileNotFoundException{
		BufferedReader eyes = null;
		BufferedWriter typeWriter = null;

		//Reading & determining wheather to add or replace.
		eyes = new BufferedReader(new FileReader(f));
		String l;
		boolean shouldAppend = true;
		ArrayList<String> contents = new ArrayList<String>();

		while((l = eyes.readLine()) != null){
			String sArray = l.split(":")[0];
			if(sArray.equalsIgnoreCase(attribute)){
				contents.add(sArray + ":" + data);
			} else {
				contents.add(l);
			}
		}

		contents.add(shouldAppend ? l : "");

		//Write
		typeWriter = new BufferedWriter(new FileWriter(f, false));

		for(String c : contents){
			typeWriter.write(c);
		}

		//Clean up some stuff.
		eyes.close();
		typeWriter.close();
	}
	
	public void deleteAttributeData(String attribute, File f) throws IOException, FileNotFoundException{
		BufferedReader eyes = null;
		BufferedWriter typeWriter = null;

		//Reading & determining if the line is the line to be removed.
		eyes = new BufferedReader(new FileReader(f));
		String l;
		ArrayList<String> contents = new ArrayList<String>();

		while((l = eyes.readLine()) != null){
			String sArray = l.split(":")[0];
			if(!sArray.equalsIgnoreCase(attribute)){
				contents.add(l);
			}
		}
		
		//Write
		typeWriter = new BufferedWriter(new FileWriter(f, false));

		for(String c : contents){
			typeWriter.write(c);
		}

		//Clean up some stuff.
		eyes.close();
		typeWriter.close();
	}
	
	public String getAttributeData(String attribute, File f) throws IOException{
		BufferedReader eyes = null;

		//Reading & return attribute data if it exists in the file.
		eyes = new BufferedReader(new FileReader(f));
		String l;

		while((l = eyes.readLine()) != null){
			String[] sArray = l.split(":");
			if(l.split(":")[0].equalsIgnoreCase(attribute)){
				eyes.close();
				return sArray[1];
			}
		}

		//Clean up some stuff.
		eyes.close();
		return null;
	}
	
}

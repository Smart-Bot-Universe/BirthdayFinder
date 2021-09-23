package edu.kentlake.computerscience.utilities;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Ruvim Slyusar
 * 
 * 	As in the name, this class is used for various different methods with most of them having to do something with files.
 *
 */
public class Utils {
	
	public static void fileToString(String file) throws IOException {
		fileToString(new File(file));
	}
	
	/**
	 * Converts the file into a String
	 */
	public static String fileToString(File file) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String line;
		while((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}
		br.close();
		return sb.toString();
	}
	
	public static void writeStringToFile(String[] data, String location) throws IOException {
		writeStringToFile(data, new File(location));
	}
	
	public static void writeStringToFile(String[] data, File location) throws IOException {
		String combined = "";
		for(String s : data) {
			combined += s;
		}
		writeStringToFile(combined, location);
	}
	
	/**
	 *  Writes the string onto a file and makes the file where you specified
	 *  
	 *  @param location : location to where file is written too
	 *  @param data : the data to be written in the file
	 */
	public static void writeStringToFile(String data, File location) throws IOException {
		FileOutputStream fos = new FileOutputStream(location);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		
		byte[] bytes = data.getBytes();
		
		bos.write(bytes);
		bos.close();
		fos.close();
	}
	
	public static boolean makeFolder(File dir) {
		if(dir.exists()) return true;
		else return dir.mkdir();
	}
	
	public static String convertStringToJson(String data, String objectName, String[] variableNames) {
		String json = "{";
		
		String[] dataV2 = data.split("\n");
		
		for(int i = 0;i < dataV2.length;i++) {
			String[] line = dataV2[i].split(",");
			
			json += "\"" + objectName + "\": {";
			
			for(int k = 0;k < variableNames.length - 1;k++) {
				json += "\"" + variableNames[k] + "\": \"" + line[k] + "\", "; //Hardcoded to be String
			}
			
			json += "\"" + variableNames[variableNames.length - 1] 
					+ "\": \"" + line[line.length - 1] + "\"}"; //Same here
		}
		
		json += "}";
		
		return json;
	}
	
	public static void addFileToList(File file) {
		
	}
	
	/**
	 * @param directory.
	 * @return A String with all the files (including folders and/or subfolders) that are separated by comas.
	 */
	public static String listEvrythingInDir(File dir) {
		String fileDirs = "";
		fileDirs += dir + ",";
		for(File child : dir.listFiles()) {
			if(child.isDirectory()) {
				fileDirs += listFilesInDir(dir) + ",";
			}else if(child.isFile()) {
				fileDirs += child + ",";
			}
		}
		return fileDirs;
	}
	
	/**
	 * @param directory.
	 * @return A String with all the files (not folders and/or subfolders) that are separated by comas.
	 */
	public static String listFilesInDir(File dir) {
		String fileDirs = "";
		for(File child : dir.listFiles()) {
			if(child.isDirectory()) {
				fileDirs += listFilesInDir(dir) + ",";
			}else if(child.isFile()) {
				fileDirs += child + ",";
			}
		}
		return fileDirs;
	}
}

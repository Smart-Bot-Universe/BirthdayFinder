package edu.kentlake.computerscience.utilities;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ruvim Slyusar
 */

public class Utils {
	
	/**
	 * @author Ruvim Slyusar 
	 */
	public static void fileToString(String file) throws IOException {
		fileToString(new File(file));
	}
	
	/**
	 * @author Ruvim Slyusar
	 * 
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
	
	/**
	 * @author Ruvim Slyusar
	 */
	public static void writeStringToFile(String[] data, String location) throws IOException {
		writeStringToFile(data, new File(location));
	}
	
	/**
	 * @author Ruvim Slyusar 
	 */
	public static void writeStringToFile(String[] data, File location) throws IOException {
		String combined = "";
		for(String s : data) {
			combined += s;
		}
		writeStringToFile(combined, location);
	}
	
	/**
	 * @author Ruvim Slyusar
	 * 
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
	
	/**
	 * @author Ruvim Slyusar 
	 */
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
	
	/**
	 * @author Ruvim Slyusar 
	 */
	public static void makeJson(Class<?> mainClass) {
		String[][] hierarchyInNames = getHierarchyInNames(mainClass);
	}
	
	/**
	 * @author Ruvim Slyusar 
	 */
	private static String[][] getHierarchyInNames(Class<?> clas) {
		List<String[]> hierarchyNames = new ArrayList<>();
		
		for(Field field : clas.getDeclaredFields()) {
			System.out.println(field.getName());
		}
		
		return null;
	}
	
	/**
	 * @author Ruvim Slyusar
	 */
	public static void addFileToList(File file) {
		
	}
}

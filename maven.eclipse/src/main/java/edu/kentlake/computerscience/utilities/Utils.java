package edu.kentlake.computerscience.utilities;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import edu.kentlake.computerscience.database.Database;

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
	
	public static boolean makeFile(File file) {
		try {
			return file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
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
	
	public static void addFile(File file, String folderDir) {
		addFile(file, folderDir, 0);
	}
	
	/**
	 * @author Ruvim Slyusar
	 * 
	 * I feel like a genius
	 */
	private static void addFile(File file, String folderDir, int count) {
		Path src = Path.of(file.toURI());
		Path dest = (count == 0) ? Path.of(new File(folderDir + "/" + file.getName()).toURI()) :
								Path.of(new File(folderDir + "/" + file.getName() + "(" + count + ")").toURI());
		try {
			Files.copy(src, dest);
		}catch (FileAlreadyExistsException e) {
			addFile(file, folderDir, ++count);
			return;
		}catch(IOException io) {
			io.printStackTrace();
		}
	}
	
	public static String getFileName(String fileName) {
		return new File(fileName).getName();
	}
	
	/**
	 * @param directory.
	 * @return A String with all the files (including folders and/or subfolders) that are separated by comas.
	 */
	public static String listEverythingInDir(File dir) {
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
	 * 
	 * @return A String containing all the files (not folders and not files inside subDirectories) inside of a directory separated by comas.
	 */
	public static String listFilesInDir(File dir) {
		String filesInDir = "";
		for(File child : dir.listFiles()) {
			if(child.isFile()) filesInDir += child + ",";
		}
		return filesInDir;
//		return (filesInDir.endsWith(",")) ? filesInDir.substring(0, filesInDir.length() - 1) : filesInDir;
	}
	
	/**
	 * @param directory.
	 * @return A String with all the files (not folders and/or subfolders) that are separated by comas.
	 * 			Including the files inside subDirectories of the dir
	 */
	public static String listFiles(File dir) {
		String files = "";
		for(File child : dir.listFiles()) {
			if(child.isDirectory()) {
				files += listFiles(child);
			}else if(child.isFile()) {
				files += child + ",";
			}
		}
		return files;
//		return (files.endsWith(",")) ? files.substring(0, files.length() - 1) : files;
	}
	
	/**
	 * @return A String with all the directories (including itself) separated by comas.
	 */
	public static String listDirs(File dir) {
		String dirs = (dir.isDirectory()) ? dir.getPath() + "," : "";
		for(File child : dir.listFiles()) {
			if(child.isDirectory()) dirs += listDirs(child);
		}
		return dirs;
//		return (dirs.endsWith(",")) ? dirs.substring(0, dirs.length() - 1) : dirs;
	}
}

package edu.kentlake.computerscience.database.filestorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Ruvim Slyusar
 *
 *	This class should and is meant to only store the different data types of one file.
 *
 */
public class FileDataType {
	public static final String REAL_FILE = "REAL_FILE";
	public static final String PRETTY_FILE = "PRETTY_FILE";
	
	private Map<String, String> storage;
	private boolean hasPrettyFile = false;
	
	public FileDataType() {
		storage = new HashMap<>();	
	}
	
	public String get(String fileDataType) {
//		System.out.println("ACTION: GET, TYPE: FileData, FILEDATATYPE: " + fileDataType); // Logs
		return storage.get(fileDataType);
	}
	
	public void put(String fileDataType, String fileData) {
		if(fileDataType.equals(PRETTY_FILE)) hasPrettyFile = true;
		storage.put(fileDataType, fileData);
//		System.out.println("ACTION: PUT, TYPE: FileData, SIZE: " + storage.size() + ", FILEDATATYPE: " + fileDataType + ", FILEDATA: " + fileData); // Logs
	}
	
	public boolean hasPrettyFile() {
		return hasPrettyFile;
	}
	
	public Set<String> getKeys(){
		return storage.keySet();
	}
	
	// Debugging stuff
	public void printlnKeys() {
		for(String key : storage.keySet()) {
			System.out.println(key);
		}
	}
	
	public void printlnValues() {
		for(String value : storage.values()) {
			System.out.println(value);
		}
	}
}

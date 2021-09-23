package edu.kentlake.computerscience.database.filestorage;

import java.util.HashMap;
import java.util.Map;

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
		return storage.get(fileDataType);
	}
	
	public void put(String fileDataType, String fileData) {
		if(fileDataType.equals(PRETTY_FILE)) hasPrettyFile = true;
		storage.put(fileDataType, fileData);
	}
	
	public boolean hasPrettyFile() {
		return hasPrettyFile;
	}
}

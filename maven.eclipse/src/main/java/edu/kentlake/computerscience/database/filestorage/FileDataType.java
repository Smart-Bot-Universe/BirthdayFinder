package edu.kentlake.computerscience.database.filestorage;

import java.util.HashMap;
import java.util.Map;

public class FileDataType {
	public static final String REAL_FILE = "REAL_FILE";
	public static final String PRETTY_FILE = "PRETTY_FILE";
	
	private Map<String, String> storage;
	
	public FileDataType() {
		storage = new HashMap<>();	
	}
	
	public String get(String fileDataType) {
		return storage.get(fileDataType);
	}
	
	public void put(String fileDataType, String fileData) {
		storage.put(fileDataType, fileData);
	}
}

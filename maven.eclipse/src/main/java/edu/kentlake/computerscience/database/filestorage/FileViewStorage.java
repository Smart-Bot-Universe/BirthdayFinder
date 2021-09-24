package edu.kentlake.computerscience.database.filestorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Ruvim Slyusar
 *
 *	This class is used to store the file directory and the FileDataType for each file.
 *						HashMap<fileDirectory, FileDataType>();
 */
public class FileViewStorage {

	private Map<String, FileDataType> fileViewStorage;
	
	public FileViewStorage() {
		fileViewStorage = new HashMap<>();
	}
	
	public void put(String fileName, FileDataType fileDataAndType) {
		fileViewStorage.put(fileName, fileDataAndType);
//		System.out.println("ACTION: PUT, TYPE: FileView, SIZE: " + fileViewStorage.size() + ", FILENAME: " + fileName + ", FILEDATATYPE: " + fileDataAndType); // Logs
	}
	
	public void putAll(Map<String, FileDataType> files) {
		fileViewStorage.putAll(files);
	}
	
	public void removeFileData(String fileDir) {
		fileViewStorage.remove(fileDir);
	}
	
	public FileDataType get(String fileDir) {
//		System.out.println("ACTION: GET, TYPE: FileView, FILEDIR: " + fileDir); // Logs
		return fileViewStorage.get(fileDir);
	}
	
	public Set<String> getKeys() {
		return fileViewStorage.keySet();
	}
	
	// Debugging stuff
	public void printlnKeys() {
		for(String key : fileViewStorage.keySet()) {
			System.out.println(key);
		}
	}
	
	public void printlnValueKeys() {
		for(FileDataType value : fileViewStorage.values()) {
			value.printlnKeys();
		}
	}
}
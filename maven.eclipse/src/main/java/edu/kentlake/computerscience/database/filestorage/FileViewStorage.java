package edu.kentlake.computerscience.database.filestorage;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Ruvim Slyusar
 *
 * This class is made for only the viewing part of a file that the user will see
 * For example for memory storage reasoning the file which is in json format will be all written onto one line which doesn't look pretty.
 * which this class is used to store the pretty version of a json file.
 */
public class FileViewStorage {

	private Map<String, FileDataType> fileViewStorage;
	
	public FileViewStorage() {
		fileViewStorage = new HashMap<>();
	}
	
	public void addFileData(String fileName, FileDataType fileDataAndType) {
		fileViewStorage.put(fileName, fileDataAndType);
	}
	
	public void removeFileData(String fileName) {
		fileViewStorage.remove(fileName);
	}
	
	public FileDataType getFileData(String fileName) {
		return fileViewStorage.get(fileName);
	}
}
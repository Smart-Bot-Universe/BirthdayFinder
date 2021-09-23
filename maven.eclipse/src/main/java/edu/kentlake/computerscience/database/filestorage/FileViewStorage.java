package edu.kentlake.computerscience.database.filestorage;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Ruvim Slyusar
 *
 *	This class is used to store the file directory and the FileDataType for each file.
 *						HashMap<fileDirectory, FileDataType>();
 *
 */
public class FileViewStorage {

	private Map<String, FileDataType> fileViewStorage;
	
	public FileViewStorage() {
		fileViewStorage = new HashMap<>();
	}
	
	public void put(String fileName, FileDataType fileDataAndType) {
		fileViewStorage.put(fileName, fileDataAndType);
	}
	
	public void putAll(Map<String, FileDataType> files) {
		fileViewStorage.putAll(files);
	}
	
	public void removeFileData(String fileDir) {
		fileViewStorage.remove(fileDir);
	}
	
	public FileDataType get(String fileDir) {
		return fileViewStorage.get(fileDir);
	}
}
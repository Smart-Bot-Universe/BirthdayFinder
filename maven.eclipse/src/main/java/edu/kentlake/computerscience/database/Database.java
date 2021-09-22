package edu.kentlake.computerscience.database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.kentlake.computerscience.classroom.Classroom;
import edu.kentlake.computerscience.classroom.Student;
import edu.kentlake.computerscience.database.filestorage.FileDataType;
import edu.kentlake.computerscience.database.filestorage.FileViewStorage;
import edu.kentlake.computerscience.utilities.Utils;

/**
 * @author Ruvim Slyusar
 */

public class Database {
	
	List<Student> students;
	File sourceFolder;
	
	private FileViewStorage fileViewStorage;
	
	public Database(String sourceFolder) {
		this(new File(sourceFolder));
	}
	
	public Database(File sourceFolder) {
		this.sourceFolder = sourceFolder;
		students = new ArrayList<>();
		try {
			fileViewStorage = new Gson().fromJson(Utils.fileToString(new File("configuration/fileViewStorage.json")), FileViewStorage.class);
			
			if(checkForChangedFolder(getSourceFolderFiles(), 
					Utils.fileToString(new File("configuration/filesInSourceFolder.txt")).split("\n"))) { restoreDatabase(); }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author Ruvim Slyusar
	 * 
	 * If the source folder was detected to change, update the files by combining the data and turning them into a json file
	 * 
	 * @throws IOException
	 */
	private void restoreDatabase() throws IOException {
		updateFileList(getSourceFolderFiles());
		String[] allData = readAllFilesAndStore(getSourceFolderFiles());
		
		ObjectCreation ob = new ObjectCreation();
		ob.setDataStructure("last, first_MI, birthDate");
		
		String ultimateJson = "";
		String prettyUltimateJson = "";
		
		for(int i = 0;i < allData.length;i++) {
			String[] fileData = sortData(allData[i], 6); // fileData[0] is data and fileData[1] is 'junk'
			fileData[0] = fileData[0].replaceAll("\"", ""); // This is to remove the " in the file in some areas like the student name "Slyusar, Ruvim V"
			
			int period = Integer.parseInt(fileData[1].substring(fileData[1].indexOf("Period: ") + 8, fileData[1].indexOf("Period: ") + 9));
			
			for(String singleObjectData : fileData[0].split("\n")) { // Creates an object Student for every student in one file (classroom) at a time
				if(singleObjectData.isBlank()) continue;
				students.add(ob.createObject(singleObjectData, Student.class));
			}
			GsonBuilder gb = new GsonBuilder();
			Classroom classroom = new Classroom(students, period);
			
			ultimateJson += gb.create().toJson(classroom) + "\n";	
			gb.setPrettyPrinting();
			prettyUltimateJson += gb.create().toJson(classroom) + "\n";
			students.removeAll(students);
		}
		
		FileDataType fileDataType = new FileDataType();
		fileDataType.put(FileDataType.REAL_FILE, ultimateJson);
		fileDataType.put(FileDataType.PRETTY_FILE, prettyUltimateJson);
		
		fileViewStorage.addFileData("combinedFiles.txt", fileDataType);
		Utils.writeStringToFile(new Gson().toJson(fileViewStorage), new File("configuration/fileViewStorage.json"));
	}

	/**
	 * 
	 * @param folderFiles
	 * @return String[0] is the data your looking for while String[1] is the leftovers
 	 * @throws IOException
	 */
	private String[] readAllFilesAndStore(File[] folderFiles) throws IOException {
		String[] dataOfAllFiles = new String[folderFiles.length];
		FileDataType fileDataType = new FileDataType();
		for(int i = 0;i < dataOfAllFiles.length;i++) {
			dataOfAllFiles[i] = Utils.fileToString(folderFiles[i]);
			fileDataType = new FileDataType(); 
			fileDataType.put(FileDataType.REAL_FILE, dataOfAllFiles[i]);
			fileDataType.put(FileDataType.PRETTY_FILE, dataOfAllFiles[i]);
			fileViewStorage.addFileData(folderFiles[i].getName(), fileDataType);
		}
		return dataOfAllFiles;
	}
	
	/**
	 * 
	 * @param data
	 * @param line where to split the data
	 * @return String[0] is the data and String[1] is the leftovers
	 */
	private String[] sortData(String data, int line) {
		String[] oneStringForEachLine = data.split("\n");
		String[] splittedByLine = data.split(oneStringForEachLine[line]);
		
		return new String[] {splittedByLine[1], splittedByLine[0]};
	}
	
	/**
	 * @author Ruvim Slyusar
	 * 
	 * @param folderFiles : copy all the files inside of the source folder onto a file to which will be used the next time this
	 * 								program runs to check if the source folder has changed
	 * @throws IOException 
	 */
	private void updateFileList(File[] folderFiles) throws IOException {
		String fileNames = "";
		for(File child : folderFiles) {
			if(checkForIllegalName(child.getName())) continue;
			fileNames += child.getName() + "\n";
		}
		Utils.writeStringToFile(fileNames, new File("configuration/filesInSourceFolder.txt"));
	}

	/**
	 * @author Ruvim Slyusar
	 * 
	 * @param folder : the folder which it will check all the files in it
	 * @param filesToCheckWith : the previous set of files
	 * @return if the sourceFolder has changed
	 */
	private boolean checkForChangedFolder(File[] folderFiles, String[] filesToCheckWith) {
		for(int i = 0;i < folderFiles.length;i++) {
			File child = folderFiles[i];
			
			if(!child.getName().equals(filesToCheckWith[i])) {
				if(checkForIllegalName(child.getName())) continue;
				return true;
			}
		}
		return false;
	}	
	
	/**
	 * @author Ruvim Slyusar
	 * 
	 * @param fileName : the file that your checking if its used by the program
	 * @return if the fileName is reserved for the program
	 */
	private boolean checkForIllegalName(String fileName) {
		switch(fileName) {
		case "combinedFiles.txt": return true;
		case "filesInSourceFolder.txt": return true;
		case "fileViewStorage.json": return true;
		default: return false;
		}
	}

	/**
	 * @author Ruvim Slyusar
	 * 
	 * @return all the source folder files
	 */
	public File[] getSourceFolderFiles() {
		return sourceFolder.listFiles();
	}
	
	/**
	 * @author Ruvim Slyusar
	 * 
	 * In general this is how you would request to receive the data inside of a file
	 * 
	 * @param fileName
	 * @param fileDataType
	 * @return a String containing the fileData
	 */
	public String getFileData(String fileName, String fileDataType) {
		return fileViewStorage.getFileData(fileName).get(fileDataType);
	}
}

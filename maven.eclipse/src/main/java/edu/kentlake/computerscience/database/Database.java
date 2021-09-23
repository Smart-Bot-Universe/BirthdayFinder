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
import edu.kentlake.computerscience.utilities.FileContentSplitter;
import edu.kentlake.computerscience.utilities.Utils;

/**
 * @author Ruvim Slyusar
 */
public class Database {
	public static final String RESOURCES_DIR = "src/main/resources";
	public static final String USER_FILES_DIR = RESOURCES_DIR + "UserFiles";
	public static final String CONFIG_DIR = RESOURCES_DIR + "configuration";
	
	List<Student> students;
	
	private FileViewStorage fileViewStorage;
	
	public Database() {
		students = new ArrayList<>();
		
		try {
			fileViewStorage = new Gson().fromJson(Utils.fileToString(new File(CONFIG_DIR + "/fileViewStorage.json")), FileViewStorage.class);
			
			if(checkForChangesInDir(new File(USER_FILES_DIR), new File(CONFIG_DIR + "/filesInUserFiles.txt"))) { restoreDatabase(); }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * If the source folder was detected to change, update the files by combining the data and turning them into a json file
	 * 
	 * @throws IOException
	 */
	@Deprecated
	private void restoreDatabase() throws IOException {
		updateFileList(new File(USER_FILES_DIR));
		readAllFilesAndStore(new File(USER_FILES_DIR));
		
		ObjectCreation ob = new ObjectCreation();
		ob.setDataStructure("last, first_MI, birthDate"); // Hardcoded :( and will be until I figure out in-memory java compiler
		
		String ultimateJson = "";
		String prettyUltimateJson = "";
		
		String[] allUserFilesDirs = getFileList().split(",");
		for(int i = 0;i < allUserFilesDirs.length;i++) {
			String fileData = fileViewStorage.get(allUserFilesDirs[i]).get(FileDataType.REAL_FILE);
			FileContentSplitter fcp = new FileContentSplitter(fileData);
			String[] fileContent = fcp.splitContent(7);
			fileContent[1] = fileContent[1].replaceAll("\"", "");
			
			int period = Integer.parseInt(fileContent[0].substring(fileContent[0].indexOf("Period: ") + 8, fileContent[0].indexOf("Period: ") + 9));
			
			for(String singleObjectData : fileContent[0].split("\n")) { // Creates an object Student for every student in one file (classroom) at a time
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
		Utils.writeStringToFile(new Gson().toJson(fileViewStorage), new File(CONFIG_DIR + "/fileViewStorage.json"));
	}

	/**
	 * This method reads all of the files in the dir and then stores it into the fileViewStorage as a REAL_FILE.
	 * To which then can be accessed anywhere in this class.
	 * 
	 * @param dir
	 * @throws IOException
	 */
	private void readAllFilesAndStore(File dir) throws IOException {
		String[] fileDirs = getFileList().split(",");
		
		for(int i = 0;i < fileDirs.length;i++) {
			String fileData = Utils.fileToString(new File(fileDirs[i]));
			FileDataType fdt = new FileDataType();

			fdt.put(FileDataType.REAL_FILE, fileData);
			fileViewStorage.put(fileDirs[i], fdt);
		}
	}
	
	/**
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
	 * This method updates the src/main/resources/filesInUserFiles.txt
	 * 
	 * @throws IOException 
	 */
	private void updateFileList(File dir) throws IOException {
		String fileNames = Utils.listFilesInDir(dir);
		Utils.writeStringToFile(fileNames, new File(CONFIG_DIR + "/filesInUserFiles.txt"));
	}
	
	private String getFileList() throws IOException {
		return Utils.fileToString(new File(CONFIG_DIR + "/filesInUserFiles.txt"));
	}

	/**
	 * @param folder : the folder which it will check all the files in it
	 * @param filesToCheckWith : the previous set of files
	 * @return if the sourceFolder has changed
	 * @throws IOException 
	 */
	private boolean checkForChangesInDir(File mainDir, File previousMainDir) throws IOException {
		String mainDirFiles = Utils.listFilesInDir(mainDir);
		String previousDirFiles = Utils.fileToString(previousMainDir);
		
		return mainDirFiles.equals(previousDirFiles);
	}
	
	/**
	 * @param fileName : the file that your checking if its used by the program
	 * @return if the fileName is reserved for the program
	 */
	@Deprecated
	private boolean checkForIllegalFile(String fileName) { // Will prob have to add a couple to the list :) // Lol this not needed if I use directory not name
		switch(fileName) {
		case "/combinedFiles.txt": return true; // uhhhhh idk man prob remove this
		case CONFIG_DIR + "/filesInSourceFolder.txt": return true;
		case CONFIG_DIR + "/fileViewStorage.json": return true;
		default: return false;
		}
	}

	/**
	 * @return all the Users files
	 */
	public File[] getUserFilesDirFiles() {
		return new File(USER_FILES_DIR).listFiles();
	}
	
	/**
	 * In general this is how you would request to receive the data inside of a file
	 * 
	 * @param fileName
	 * @param fileDataType
	 * @return a String containing the fileData
	 */
	public String getFileData(String fileName, String fileDataType) {
		FileDataType fdt = fileViewStorage.get(fileName);
		if(fileDataType.equals(FileDataType.PRETTY_FILE) && fdt.hasPrettyFile()) {
			return fdt.get(fileDataType);
		}else if(fileDataType.equals(FileDataType.PRETTY_FILE) && !fdt.hasPrettyFile()) {
			// When I get logs I will log if there was a request for a PRETTY_FILE, and there was no PRETTY_FILE
			return fdt.get(FileDataType.REAL_FILE);
		}
		return fdt.get(fileDataType);
	}
}

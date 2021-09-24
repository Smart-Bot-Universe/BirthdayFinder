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
	public static final String USER_FILES_DIR = RESOURCES_DIR + "/UserFiles";
	public static final String CONFIG_DIR = RESOURCES_DIR + "/configuration";
	
	List<Student> students;
	
	private FileViewStorage fileViewStorage;
	
	// List of all the files directory from the previous time this program runned.
	private String fileList;
	
	public Database() {
		try {
			students = new ArrayList<>();
			fileList = Utils.fileToString(new File(CONFIG_DIR + "/filesInUserFiles.txt"));
			
//			fileViewStorage = new Gson().fromJson(Utils.fileToString(new File(CONFIG_DIR + "/fileViewStorage.json")), FileViewStorage.class);
			fileViewStorage = new FileViewStorage();
			
			restoreDatabase();
//			if(checkForChangesInDir(new File(USER_FILES_DIR))) { restoreDatabase(); }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * If the source folder was detected to change, update the files by combining the data and turning them into a json file
	 * 
	 * @throws IOException
	 */
	private void restoreDatabase() throws IOException {
		updateFileList(new File(USER_FILES_DIR));
		readAllFilesAndStore(new File(USER_FILES_DIR));
		
		ObjectCreation ob = new ObjectCreation();
		ob.setDataStructure("last, first_MI, birthDate"); // Hard coded :( and will be until I figure out in-memory java compiler
		
		String ultimateJson = "";
		String prettyUltimateJson = "";
		
		String[] allDirsInUserFiles = Utils.listDirs(new File(USER_FILES_DIR)).split(",");
		for(int i = 0;i < allDirsInUserFiles.length;i++) { // Dir
			String[] filesInDir = Utils.listFilesInDir(new File(allDirsInUserFiles[i])).split(",");
			
			for(int n = 0;n < filesInDir.length;n++) { // Files in Dir
				if(filesInDir[n].isBlank()) continue; if(isIllegalFileName(Utils.getFileName(filesInDir[n]))) continue;
				
				String fileData = fileViewStorage.get(filesInDir[n]).get(FileDataType.REAL_FILE);
				FileContentSplitter fcp = new FileContentSplitter(fileData);
				String[] fileContent = fcp.splitContent(7);
				fileContent[1] = fileContent[1].replaceAll("\"", "");
				
				// Hard coded in the sense that not every file has a "Period: "
				int period = Integer.parseInt(fileContent[0].substring(fileContent[0].indexOf("Period: ") + 8, fileContent[0].indexOf("Period: ") + 9));
				
				for(String singleObjectData : fileContent[1].split("\n")) { // Creates an object Student for every student in one file (classroom) at a time
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
			
			fileViewStorage.put(new File(allDirsInUserFiles[i] + "/combinedFiles.txt").getPath(), fileDataType);
			Utils.writeStringToFile(fileDataType.get(FileDataType.REAL_FILE), new File(allDirsInUserFiles[i] + "/combinedFiles.txt"));
		}
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
		String[] fileDirs = fileList.split(",");
		
		for(int i = 0;i < fileDirs.length;i++) {
			if(fileDirs[i].isBlank()) continue;
			if(isIllegalFileName(Utils.getFileName(fileDirs[i]))) continue;
			
			String fileData = Utils.fileToString(new File(fileDirs[i]));
			FileDataType fdt;
//			try { fdt = fileViewStorage.get(fileDirs[i]); }catch(Exception e) { fdt = new FileDataType(); } // To not overwrite // Idk if it works
			fdt = new FileDataType();
			fdt.put(FileDataType.REAL_FILE, fileData);
			fileViewStorage.put(fileDirs[i], fdt);
		}
	}
	
	/**
	 * This method updates the src/main/resources/filesInUserFiles.txt
	 * 
	 * @throws IOException 
	 */
	private void updateFileList(File dir) throws IOException {
		String fileNames = Utils.listFiles(dir);
		Utils.writeStringToFile(fileNames, new File(CONFIG_DIR + "/filesInUserFiles.txt"));
	}
	
	/**
	 * @return A String version of the file src/main/resources/configuration/filesInUserFiles.txt
	 */
	public String getFileList() {
		return fileList;
	}
	
	/**
	 * @return all the Users files
	 */
	public File[] getUserFilesDirFiles() {
		return new File(USER_FILES_DIR).listFiles();
	}
	
	public boolean isIllegalFileName(String fileName) {
		switch(fileName) {
		case "combinedFiles.txt": return true;
			default: return false;
		}
	}

	/**
	 * @param folder : the folder which it will check all the files in it
	 * @param filesToCheckWith : the previous set of files
	 * @return if the sourceFolder has changed
	 * @throws IOException 
	 */
	private boolean checkForChangesInDir(File mainDir) throws IOException {
		String mainDirFiles = Utils.listFilesInDir(mainDir); // Might need to change
		
		return mainDirFiles.equals(fileList);
	}
	
	/**
	 * In general this is how you would request to receive the data inside of a file
	 * 
	 * @param fileDir
	 * @param fileDataType
	 * @return a String containing the fileData
	 */
	public String getFileData(String fileDir, String fileDataType) {
//		String[] fileDirs = fileList.split(",");
//		System.out.println(fileViewStorage.get(fileDirs[1]).get(FileDataType.REAL_FILE));
		
		FileDataType fdt = fileViewStorage.get(fileDir);
//		System.out.println(fdt.get(FileDataType.PRETTY_FILE));
		if(fileDataType.equals(FileDataType.PRETTY_FILE) && fdt.hasPrettyFile()) {
			return fdt.get(fileDataType);
		}else if(fileDataType.equals(FileDataType.PRETTY_FILE) && !fdt.hasPrettyFile()) {
			// When I get logs I will log if there was a request for a PRETTY_FILE, and there was no PRETTY_FILE
			return fdt.get(FileDataType.REAL_FILE);
		}
		return fdt.get(fileDataType);
	}
}

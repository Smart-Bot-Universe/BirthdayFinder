package edu.kentlake.computerscience.database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.kentlake.computerscience.classroom.Student;
import edu.kentlake.computerscience.utilities.Utils;

/**
 * @author Ruvim Slyusar
 */

public class Database {
	
	List<Student> students = new ArrayList<>();
	File sourceFolder;
	
	public Database(String sourceFolder) {
		this(new File(sourceFolder));
	}
	
	public Database(File sourceFolder) {
		this.sourceFolder = sourceFolder;
		
		try {
			if(checkForChangedFolder(getSourceFolderFiles(), 
					Utils.fileToString(new File("configuration/filesInSourceFolder.txt")).split("\n"))) restoreDatabase();
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
		String[] unfilteredData = readAndCombineFiles(getSourceFolderFiles());
		
		//Filter data and then send it to objectCreation /////2/2/2/2/12/213/423/534/63/634
		
//			String[] filteredData = unfilteredData.split("\"Last, First MI\",Birth Date,,,,,");
//			for(String s : filteredData[0].split(",")) {
//				if(s.contains("Period")) period = s;
//			}
//			data += period + "\n" + filteredData[1];
//		Utils.writeStringToFile(data, new File("files/combinedFiles.txt"));
//		Utils.writeStringToFile(fileNames, new File("configuration/filesInSourceFolder.txt"));
	}
	
	private String[] readAndCombineFiles(File[] sourceFolderFiles) throws IOException {
		String[] combinedFiles = new String[sourceFolderFiles.length];
		for(int i = 0;i < combinedFiles.length;i++) {
			combinedFiles[i] = Utils.fileToString(sourceFolderFiles[i]);
		}
		return combinedFiles;
	}
	
	/**
	 * @author Ruvim Slyusar
	 * 
	 * @param sourceFolderFiles : copy all the files inside of the source folder onto a file to which will be used the next time this
	 * 								program runs to check if the source folder has changed
	 * @throws IOException 
	 */
	private void updateFileList(File[] sourceFolderFiles) throws IOException {
		String fileNames = "";
		for(File child : sourceFolderFiles) {
			if(checkForIllegalName(child.getName())) continue;
			fileNames += child.getName() + "\n";
		}
		Utils.writeStringToFile(fileNames, new File("configuration/filesInSourceFolder.txt"));
	}

	/**
	 * @author Ruvim Slyusar
	 * 
	 * @param sourceFolder : the folder which it will check all the files in it
	 * @param filesToCheckWith : the previous set of files
	 * @return if the sourceFolder has changed
	 */
	private boolean checkForChangedFolder(File[] sourceFolder, String[] filesToCheckWith) {
		for(int i = 0;i < getSourceFolderFiles().length;i++) {
			File child = getSourceFolderFiles()[i];
			
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
}

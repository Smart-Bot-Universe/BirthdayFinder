package program.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utils {

	public static String fileToString(File file) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String line;
		while((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}
		br.close();
		return sb.toString();
	}
	
	public static void addFile(File file, String folderDir) {
		
		
		Path src = Path.of(file.toURI());
		Path dest = Path.of(new File(folderDir).toURI());
		try {
			Files.copy(src, dest);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean delete(File file) {
		if(file.exists()) return file.delete();
		else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return file.delete();
		}
	}
}

package edu.kentlake.computerscience;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import edu.kentlake.computerscience.application.App;
import edu.kentlake.computerscience.classroom.Student;
import edu.kentlake.computerscience.utilities.Utils;
import javafx.application.Application;

public class Main {

//	public static void main(String[] args) {
//		try {
//			String fileData = Utils.fileToString(new File("files/wow.txt"));			
//			String[] arrayOfFileData = Arrays.copyOfRange(fileData.split("\n"), 7, fileData.split("\n").length);
//			
//			String fileDataV2 = "";
//			for(String s : arrayOfFileData) fileDataV2 += s + "\n";
//			fileDataV2 = fileDataV2.replace("\"", "");
//			
//			String json = Utils.convertStringToJson(fileDataV2, "Student",
//					new String[] {"last", "first", "middleInitials", "dateOfBirth"});
//			
//			String[] eachObject = json.split("\"Student\": ");
//			
//			Gson gson = new Gson();
//			
//			List<Student> students = new ArrayList<Student>();
//			
//			for(int i = 1;i < eachObject.length - 1;i++) {
//				students.add(gson.fromJson(eachObject[i], Student.class));;
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void main(String[] args) {
		Application.launch(App.class, args);
	}
}
package edu.kentlake.computerscience.database;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class ObjectCreation {

	String[] dataStructure;
	
	/**
	 * @author Ruvim Slyusar
	 * 
	 * @param values : the file that has all the data you want to sort to an object
	 * @param object : the type of object you want
	 * @return
	 */
	public <T> T createObject(String objectData, Class<T> objectClass) {
//		Map<String, Map<Object, String>> varsAndValues = new HashMap<>(); // ???
		Object[] dataType = new Object[dataStructure.length];
		
		// Lol it was called createObject before and yet I coded it like I'm using the whole file *facepalm* // Of course I fixed it so you won't see it :) 
		// It was just one for loop too so eeeh you're not missing out on a bunch // ArrayList makes it seem slow to me so arrays it is
		String json = "";
		try {
			String[] sortedByComas = objectData.split(",");
			for(int i = 0;i < sortedByComas.length;i++) {
				dataType[i] = (Object)objectClass.getDeclaredField(dataStructure[i]).getType(); // Null stuff fix needed
			}
			json = createSingleObjectJson(objectClass.getSimpleName(), dataStructure, dataType, sortedByComas);
		}catch(Exception e) {
			e.printStackTrace();
		}
		Gson gson = new Gson();
		return gson.fromJson(json, objectClass);
	}
	
	/**
	 * @author Ruvim Slyusar
	 * 
	 * This method is used for finding the data type like int because int simply isn't a class so we have to box
	 * it to its class Integer to find out
	 * 
	 * @param dataType : object that your looking to find the data type
	 * @return enum DataType that classifies what data type it is
	 */
	public DataType getBoxedPrimitiveDataType(Object dataType) {
		switch(dataType.toString()) {
		case "double": return DataType.NUMBER;
		case "float": return DataType.NUMBER;
		case "long": return DataType.NUMBER;
		case "int": return DataType.NUMBER;
		case "short": return DataType.NUMBER;
		case "byte": return DataType.NUMBER;
		case "class java.lang.String": return DataType.WORD;
		case "char": return DataType.WORD;
		case "boolean": return DataType.BOOLEAN;
		default: throw new IllegalArgumentException("Unknown data type - " + dataType.toString());
		}
	}
	
	
	/**
	 * @author Ruvim Slyusar
	 * 
	 * This creates a json for later use to use Gson to convert it to an object.
	 * 
	 * @param objectName : the class name
	 * @param varsWithValue: a map thats sorted by variableName:valueOfVariable
	 * @return
	 */
	private String createSingleObjectJson(String objectName, String[] variableName, Object[] dataType, String[] dataForVariable) {
		// Not dealing with Map<String, Map<Object, String>> varsWithValue;
		String json = "{";
		for(int i = 0;i < variableName.length;i++){
			
			json += variableName[i] + ":";
			switch(getBoxedPrimitiveDataType(dataType[i])) {
			case NUMBER: json += dataForVariable[i] + ","; break;
			case WORD: json += "\"" + dataForVariable[i] + "\","; break;
			case BOOLEAN: json += dataForVariable[i] + ","; break;
				default: break;
			}
		}
		json = json.substring(0, json.length() - 1); json += "}";
		return json; // Return them json string and yes this method is private because I simply don't want to touch the arrow keys to select the right method
	}

	/**
	 * @author Ruvim Slyusar
	 * 
	 * @param dataStructure is the format of how data in the file is sorted to be inside of an object
	 */
	public void setDataStructure(String dataStructure) {
		this.dataStructure = dataStructure.replaceAll(" ", "").split(","); // There's a reason for that replaceAll()
	}
}

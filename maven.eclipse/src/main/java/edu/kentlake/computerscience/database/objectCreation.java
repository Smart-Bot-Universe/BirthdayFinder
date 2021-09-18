package edu.kentlake.computerscience.database;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class objectCreation {

	String[] dataStructure;
	
	public objectCreation() {
		
	}
	
	/**
	 * @author Ruvim Slyusar
	 * 
	 * @param values : the file that has all the data you want to sort to an object
	 * @param object : the type of object you want
	 * @return
	 */
	public Class<?> createObject(String values, Class<?> object) {
//		try {
//			Class<?> obj = Class.forName(object.getName());
//			
//			String[] splittedValues = values.split(",");
//			
//			for(int i = 0;i < dataStructure.length;i++) {
//				Field variable = obj.getField(dataStructure[i]);
//				Type type = obj.getField(dataStructure[i]).getGenericType();
//				
//				String value = splittedValues[i];
//				switch(type.getTypeName()){
//				case "double": variable.setDouble(obj, Double.parseDouble(value)); break;
//				case "float": variable.setFloat(obj, Float.parseFloat(value)); break;
//				case "long": variable.setLong(obj, Long.parseLong(value)); break;
//				case "int": variable.setInt(obj, Integer.parseInt(value)); break;
//				case "short": variable.setShort(obj, Short.parseShort(value)); break;
//				case "byte": variable.setByte(obj, Byte.parseByte(value)); break;
//				case "String": variable.set(obj, value); break;
//				case "char": variable.setChar(obj, value.toCharArray()[0]); break;
//				case "boolean": variable.setBoolean(obj, value.equals("true") ? true : false); break;
//				default: System.out.println("Error"); continue;
//				}
//			}
//			return obj;
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
		
		Map<String, Object> varsAndValues = new HashMap<>();
		
		try {
			for(String sortedByLines : values.split("\n")) {
				String[] sortedByComas = sortedByLines.split(",");
				for(int i = 0;i < sortedByComas.length;i++) {
					varsAndValues.put(dataStructure[i], (Object)object.getField(sortedByComas[i]));
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		createSingleObjectJson(object.getName(), varsAndValues);
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
		switch(dataType.getClass().getSimpleName()) {
		case "double": return DataType.NUMBER;
		case "float": return DataType.NUMBER;
		case "long": return DataType.NUMBER;
		case "int": return DataType.NUMBER;
		case "short": return DataType.NUMBER;
		case "byte": return DataType.NUMBER;
		case "String": return DataType.WORD;
		case "char": return DataType.WORD;
		case "boolean": return DataType.BOOLEAN;
		default: throw new IllegalArgumentException("Unknown data type");
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
	public String createSingleObjectJson(String objectName, Map<String, Object> varsWithValue) {
		String json = "{" + objectName + ":{";
		for(Map.Entry<String, Object> set : varsWithValue.entrySet()){
			String varName = set.getKey();
			Object varValue = set.getValue();
			switch(getBoxedPrimitiveDataType(varValue)) {
			case NUMBER:
			case WORD:
			case BOOLEAN:
				default: break;
			}
		}
	}

	/**
	 * @author Ruvim Slyusar
	 * 
	 * @param dataStructure is the format of how data in the file is sorted to be inside of an object
	 */
	public void setDataStructure(String dataStructure) {
		this.dataStructure = dataStructure.split(",");
	}
}

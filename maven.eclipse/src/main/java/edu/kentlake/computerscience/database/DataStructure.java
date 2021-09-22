package edu.kentlake.computerscience.database;

import java.util.Objects;

/**
 * @author Ruvim Slyusar
 *
 * This class is for storing and making a hashCode for the dataStructure. 
 * For which the dataStructure is used to detemine the order of how the data inside of a file will be stores onto an object.
 * And possibly other stuff like putting it in a json file.
 */

public class DataStructure {

	String fileFormat;
	boolean isSelected;
	
	public DataStructure() {
		
	}
	
	public DataStructure(String fileFormat) {
		this.fileFormat = fileFormat;
		isSelected = false;
	}
	
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(fileFormat);
	}
}

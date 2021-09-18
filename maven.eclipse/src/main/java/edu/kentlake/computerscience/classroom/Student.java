package edu.kentlake.computerscience.classroom;

public class Student {
	String last;
	String first_MI;
	String dateOfBirth;
	
	public int getDateOfBirth() {
		String[] dates = dateOfBirth.split("/");
		return Integer.parseInt((dates[0] + dates[1] + dates[2]));
	}
}

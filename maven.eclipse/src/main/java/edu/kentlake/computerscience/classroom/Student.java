package edu.kentlake.computerscience.classroom;

public class Student {
	String last;
	String first_MI;
	String birthDate;
	
	public int getDateOfBirth() {
		String[] dates = birthDate.split("/");
		return Integer.parseInt((dates[0] + dates[1] + dates[2]));
	}
	
	@Override
	public String toString() {
		return "last:" + last + " first_MI:" + first_MI + " birthDate:" + birthDate;
	}
}

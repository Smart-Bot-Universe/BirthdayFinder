package program.classroom;

import java.time.Month;

public class Student {
	String last;
	String first_MI;
	String birthDate;
	
	public Month getBirthMonth() {
		return Month.of(Integer.parseInt(birthDate.split("/")[1]));
	}
	
	public String getBirthDate() {
		return birthDate;
	}
	
	public String getLastName() {
		return last;
	}
	
	public String getFirst_MI() {
		return first_MI;
	}
	
	@Override
	public String toString() {
		return "last:" + last + " first_MI:" + first_MI + " birthDate:" + birthDate;
	}
}

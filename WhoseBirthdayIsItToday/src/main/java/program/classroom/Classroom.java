package program.classroom;

import java.util.List;

public class Classroom {
	int period;
	List<Student> students;
	
	public Classroom(List<Student> students, int period) {
		this.students = students;
		this.period = period;
	}
	
	public List<Student> getStudents(){
		return students;
	}
	
	public int getPeriod() {
		return period;
	}
}

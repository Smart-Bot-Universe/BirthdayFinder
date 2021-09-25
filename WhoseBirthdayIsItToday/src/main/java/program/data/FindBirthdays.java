package program.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import program.classroom.Classroom;
import program.classroom.Student;

public class FindBirthdays {

	Map<Integer, Classroom> sortedClassrooms;
	List<TodaysBirthday> allBirthdaysToday;
	
	public FindBirthdays(Map<Integer, Classroom> sortedClassrooms) {
		this.sortedClassrooms = sortedClassrooms;
		allBirthdaysToday = new ArrayList<>();
		
		findThemBirthdays();
	}
	
	public void findThemBirthdays() {
		LocalDate todaysDateObj = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyy");
		String todaysDate = todaysDateObj.format(formatter);
		
		for(Classroom currentClassroom : sortedClassrooms.values()) {
			for(Student currentStudent : currentClassroom.getStudents()) {
				String[] arrOne = currentStudent.getBirthDate().split("/");
				String[] arrTwo = todaysDate.split("/");
				String studentNoYear = arrOne[0] + arrOne[1];
				String todayNoYear = arrTwo[0] + arrTwo[1];
				
				if(studentNoYear.equals(todayNoYear)) allBirthdaysToday.add(new TodaysBirthday(currentStudent, currentClassroom.getPeriod()));
			}
		}
	}
	
	public List<TodaysBirthday> getTodaysBirthdayKids(){
		return allBirthdaysToday;
	}
	
	public class TodaysBirthday {
		private int period;
		private Student birthdayStudent;
		
		public TodaysBirthday(Student birthdayStudent, int period) {
			this.birthdayStudent = birthdayStudent;
			this.period = period;
		}
		
		public int getStudentsPeriodNum() {
			return period;
		}
		
		public Student getBirthdayStudent() {
			return birthdayStudent;
		}
	}
}

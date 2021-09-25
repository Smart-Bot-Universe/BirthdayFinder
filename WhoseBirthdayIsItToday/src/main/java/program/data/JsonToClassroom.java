package program.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import program.classroom.Classroom;
import program.data.FindBirthdays.TodaysBirthday;

public class JsonToClassroom {
	
	private Gson gson;
	private FindBirthdays birthdayFinder;
	
	private List<Classroom> classrooms;
	private Map<Integer, Classroom> sortedClassrooms;

	public JsonToClassroom() {
		classrooms = new ArrayList<>();
		sortedClassrooms = new HashMap<>();
		gson = new Gson();
	}
	
	public void start(String json) {
		String[] classroomsJson = json.split("\n");
		
		for(int i = 0;i < classroomsJson.length;i++) {
			classrooms.add(gson.fromJson(classroomsJson[i], Classroom.class));
		}
		for(Classroom room : classrooms) sortedClassrooms.put(room.getPeriod(), room);
		birthdayFinder = new FindBirthdays(sortedClassrooms);
	}
	
	public List<TodaysBirthday> getTodaysBirthdays() {
		return birthdayFinder.getTodaysBirthdayKids();
	}
}

package support;

public class Slot {
	private String course;
	private String teacherName;
	private String teacherSurname;
	private int day;			// 0: lunedì, 1: martedì, 2: mercoledì, 3: giovedì, 4: venerdì
	private int time;			// 0: 15-16, 1: 16-17,  2: 17-18,    3: 18-19

	public Slot(String course, String teacherName, String teacherSurname, int day, int time) {
		this.course = course;
		this.teacherName = teacherName;
		this.teacherSurname = teacherSurname;
		this.day = day;
		this.time = time;
	}

	public String getCourse() {
		return course;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public String getTeacherSurname() {
		return teacherSurname;
	}

	public int getDay() {
		return day;
	}

	public int getTime() {
		return time;
	}

	@Override
	public String toString() {
		return "Slot{" +
				"course='" + course + '\'' +
				", teacherName='" + teacherName + '\'' +
				", teacherSurname='" + teacherSurname + '\'' +
				", day=" + day +
				", time=" + time +
				'}';
	}
}

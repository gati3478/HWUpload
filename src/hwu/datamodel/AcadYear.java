package hwu.datamodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AcadYear {
	private int fromYear;
	private int toYear;
	private List<Course> courses;

	public AcadYear(int fromYear, int toYear) {
		this.fromYear = fromYear;
		this.toYear = toYear;
		courses = new ArrayList<Course>();
	}

	public int getStartYear() {
		return fromYear;
	}

	public int getEndYear() {
		return toYear;
	}

	public void addCourse(Course course) {
		courses.add(course);
	}

	public List<Course> getCourses() {
		return courses;
	}

}

package hwu.datamodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AcadYear {
	private int year;
	private List<Course> courses;

	public AcadYear(int year) {
		this.year = year;
		courses = new ArrayList<Course>();
	}

	public int getYear() {
		return year;
	}

	public void addCourse(Course course) {
		courses.add(course);
	}

	public Iterator<Course> iterator() {
		return courses.iterator();
	}

}

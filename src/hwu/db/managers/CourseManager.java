package hwu.db.managers;

import java.util.List;

import hwu.datamodel.Course;
import hwu.datamodel.users.Lecturer;
import hwu.datamodel.users.Student;
import hwu.datamodel.users.User;

import javax.sql.DataSource;

public class CourseManager extends Manager {
	public static final String ATTRIBUTE_NAME = "course_manager";

	/**
	 * Constructs CourseManager object with provided DataSource object.
	 * 
	 * @param dataSource
	 *            DataSource object representing connection pool.
	 */
	public CourseManager(DataSource dataSource) {
		super(dataSource);
	}

	public Course getCourse(int id) {
		return null;
	}

	public List<Course> getCourses(Student student) {
		return null;
	}

	public List<Course> getCourses(Lecturer lecturer) {
		return null;
	}

	public List<Course> getAssignedCourses(User tutor) {
		return null;
	}

}

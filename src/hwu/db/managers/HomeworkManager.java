package hwu.db.managers;

import java.util.List;

import hwu.datamodel.Course;
import hwu.datamodel.Homework;
import hwu.datamodel.users.Lecturer;
import hwu.datamodel.users.Student;
import hwu.datamodel.users.User;

import javax.sql.DataSource;

public class HomeworkManager extends Manager {
	public static final String ATTRIBUTE_NAME = "homework_manager";

	/**
	 * Constructs HomeworkManager object with provided DataSource object.
	 * 
	 * @param dataSource
	 *            DataSource object representing connection pool.
	 */
	public HomeworkManager(DataSource dataSource) {
		super(dataSource);
	}

	public Homework getHomework(int id) {
		return null;
	}

	public List<Homework> getHomework(Student student, Course course) {
		return null;
	}

	public List<Homework> getHomework(Lecturer lecturer, Course course) {
		return null;
	}

	public List<Homework> getAssignedHomework(User tutor, Course course) {
		return null;
	}

}

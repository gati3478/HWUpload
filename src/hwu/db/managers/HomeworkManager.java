package hwu.db.managers;

import java.util.List;

import hwu.datamodel.Course;
import hwu.datamodel.Homework;
import hwu.datamodel.Homework.HomeworkForm;
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

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Homework getHomework(int id) {
		return null;
	}

	/**
	 * 
	 * @param homework
	 */
	public void addHomework(Homework homework) {
	}

	/**
	 * 
	 * @param homework
	 */
	public void removeHomework(Homework homework) {
	}

	/**
	 * 
	 * @param homework
	 * @return
	 */
	public List<HomeworkForm> getHomeworkForms(Homework homework) {
		return null;
	}

	/**
	 * 
	 * @param homework
	 * @param form
	 */
	public void addHomeworkForm(Homework homework, HomeworkForm form) {
	}

	/**
	 * 
	 * @param homework
	 * @param form
	 */
	public void removeHomeworkForm(Homework homework, HomeworkForm form) {
	}

	/**
	 * 
	 * @param user
	 * @param course
	 * @return
	 */
	public List<Homework> getHomework(User user, Course course) {
		if (user instanceof Student)
			return getStudentHomework((Student) user, course);
		else if (user instanceof Lecturer)
			return getLecturerHomework((Lecturer) user, course);
		else
			return null;
	}

	/**
	 * 
	 * @param student
	 * @param course
	 * @return
	 */
	public List<Homework> getStudentHomework(Student student, Course course) {
		return null;
	}

	/**
	 * 
	 * @param lecturer
	 * @param course
	 * @return
	 */
	public List<Homework> getLecturerHomework(Lecturer lecturer, Course course) {
		return null;
	}

	/**
	 * 
	 * @param tutor
	 * @param course
	 * @return
	 */
	public List<Homework> getAssignedHomework(User tutor, Course course) {
		return null;
	}

}

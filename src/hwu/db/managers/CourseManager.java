package hwu.db.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import hwu.datamodel.AcadYear;
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
		Course course = null;
		try {
			Connection con = dataSource.getConnection();
			String query = "SELECT * FROM courses WHERE courses.id=" + id;
			PreparedStatement statement = con.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				course = new Course(id, rs.getString("name"),
						rs.getString("description"), rs.getDate("start_date"),
						rs.getDate("end_date"), rs.getInt("latedays_num"),
						rs.getInt("latedays_len"));
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return course;
	}

	public List<Course> getCourses(Student student) {
		List<Course> courses = new ArrayList<Course>();
		try {
			Connection con = dataSource.getConnection();
			String query = "SELECT courses.name, courses.ID "
					+ "FROM (SELECT * FROM course_students WHERE student_id=?) AS cur_student "
					+ "LEFT JOIN courses ON course_id=courses.id "
					+ "WHERE courses.start_date <= NOW() AND courses.end_date >= NOW()";
			PreparedStatement statement = con.prepareStatement(query);
			statement.setInt(1, student.getID());
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				courses.add(new Course(rs.getString(1), rs.getInt(2)));
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return courses;
	}

	@SuppressWarnings("deprecation")
	public List<AcadYear> getCourses(Lecturer lecturer) {
		List<AcadYear> years = new ArrayList<AcadYear>();
		try {
			Connection con = dataSource.getConnection();
			String query = "SELECT courses.name, courses.start_date, courses.ID "
					+ "FROM (SELECT * FROM courses_lecturers WHERE lecturer_id = ?) "
					+ "AS cur_lecturer LEFT JOIN courses ON course_id=courses.id "
					+ "ORDER BY courses.start_date";
			PreparedStatement statement = con.prepareStatement(query);
			statement.setInt(1, lecturer.getID());
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				addCourseToYear(years,
						new Course(rs.getString(1), rs.getInt(3)), rs
								.getDate(2).getYear() + 1900);
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return years;
	}

	// courses are passed to addCourseToYear ordered ascending by 'year'
	// parameter
	private void addCourseToYear(List<AcadYear> years, Course course, int year) {
		if (years.isEmpty()
				|| years.get(years.size() - 1).getStartYear() < year)
			years.add(new AcadYear(year, year + 1));
		years.get(years.size() - 1).addCourse(course);
	}

	public List<Course> getAssignedCourses(User tutor) {
		List<Course> courses = new ArrayList<Course>();
		try {
			Connection con = dataSource.getConnection();
			String query = "SELECT courses.name, courses.ID "
					+ "FROM (SELECT * FROM courses_tutors WHERE tutor_id = ?) AS cur_tutor "
					+ "LEFT JOIN courses ON course_id=courses.id "
					+ "WHERE courses.start_date <= NOW() AND courses.end_date >= NOW()";
			PreparedStatement statement = con.prepareStatement(query);
			statement.setInt(1, tutor.getID());
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				courses.add(new Course(rs.getString(1), rs.getInt(2)));
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return courses;
	}

	/*
	 * id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name NVARCHAR(128) NOT NULL,
	 * description TEXT, start_date DATE, end_date DATE, latedays_num INT
	 * DEFAULT 0, latedays_len INT
	 */
	public void addCourseToDB(Course course) {
		List<String> columnNames = new ArrayList<String>();
		columnNames.add("name");
		columnNames.add("description");
		columnNames.add("start_date");
		columnNames.add("end_date");
		columnNames.add("latedays_num");
		columnNames.add("latedays_len");
		List<String> values = new ArrayList<String>();
		values.add(course.getName());
		values.add(course.getDescription());
		values.add(course.getStartDate().toString());
		values.add(course.getEndDate().toString());
		values.add("" + course.getLateDaysNumber());
		values.add("" + course.getLateDaysLength());
		executeInsert("courses", columnNames, values);
	}

}

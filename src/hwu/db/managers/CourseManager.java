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

import com.mysql.jdbc.Statement;

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

	// returns id of the added course
	public int addCourseToDB(Course course) {
		int ret = -1;
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
		String query = generateInsertQuery("courses", columnNames, values);
		try {
			Connection con = dataSource.getConnection();
			PreparedStatement statement = con.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			statement.executeUpdate();
			ResultSet rs = statement.getGeneratedKeys();
			rs.next();
			ret = rs.getInt(1);
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public void enroll(List<Student> students, int course_id) {
		String query = "INSERT INTO course_students(course_id, student_id) "
				+ "VALUES(" + course_id
				+ ", (SELECT id FROM users WHERE email_creds=?) )";
		try {
			Connection con = dataSource.getConnection();
			PreparedStatement statement;
			statement = con.prepareStatement(query);
			for (Student student : students) {
				statement.setString(1, student.getEmail());
				statement.executeUpdate();
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isAssociated(User user, Course course) {
		String queries[] = new String[3];
		queries[0] = "SELECT * FROM course_students WHERE course_id=? AND student_id=?";
		queries[1] = "SELECT * FROM courses_lecturers WHERE course_id=? AND lecturer_id=?";
		queries[2] = "SELECT * FROM courses_tutors WHERE course_id=? AND tutor_id=?";
		try {
			Connection con = dataSource.getConnection();
			PreparedStatement statement = null;
			ResultSet rs;
			for (int i = 0; i < 3; ++i) {
				statement = con.prepareStatement(queries[i]);
				statement.setInt(1, course.getID());
				statement.setInt(2, user.getID());
				rs = statement.executeQuery();
				if (rs.next()) {
					con.close();
					return true;
				}
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
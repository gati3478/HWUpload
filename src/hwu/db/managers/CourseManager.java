package hwu.db.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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
		return null;
	}

	public List<Course> getCourses(Student student) {
		return null;
	}

	public Iterator<AcadYear> getCourses(Lecturer lecturer) {
		List<AcadYear> years = new ArrayList<AcadYear>();
		try {
			Connection con = dataSource.getConnection();
			String query = "SELECT courses.name, courses.start_date, courses.ID " + 
					"FROM (SELECT * FROM courses_lecturers WHERE lecturer_id = ?) " + 
					"AS cur_lecturer LEFT JOIN courses ON course_id=courses.id " + 
					"ORDER BY courses.start_date";
			PreparedStatement statement = con.prepareStatement(query);
			statement.setInt(1, lecturer.getID());
			ResultSet rs = statement.executeQuery();
			while(rs.next()){
				addCourseToYear(years, new Course(rs.getString(1), rs.getInt(3)), 
						rs.getDate(2).getYear() + 1900);
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return years.iterator();
	}

	// courses are passed to addCourseToYear ordered ascending by 'year' parameter
	private void addCourseToYear(List<AcadYear> years, Course course, int year) {
		if(years.isEmpty() || years.get(years.size()-1).getYear() < year)
			years.add(new AcadYear(year));
		years.get(years.size()-1).addCourse(course);
	}
	
	public List<Course> getAssignedCourses(User tutor) {
		return null;
	}

}

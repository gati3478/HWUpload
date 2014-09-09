package hwu.db.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import hwu.datamodel.Course;
import hwu.datamodel.users.Lecturer;
import hwu.datamodel.users.Student;
import hwu.datamodel.users.User;

import javax.sql.DataSource;

public class UserManager extends Manager {
	public static final String ATTRIBUTE_NAME = "user_manager";

	/**
	 * Constructs UserManager object with provided DataSource object.
	 * 
	 * @param dataSource
	 *            DataSource object representing connection pool.
	 */
	public UserManager(DataSource dataSource) {
		super(dataSource);
	}

	/**
	 * 
	 * @param user
	 * @throws SQLException
	 */
	public void tryAddUser(User user) throws SQLException {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("INSERT INTO users ");
		queryBuilder.append("(email_creds, first_name, last_name, status) ");
		queryBuilder.append("VALUES (?, ?, ?, ?)" );
		queryBuilder.append("ON DUPLICATE KEY UPDATE ");
		if(user.getFirstName() != null) 
			queryBuilder.append("users.first_name = VALUES(first_name), users.last_name = VALUES(last_name)"); 
		else queryBuilder.append("first_name=first_name"); // no change(equivalent to ignore)
		Connection con = dataSource.getConnection();
		PreparedStatement statement = con.prepareStatement(queryBuilder
				.toString());
		statement.setString(1, user.getEmail());
		statement.setString(2, user.getFirstName());
		statement.setString(3, user.getLastName());
		if (user instanceof Student)
			statement.setString(4, "student");
		else if (user instanceof Lecturer)
			statement.setString(4, "lecturer");
		statement.executeUpdate();
		con.close();
	}

	/**
	 * 
	 * @param emailCredential
	 * @return
	 * @throws SQLException
	 */
	public User getUser(String emailCredential) throws SQLException {
		User user = null;
		Connection con = dataSource.getConnection();
		String query = "SELECT * FROM users WHERE email_creds = ?;";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, emailCredential);
		ResultSet rs = statement.executeQuery();

		if (rs.next()) {
			int id = rs.getInt("id");
			String firstName = rs.getString("first_name");
			String lastName = rs.getString("last_name");
			String status = rs.getString("status");
			if (status.equals("student"))
				user = new Student(id, emailCredential, firstName, lastName);
			else if (status.equals("lecturer"))
				user = new Lecturer(id, emailCredential, firstName, lastName);
		}
		con.close();
		return user;
	}

	/**
	 * 
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public boolean isTutor(User user) throws SQLException {
		boolean result = false;
		Connection con = dataSource.getConnection();
		String query = "SELECT tutor FROM users WHERE id = ?;";
		PreparedStatement stm = con.prepareStatement(query);
		stm.setInt(1, user.getID());
		ResultSet rs = stm.executeQuery();
		if (rs.next())
			result = rs.getBoolean("tutor");
		con.close();
		return result;
	}

	/**
	 * 
	 * @param user
	 */
	public void makeTutor(User user) {
		executeSimpleUpdate("users", "tutor", "1", "id", ""+user.getID());
	}

	/**
	 * 
	 * @param user
	 */
	public void revokeTutor(User user) {
		executeSimpleUpdate("users", "tutor", "0", "id", "" + user.getID());
	}

	/**
	 * 
	 * @param course
	 * @return
	 * @throws SQLException
	 */
	public List<Student> getEnrolledStudents(Course course) throws SQLException {
		List<Student> students = new ArrayList<Student>();
		StringBuilder qb = new StringBuilder("SELECT users.id, ");
		qb.append("users.email_creds, users.first_name, users.last_name ");
		qb.append("FROM course_students INNER JOIN users ON ");
		qb.append("users.id = course_students.student_id WHERE course_id = ?;");
		Connection con = dataSource.getConnection();
		PreparedStatement stm = con.prepareStatement(qb.toString());
		stm.setInt(1, course.getID());
		ResultSet rs = stm.executeQuery();
		while (rs.next()) {
			int id = rs.getInt("id");
			String emailCreds = rs.getString("email_creds");
			String firstName = rs.getString("first_name");
			String lastName = rs.getString("last_name");
			Student user = new Student(id, emailCreds, firstName, lastName);
			students.add(user);
		}
		con.close();
		return students;
	}

	/**
	 * 
	 * @param course
	 * @param tutor
	 * @return
	 * @throws SQLException
	 */
	public List<Student> getAssignedEnrolledStudents(Course course, User tutor)
			throws SQLException {
		List<Student> students = new ArrayList<Student>();
		StringBuilder qb = new StringBuilder("SELECT users.id, ");
		qb.append("users.email_creds, users.first_name, users.last_name ");
		qb.append("FROM course_students INNER JOIN users ON ");
		qb.append("users.id = course_students.student_id ");
		qb.append("WHERE course_id = ? AND tutor_id = ?;");
		Connection con = dataSource.getConnection();
		PreparedStatement stm = con.prepareStatement(qb.toString());
		stm.setInt(1, course.getID());
		stm.setInt(2, tutor.getID());
		ResultSet rs = stm.executeQuery();
		while (rs.next()) {
			int id = rs.getInt("id");
			String emailCreds = rs.getString("email_creds");
			String firstName = rs.getString("first_name");
			String lastName = rs.getString("last_name");
			Student user = new Student(id, emailCreds, firstName, lastName);
			students.add(user);
		}
		con.close();
		return students;
	}
	
	public static boolean isStudentEmail(String email) {
		return email.length() == 7 
				&& Character.isDigit(email.charAt(5))
				&& Character.isDigit(email.charAt(6));
	}

	public static String getCreds(String email) {
		int atIndex = email.indexOf('@');
		if(atIndex == -1) return null;
		return email.substring(0, atIndex);
	}
}

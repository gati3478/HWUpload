package hwu.db.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
		queryBuilder.append("INSERT IGNORE INTO users ");
		queryBuilder.append("(email_creds, first_name, last_name, status) ");
		queryBuilder.append("VALUES (?, ?, ?, ?)");
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
			int tutor = rs.getInt("tutor");
			boolean isTutor = false;
			if (tutor == 1)
				isTutor = true;
			if (status.equals("student"))
				user = new Student(id, emailCredential, firstName, lastName,
						isTutor);
			else if (status.equals("lecturer"))
				user = new Lecturer(id, emailCredential, firstName, lastName,
						isTutor);
		}
		con.close();
		return user;
	}

	/**
	 * 
	 * @param user
	 */
	public void makeTutor(User user) {
		executeSimpleUpdate("users", "tutor", "1", "id", "" + user.getID());
	}

	/**
	 * 
	 * @param user
	 */
	public void revokeTutor(User user) {
		executeSimpleUpdate("users", "tutor", "0", "id", "" + user.getID());
	}

}

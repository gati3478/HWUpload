package hwu.db.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
		else
			statement.setString(4, "lecturer");
		statement.executeUpdate();
		con.close();
	}

	/**
	 * 
	 * @param emailCredential
	 * @return
	 */
	public User getUser(String emailCredential) {
		return null;
	}

}

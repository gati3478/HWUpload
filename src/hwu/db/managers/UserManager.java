package hwu.db.managers;

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

	public void tryAddUser(User user) {
	}

	public User getUser(String emailCredential) {
		return null;
	}

}

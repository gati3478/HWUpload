package hwu.datamodel.users;

public class Lecturer extends User {

	/**
	 * 
	 * @param id
	 * @param email
	 * @param firstName
	 * @param lastName
	 */
	public Lecturer(Integer id, String email, String firstName, String lastName) {
		super(id, email, firstName, lastName);
	}

	/**
	 * 
	 * @param email
	 * @param firstName
	 * @param lastName
	 */
	public Lecturer(String email, String firstName, String lastName) {
		super(email, firstName, lastName);
	}

}

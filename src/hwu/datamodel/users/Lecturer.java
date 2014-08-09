package hwu.datamodel.users;

public class Lecturer extends User {

	/**
	 * 
	 * @param id
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param isTutor
	 */
	public Lecturer(Integer id, String email, String firstName,
			String lastName, boolean isTutor) {
		super(id, email, firstName, lastName, isTutor);
	}

	/**
	 * 
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param isTutor
	 */
	public Lecturer(String email, String firstName, String lastName,
			boolean isTutor) {
		super(email, firstName, lastName, isTutor);
	}

}

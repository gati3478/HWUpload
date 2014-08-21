package hwu.datamodel.users;

public class Student extends User {

	/**
	 * 
	 * @param id
	 * @param email
	 * @param firstName
	 * @param lastName
	 */
	public Student(Integer id, String email, String firstName, String lastName) {
		super(id, email, firstName, lastName);
	}

	/**
	 * 
	 * @param email
	 * @param firstName
	 * @param lastName
	 */
	public Student(String email, String firstName, String lastName) {
		super(email, firstName, lastName);
	}
	
	/**
	 * 
	 * @param email
	 */
	public Student(String email) {
		super(email, null, null);
	}

}

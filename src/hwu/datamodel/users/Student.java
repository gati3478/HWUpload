package hwu.datamodel.users;

public class Student extends User {

	/**
	 * 
	 * @param id
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param isTutor
	 */
	public Student(Integer id, String email, String firstName, String lastName,
			boolean isTutor) {
		super(id, email, firstName, lastName, isTutor);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param isTutor
	 */
	public Student(String email, String firstName, String lastName,
			boolean isTutor) {
		super(email, firstName, lastName, isTutor);
	}

}

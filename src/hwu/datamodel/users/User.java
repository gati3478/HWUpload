package hwu.datamodel.users;

public abstract class User {
	public static final String ATTRIBUTE_NAME = "user";
	protected Integer id;
	protected String email;
	protected String firstName;
	protected String lastName;
	protected boolean isTutor;

	/**
	 * 
	 * @param id
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param isTutor
	 */
	public User(Integer id, String email, String firstName, String lastName,
			boolean isTutor) {
		this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.isTutor = isTutor;
	}

	/**
	 * 
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param isTutor
	 */
	public User(String email, String firstName, String lastName, boolean isTutor) {
		this(null, email, firstName, lastName, isTutor);
	}

	/**
	 * 
	 * @return
	 */
	public int getID() {
		return id;
	}

	/**
	 * 
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * 
	 * @return
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isTutor() {
		return isTutor;
	}

}

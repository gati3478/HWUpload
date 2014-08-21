package hwu.datamodel.users;

public abstract class User {
	public static final String ATTRIBUTE_NAME = "user";
	protected Integer id;
	protected String email;
	protected String firstName;
	protected String lastName;

	/**
	 * 
	 * @param id
	 * @param email
	 * @param firstName
	 * @param lastName
	 */
	public User(Integer id, String email, String firstName, String lastName) {
		this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	/**
	 * 
	 * @param email
	 * @param firstName
	 * @param lastName
	 */
	public User(String email, String firstName, String lastName) {
		this(null, email, firstName, lastName);
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

}

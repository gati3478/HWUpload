package hwu.datamodel.users;

public abstract class User {
	protected int id;
	protected String email;
	protected String firstName;
	protected String lastName;
	protected boolean isTutor;

	protected int getID() {
		return id;
	}

	protected String getEmail() {
		return email;
	}

	protected String getFirstName() {
		return firstName;
	}

	protected String getLastName() {
		return lastName;
	}

	protected boolean isTutor() {
		return isTutor;
	}

}

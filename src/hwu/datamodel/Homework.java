package hwu.datamodel;

import java.sql.Timestamp;

public class Homework {
	private int id;
	private String name;
	private String description;
	private int number;
	private Timestamp deadline;
	private boolean isActive;
	private boolean latedaysAllowed;
	private HomeworkForm form;

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
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @return
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * 
	 * @return
	 */
	public Timestamp getDeadline() {
		return deadline;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * 
	 * @return
	 */
	public boolean latedaysAllowed() {
		return latedaysAllowed;
	}

	private class HomeworkForm {
		private String regex;
		private int maxFilesize;

		/**
		 * 
		 * @return
		 */
		private String getRegex() {
			return regex;
		}

		/**
		 * 
		 * @return
		 */
		private int getMaxFilesize() {
			return maxFilesize;
		}

	}

}

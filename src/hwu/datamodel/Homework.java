package hwu.datamodel;

import java.sql.Timestamp;

public class Homework {
	private Integer id;
	private String name;
	private String description;
	private int number;
	private Timestamp deadline;
	private boolean isActive;
	private boolean latedaysAllowed;

	/**
	 * 
	 * @param id
	 * @param name
	 * @param description
	 * @param number
	 * @param deadline
	 * @param isActive
	 * @param latedaysAllowed
	 */
	public Homework(Integer id, String name, String description, int number,
			Timestamp deadline, boolean isActive, boolean latedaysAllowed) {
		this.id = id;
		this.description = description;
		this.number = number;
		this.deadline = deadline;
		this.isActive = isActive;
		this.latedaysAllowed = latedaysAllowed;
	}

	/**
	 * 
	 * @param name
	 * @param description
	 * @param number
	 * @param deadline
	 * @param isActive
	 * @param latedaysAllowed
	 */
	public Homework(String name, String description, int number,
			Timestamp deadline, boolean isActive, boolean latedaysAllowed) {
		this(null, name, description, number, deadline, isActive,
				latedaysAllowed);
	}

	/**
	 * 
	 * @return
	 */
	public Integer getID() {
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

	public class HomeworkForm {
		private String regex;
		private int maxFileSize;
		private String fileExt;

		/**
		 * 
		 * @param regex
		 * @param maxFileSize
		 * @param fileExt
		 */
		public HomeworkForm(String regex, int maxFileSize, String fileExt) {
			this.regex = regex;
			this.maxFileSize = maxFileSize;
			this.fileExt = fileExt;
		}

		/**
		 * 
		 * @return
		 */
		public String getRegex() {
			return regex;
		}

		/**
		 * 
		 * @return
		 */
		public int getMaxFileSize() {
			return maxFileSize;
		}

		/**
		 * 
		 * @return
		 */
		public String getFileExtension() {
			return fileExt;
		}

	}

}

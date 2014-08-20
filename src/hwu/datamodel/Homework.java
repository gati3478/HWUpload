package hwu.datamodel;

import java.sql.Timestamp;

public class Homework {
	private Integer id;
	private String name;
	private String description;
	private int number;
	private Timestamp deadline;
	private boolean isActive;
	private boolean latedaysDisabled;

	/**
	 * 
	 * @param id
	 * @param name
	 * @param description
	 * @param number
	 * @param deadline
	 * @param isActive
	 * @param latedaysDisabled
	 */
	public Homework(Integer id, String name, String description, int number,
			Timestamp deadline, boolean isActive, boolean latedaysDisabled) {
		this.id = id;
		this.description = description;
		this.number = number;
		this.deadline = deadline;
		this.isActive = isActive;
		this.latedaysDisabled = latedaysDisabled;
	}

	/**
	 * 
	 * @param name
	 * @param description
	 * @param number
	 * @param deadline
	 * @param isActive
	 * @param latedaysDisabled
	 */
	public Homework(String name, String description, int number,
			Timestamp deadline, boolean isActive, boolean latedaysDisabled) {
		this(null, name, description, number, deadline, isActive,
				latedaysDisabled);
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
	public boolean latedaysDisabled() {
		return latedaysDisabled;
	}

}

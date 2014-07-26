package hwu.datamodel;

import java.sql.Date;

public class Course {
	private Integer id;
	private String name;
	private String description;
	private Date startDate;
	private Date endDate;

	/**
	 * 
	 * @param id
	 * @param name
	 * @param description
	 * @param startDate
	 * @param endDate
	 */
	public Course(Integer id, String name, String description, Date startDate,
			Date endDate) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	/**
	 * 
	 * @param name
	 * @param description
	 * @param startDate
	 * @param endDate
	 */
	public Course(String name, String description, Date startDate, Date endDate) {
		this(null, name, description, startDate, endDate);
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
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * 
	 * @return
	 */
	public Date getEndDate() {
		return endDate;
	}

}

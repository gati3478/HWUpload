package hwu.datamodel;

import java.sql.Date;

public class Course {
	private Integer id;
	private String name;
	private String description;
	private Date startDate;
	private Date endDate;
	private int lateDaysNumber;
	private int lateDaysLength;

	/**
	 * 
	 * @param id
	 * @param name
	 * @param description
	 * @param startDate
	 * @param endDate
	 * @param lateDaysNumber
	 * @param lateDaysLength
	 */
	public Course(Integer id, String name, String description, Date startDate,
			Date endDate, int lateDaysLength, int lateDaysNumber) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.lateDaysLength = lateDaysLength;
		this.lateDaysNumber = lateDaysNumber;
	}

	/**
	 * 
	 * @param name
	 * @param description
	 * @param startDate
	 * @param endDate
	 * @param lateDaysNumber
	 * @param lateDaysLength
	 */
	public Course(String name, String description, Date startDate, 
			Date endDate, int lateDaysLength, int lateDaysNumber, boolean forbidLastDay) {
		this(null, name, description, startDate, endDate, lateDaysLength, lateDaysNumber);
	}

	/**
	 * @param id
	 * @param name
	 */
	public Course(String name, int id) {
		this(id, name, null, null, null, -1, -1);
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

	/**
	 * 
	 * @return
	 */
	public int getLateDaysNumber() {
		return lateDaysNumber;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getLateDaysLength() {
		return lateDaysLength;
	}

}

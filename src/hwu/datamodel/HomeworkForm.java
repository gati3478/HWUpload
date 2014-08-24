package hwu.datamodel;

public class HomeworkForm {
	public static String FIRST_NAME_EX = "#name";
	public static String LAST_NAME_EX = "#surname";
	public static String INITIAL_EX = "#initial";
	public static String GROUP_EX = "#group";
	public static String EMAIL_EX = "#emmail";
	private Integer id;
	private String regex;
	private String fileExt;

	/**
	 * 
	 * @param id
	 * @param regex
	 * @param maxFileSize
	 * @param fileExt
	 */
	public HomeworkForm(Integer id, String regex, String fileExt) {
		this.id = id;
		this.regex = regex;
		this.fileExt = fileExt;
	}

	/**
	 * 
	 * @param regex
	 * @param maxFileSize
	 * @param fileExt
	 */
	public HomeworkForm(String regex, String fileExt) {
		this(null, regex, fileExt);
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
	public String getRegex() {
		return regex;
	}

	/**
	 * 
	 * @return
	 */
	public String getFileExtension() {
		return fileExt;
	}

}

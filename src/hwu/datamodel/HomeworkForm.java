package hwu.datamodel;

public class HomeworkForm {
	private Integer id;
	private String regex;
	private int maxFileSize;
	private String fileExt;

	/**
	 * 
	 * @param id
	 * @param regex
	 * @param maxFileSize
	 * @param fileExt
	 */
	public HomeworkForm(Integer id, String regex, int maxFileSize,
			String fileExt) {
		this.id = id;
		this.regex = regex;
		this.maxFileSize = maxFileSize;
		this.fileExt = fileExt;
	}

	/**
	 * 
	 * @param regex
	 * @param maxFileSize
	 * @param fileExt
	 */
	public HomeworkForm(String regex, int maxFileSize, String fileExt) {
		this(null, regex, maxFileSize, fileExt);
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

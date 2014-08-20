package hwu.db.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import hwu.datamodel.Course;
import hwu.datamodel.Homework;
import hwu.datamodel.HomeworkForm;
import hwu.datamodel.users.Student;

import javax.sql.DataSource;

public class HomeworkManager extends Manager {
	public static final String ATTRIBUTE_NAME = "homework_manager";

	/**
	 * Constructs HomeworkManager object with provided DataSource object.
	 * 
	 * @param dataSource
	 *            DataSource object representing connection pool.
	 */
	public HomeworkManager(DataSource dataSource) {
		super(dataSource);
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public Homework getHomework(int id) throws SQLException {
		Homework result = null;
		Connection con = dataSource.getConnection();
		String query = "SELECT * FROM homework WHERE id = ?;";
		PreparedStatement st = con.prepareStatement(query);
		st.setInt(1, id);
		ResultSet rs = st.executeQuery();
		// parsing to Homework object
		if (rs.next()) {
			String name = rs.getString("name");
			String description = rs.getString("description");
			int number = rs.getInt("number");
			Timestamp deadline = rs.getTimestamp("deadline");
			boolean active = rs.getBoolean("active");
			boolean latedaysDisabled = rs.getBoolean("forbid_latedays");
			result = new Homework(id, name, description, number, deadline,
					active, latedaysDisabled);
		}
		con.close();
		return result;
	}

	/**
	 * 
	 * @param homework
	 * @param course
	 */
	public void addHomework(Homework homework, Course course) {
		List<String> columnNames = new ArrayList<String>();
		columnNames.add("name");
		columnNames.add("description");
		columnNames.add("number");
		columnNames.add("course_id");
		columnNames.add("deadline");
		columnNames.add("active");
		columnNames.add("forbid_latedays");
		List<String> values = new ArrayList<String>();
		values.add(homework.getName());
		values.add(homework.getDescription());
		values.add("" + homework.getNumber());
		values.add("" + course.getID());
		values.add(homework.getDeadline().toString());
		values.add("" + homework.isActive());
		values.add("" + homework.latedaysDisabled());
		executeInsert("homework", columnNames, values);
	}

	/**
	 * 
	 * @param homework
	 * @param newName
	 */
	public void changeHomeworkName(Homework homework, String newName) {
		executeSimpleUpdate("homework", "name", newName, "id",
				"" + homework.getID());
	}

	/**
	 * 
	 * @param homework
	 * @param newDescription
	 */
	public void changeHomeworkDescription(Homework homework,
			String newDescription) {
		executeSimpleUpdate("homework", "description", newDescription, "id", ""
				+ homework.getID());
	}

	/**
	 * 
	 * @param homework
	 * @param newNumber
	 */
	public void changeHomeworkNumber(Homework homework, String newNumber) {
		executeSimpleUpdate("homework", "number", newNumber, "id", ""
				+ homework.getID());
	}

	/**
	 * 
	 * @param homework
	 * @param newDeadline
	 */
	public void changeHomeworkDeadline(Homework homework, Timestamp newDeadline) {
		executeSimpleUpdate("homework", "deadline", newDeadline.toString(),
				"id", "" + homework.getID());
	}

	/**
	 * 
	 * @param homework
	 */
	public void makeHomeworkActive(Homework homework) {
		changeHomeworkState(homework, true);
	}

	/**
	 * 
	 * @param homework
	 */
	public void makeHomeworkDisabled(Homework homework) {
		changeHomeworkState(homework, false);
	}

	/*
	 * 
	 */
	private void changeHomeworkState(Homework homework, boolean state) {
		executeSimpleUpdate("homework", "active", "" + state, "id", ""
				+ homework.getID());
	}

	/**
	 * 
	 * @param homework
	 */
	public void removeHomework(Homework homework) {
		executeSimpleDelete("homework", "id", homework.getID().toString());
	}

	/**
	 * 
	 * @param homework
	 * @return
	 * @throws SQLException
	 */
	public List<HomeworkForm> getHomeworkForms(Homework homework)
			throws SQLException {
		List<HomeworkForm> forms = new ArrayList<HomeworkForm>();
		Connection con = dataSource.getConnection();
		String query = "SELECT * FROM homework_forms WHERE hw_id = ?;";
		PreparedStatement st = con.prepareStatement(query);
		st.setInt(1, homework.getID());
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			Integer id = rs.getInt("id");
			String regex = rs.getString("regex");
			int maxFileSize = rs.getInt("max_filesize");
			String fileExt = rs.getString("file_extension");
			HomeworkForm form = new HomeworkForm(id, regex, maxFileSize,
					fileExt);
			forms.add(form);
		}
		con.close();
		return forms;
	}

	/**
	 * 
	 * @param homework
	 * @param form
	 */
	public void addHomeworkForm(Homework homework, HomeworkForm form) {
		List<String> columnNames = new ArrayList<String>();
		columnNames.add("hw_id");
		columnNames.add("regex");
		columnNames.add("max_filesize");
		columnNames.add("file_extension");
		List<String> values = new ArrayList<String>();
		values.add("" + homework.getID());
		values.add(form.getRegex());
		values.add("" + form.getMaxFileSize());
		values.add(form.getFileExtension());
		executeInsert("homework_forms", columnNames, values);
	}

	/**
	 * 
	 * @param form
	 * @param newRegex
	 */
	public void changeFormRegex(HomeworkForm form, String newRegex) {
		executeSimpleUpdate("homework_forms", "regex", newRegex, "id", ""
				+ form.getID());
	}

	/**
	 * 
	 * @param form
	 * @param newMaxFileSize
	 */
	public void changeFormMaxFileSize(HomeworkForm form, int newMaxFileSize) {
		executeSimpleUpdate("homework_forms", "max_filesize", ""
				+ newMaxFileSize, "id", "" + form.getID());
	}

	/**
	 * 
	 * @param form
	 * @param newExt
	 */
	public void changeFormFileExtension(HomeworkForm form, String newExt) {
		executeSimpleUpdate("homework_forms", "file_extension", newExt, "id",
				"" + form.getID());
	}

	/**
	 * 
	 * @param homework
	 * @param form
	 * @throws SQLException
	 */
	public void removeHomeworkForm(Homework homework, HomeworkForm form) {
		executeSimpleDelete("homework_forms", "id", "" + form.getID());
	}

	/**
	 * 
	 * @param course
	 * @return
	 * @throws SQLException
	 */
	public List<Homework> getHomework(Course course) throws SQLException {
		List<Homework> result = new ArrayList<Homework>();
		Connection con = dataSource.getConnection();
		String query = "SELECT * FROM homework WHERE course_id = ?;";
		PreparedStatement statement = con.prepareStatement(query);
		statement.setInt(1, course.getID());
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
			Integer id = rs.getInt("id");
			String name = rs.getString("name");
			String description = rs.getString("description");
			int number = rs.getInt("number");
			Timestamp deadline = rs.getTimestamp("deadline");
			boolean active = rs.getBoolean("active");
			boolean latedaysDisabled = rs.getBoolean("forbid_latedays");
			Homework hw = new Homework(id, name, description, number, deadline,
					active, latedaysDisabled);
			result.add(hw);
		}
		con.close();
		return result;
	}

	/**
	 * 
	 * @param hw
	 * @param student
	 * @param filename
	 * @param date
	 */
	public void addHomeworkFilename(Homework hw, Student student,
			String filename, Timestamp date) {
		List<String> columnNames = new ArrayList<String>();
		columnNames.add("filename");
		columnNames.add("submit_time");
		columnNames.add("student_id");
		columnNames.add("hw_id");
		List<String> values = new ArrayList<String>();
		values.add(filename);
		values.add(date.toString());
		values.add("" + student.getID());
		values.add("" + hw.getID());
		executeInsert("files", columnNames, values);
	}

	/**
	 * 
	 * @param hw
	 * @param student
	 * @return
	 * @throws SQLException
	 */
	public Timestamp getHomeworkSubmissionDate(Homework hw, Student student)
			throws SQLException {
		Timestamp result = null;
		Connection con = dataSource.getConnection();
		String query = "SELECT * FROM files WHERE hw_id = ? AND student_id = ?;";
		PreparedStatement stm = con.prepareStatement(query);
		stm.setInt(1, hw.getID());
		stm.setInt(2, student.getID());
		ResultSet rs = stm.executeQuery();
		if (rs.next())
			result = rs.getTimestamp("submit_time");
		con.close();
		return result;
	}

	/**
	 * 
	 * @param hw
	 * @return
	 * @throws SQLException
	 */
	public List<String> getHomeworkFilenames(Homework hw) throws SQLException {
		List<String> result = new ArrayList<String>();
		Connection con = dataSource.getConnection();
		String query = "SELECT * FROM files WHERE hw_id = ?;";
		PreparedStatement stm = con.prepareStatement(query);
		stm.setInt(1, hw.getID());
		ResultSet rs = stm.executeQuery();
		while (rs.next()) {
			String filename = rs.getString("filename");
			result.add(filename);
		}
		con.close();
		return result;
	}

	/**
	 * 
	 * @param hw
	 * @param student
	 * @return
	 * @throws SQLException
	 */
	public List<String> getStudentHomeworkFilenames(Homework hw, Student student)
			throws SQLException {
		List<String> result = new ArrayList<String>();
		Connection con = dataSource.getConnection();
		String query = "SELECT * FROM files WHERE hw_id = ? AND student_id = ?;";
		PreparedStatement stm = con.prepareStatement(query);
		stm.setInt(1, hw.getID());
		stm.setInt(2, student.getID());
		ResultSet rs = stm.executeQuery();
		while (rs.next()) {
			String filename = rs.getString("filename");
			result.add(filename);
		}
		con.close();
		return result;
	}

}

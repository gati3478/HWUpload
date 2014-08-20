package hwu.db.managers;

import hwu.datamodel.Course;
import hwu.datamodel.Homework;
import hwu.datamodel.users.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class LateDaysManager extends Manager {
	public static final String ATTRIBUTE_NAME = "latedays_manager";

	/**
	 * Constructs LatedaysManager object with provided DataSource object.
	 * 
	 * @param dataSource
	 *            DataSource object representing connection pool.
	 */
	public LateDaysManager(DataSource dataSource) {
		super(dataSource);
	}

	/**
	 * 
	 * @param homework
	 */
	public void disallowLatedays(Homework homework) {
		changeLatedaysState(homework, true);
	}

	/**
	 * 
	 * @param homework
	 */
	public void allowLatedays(Homework homework) {
		changeLatedaysState(homework, false);
	}

	/*
	 * 
	 */
	private void changeLatedaysState(Homework homework, boolean state) {
		executeSimpleUpdate("homework", "forbid_latedays", "" + state, "id", ""
				+ homework.getID());
	}

	/**
	 * 
	 * @param hw
	 * @param student
	 * @throws SQLException
	 */
	public void useLateDay(Homework hw, Student student) throws SQLException {
		int lateDaysTaken = usedLateDays(hw, student);
		// zero used latedays means that corresponding row doesn't exist
		if (lateDaysTaken == 0) {
			// we create row so its 'latedays_taken' field can be increased
			List<String> values = new ArrayList<String>();
			values.add("" + student.getID());
			values.add("" + hw.getID());
			values.add("" + 0);
			executeInsert("latedays_history", values);
		}
		// executing actual increment
		Connection con = dataSource.getConnection();
		StringBuilder qb = new StringBuilder("UPDATE latedays_history ");
		qb.append("SET latedays_taken = ? ");
		qb.append("WHERE hw_id = ? AND student_id = ?;");
		PreparedStatement stm = con.prepareStatement(qb.toString());
		stm.setInt(1, lateDaysTaken + 1);
		stm.setInt(2, hw.getID());
		stm.setInt(3, student.getID());
		stm.executeUpdate();
		con.close();
	}

	/**
	 * 
	 * @param hw
	 * @param student
	 * @return
	 * @throws SQLException
	 */
	public int usedLateDays(Homework hw, Student student) throws SQLException {
		int lateDaysTaken = 0;
		Connection con = dataSource.getConnection();
		StringBuilder qb = new StringBuilder("SELECT latedays_taken FROM ");
		qb.append("latedays_history WHERE hw_id = ? AND student_id = ?;");
		PreparedStatement stm = con.prepareStatement(qb.toString());
		stm.setInt(1, hw.getID());
		stm.setInt(2, student.getID());
		ResultSet rs = stm.executeQuery();
		// parsing data if applicable
		if (rs.next())
			lateDaysTaken = rs.getInt("latedays_taken");
		con.close();
		return lateDaysTaken;
	}

	/**
	 * 
	 * @param student
	 * @return
	 * @throws SQLException
	 */
	public int usedLateDays(Course course, Student student) throws SQLException {
		int lateDaysTaken = 0;
//		Connection con = dataSource.getConnection();
//		StringBuilder qb = new StringBuilder("SELECT latedays_taken FROM ");
//		qb.append("latedays_history WHERE student_id = ?;");
//		PreparedStatement stm = con.prepareStatement(qb.toString());
//		stm.setInt(1, student.getID());
//		ResultSet rs = stm.executeQuery();
//		// parsing data if applicable
//		while (rs.next())
//			lateDaysTaken += rs.getInt("latedays_taken");
//		con.close();
		return lateDaysTaken;
	}

	public int getLateDaysNum(Course course) {
		return 0;
	}

	public void changeLateDaysNum(Course course, int newNumber) {
	}

	public int getLateDaysLength(Course course) {
		return 0;
	}

	public void changeLateDaysLength(Course course, int newLength) {
	}

}

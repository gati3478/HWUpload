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
		int boolState = 0;
		if (state)
			boolState = 1;
		executeSimpleUpdate("homework", "forbid_latedays", "" + boolState,
				"id", "" + homework.getID());
	}

	/**
	 * 
	 * @param hw
	 * @param student
	 * @throws SQLException
	 */
	public void useLateDay(Homework hw, Student student) throws SQLException {
		int lateDaysTaken = usedLateDaysForHomework(hw, student);
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
	public int usedLateDaysForHomework(Homework hw, Student student)
			throws SQLException {
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
	 * @throws SQLException
	 * @return
	 */
	public int usedLateDays(Course course, Student student) {
		int lateDaysTaken = 0;
		try {
			Connection con = dataSource.getConnection();
			String query = "SELECT SUM(latedays_taken) FROM latedays_history LEFT JOIN "
					+ "homework ON homework.id=latedays_history.hw_id "
					+ "WHERE student_id=? AND homework.course_id=?";
			PreparedStatement statement = con.prepareStatement(query);
			statement.setInt(1, student.getID());
			statement.setInt(2, course.getID());
			ResultSet rs = statement.executeQuery();
			rs.next();
			lateDaysTaken = rs.getInt(1);
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lateDaysTaken;
	}

	/**
	 * 
	 * @param course
	 * @param student
	 * @return
	 */
	public int lateDaysRemaining(Course course, Student student) {
		int all = 0;
		int taken = usedLateDays(course, student);
		try {
			Connection con = dataSource.getConnection();
			List<String> columns = new ArrayList<String>();
			columns.add("latedays_num");
			String query = generateSimpleSelectQuery("courses", columns, "id", "" + course.getID());
			PreparedStatement statement = con.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			rs.next();
			all = rs.getInt(1);
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return all - taken;
	}

	/**
	 * 
	 * @param course
	 * @param newNumber
	 */
	public void changeLateDaysNum(Course course, int newNumber) {
		executeSimpleUpdate("courses", "latedays_num", "" + newNumber, "id", ""
				+ course.getID());
	}

	/**
	 * 
	 * @param course
	 * @param newLength
	 */
	public void changeLateDaysLength(Course course, int newLength) {
		executeSimpleUpdate("courses", "latedays_len", "" + newLength, "id", ""
				+ course.getID());
	}

}

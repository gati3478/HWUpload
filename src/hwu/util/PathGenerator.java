package hwu.util;

import java.io.File;
import java.util.Calendar;

import hwu.datamodel.Course;
import hwu.datamodel.Homework;
import hwu.datamodel.users.Student;

public class PathGenerator {

	/**
	 * 
	 * @param course
	 * @param homework
	 * @return
	 */
	public static String getRelativePath(Course course, Homework homework) {
		return getRelativePath(course, homework, null);
	}

	/**
	 * 
	 * @param course
	 * @param student
	 * @param homework
	 * @return
	 */
	public static String getRelativePath(Course course, Homework homework,
			Student student) {
		String finalPath = "";
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(course.getStartDate().getTime());
		int startYear = cal.get(Calendar.YEAR);
		cal.setTimeInMillis(course.getEndDate().getTime());
		int endYear = cal.get(Calendar.YEAR);
		finalPath = File.separator + startYear + "-" + endYear;
		finalPath += File.separator + course.getName() + course.getID();
		finalPath += File.separator + homework.getNumber();
		if (student != null)
			finalPath += File.separator + student.getEmail();
		return finalPath;
	}
}

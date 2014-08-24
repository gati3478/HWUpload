package hwu.servlet;

import hwu.datamodel.Course;
import hwu.datamodel.Homework;
import hwu.datamodel.users.Student;
import hwu.datamodel.users.User;
import hwu.db.managers.CourseManager;
import hwu.db.managers.HomeworkManager;
import hwu.db.managers.LateDaysManager;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UseLateDay
 */
@WebServlet("/UseLateDay")
public class UseLateDay extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UseLateDay() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// setting unicode encoding for inputs
		response.setContentType("UTF-8");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");

		User currUser = (User) request.getSession().getAttribute(
				User.ATTRIBUTE_NAME);
		HomeworkManager hwManager = (HomeworkManager) getServletContext()
				.getAttribute(HomeworkManager.ATTRIBUTE_NAME);
		CourseManager manager = (CourseManager) getServletContext()
				.getAttribute(CourseManager.ATTRIBUTE_NAME);
		LateDaysManager ldManager = (LateDaysManager) getServletContext()
				.getAttribute(LateDaysManager.ATTRIBUTE_NAME);
		String hwIdStr = request.getParameter("hw");
		String courseIdStr = request.getParameter("course");

		// error checking
		if (currUser == null || hwIdStr == null || courseIdStr == null) {
			response.sendRedirect("index.jsp");
			return;
		}

		Integer homework_id = null;
		Integer course_id = null;
		try {
			homework_id = Integer.parseInt(hwIdStr);
			course_id = Integer.parseInt(courseIdStr);
		} catch (NumberFormatException e) {
			response.sendRedirect("index.jsp");
			return;
		}

		Homework thisHomework = null;
		try {
			thisHomework = hwManager.getHomework(homework_id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Course thisCourse = manager.getCourse(course_id);
		if (thisHomework == null || thisCourse == null
				|| !thisHomework.isActive() || thisHomework.latedaysDisabled()
				|| !(currUser instanceof Student)
				|| !manager.isAssociated(currUser, thisCourse)) {
			response.sendRedirect("index.jsp");
			return;
		}

		// calculating and using latedays
		int totalLateDays = thisCourse.getLateDaysNumber();
		try {
			int lateDaysUsed = ldManager.usedLateDays(thisCourse,
					(Student) currUser);
			if (totalLateDays - lateDaysUsed > 0) {
				ldManager.useLateDay(thisHomework, (Student) currUser);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.sendRedirect("homework.jsp?id=" + homework_id + "&course_id="
				+ course_id);
	}
}

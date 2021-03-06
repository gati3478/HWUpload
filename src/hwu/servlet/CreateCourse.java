package hwu.servlet;

import hwu.datamodel.Course;
import hwu.datamodel.users.Lecturer;
import hwu.datamodel.users.User;
import hwu.db.managers.CourseManager;
import hwu.db.managers.UserManager;
import hwu.util.ExcelParser;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CreateCourse")
public class CreateCourse extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String COURSE_ID_ATTRIBUTE_NAME = "course_id";
	public static final String COURSE_CREATION_ERROR_MESSAGE = "error";

	public CreateCourse() {
		super();
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// setting unicode encoding for inputs
		response.setContentType("UTF-8");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");

		User user = (User) request.getSession().getAttribute(
				User.ATTRIBUTE_NAME);
		CourseManager manager = (CourseManager) request.getServletContext()
				.getAttribute(CourseManager.ATTRIBUTE_NAME);
		UserManager uManager = (UserManager) request.getServletContext()
				.getAttribute(UserManager.ATTRIBUTE_NAME);
		if (user == null || !(user instanceof Lecturer)) {
			response.sendRedirect("index.jsp");
			return;
		}

		String name = request.getParameter("name");
		String description = request.getParameter("descr");
		Date startDate = constructDate(request.getParameter("fday"),
				request.getParameter("fmonth"), request.getParameter("fyear"));
		Date endDate = constructDate(request.getParameter("eday"),
				request.getParameter("emonth"), request.getParameter("eyear"));
		
		if(startDate == null || endDate == null) {
			response.sendRedirect("createcourse.jsp");
			return;
		}
		
		int lateDaysNum = -1;
		int lateDaysLen = -1;
		boolean forbidLast = false;
		// check for late days parameters
		if (request.getParameter("late_days") != null) {
			if (request.getParameter("late_day_num") == null
					|| request.getParameter("late_day_len") == null) {
				response.sendRedirect("index.jsp");
				return;
			}
			lateDaysNum = Integer
					.parseInt(request.getParameter("late_day_num"));
			lateDaysLen = Integer
					.parseInt(request.getParameter("late_day_len"));
			forbidLast = request.getParameter("last_day") != null;
		}

		int course_id = manager.addCourseToDB(new Course(name, description,
				startDate, endDate, lateDaysLen, lateDaysNum, forbidLast));
		Course course = new Course(course_id);
		
		manager.associate(new Course(course_id), user);
		int i = 0;
		while(true) {
			String lectEmail = request.getParameter("lecturer"+i);
			if(lectEmail == null) break;
			Lecturer lecturer = null;
			try {
				uManager.tryAddUser(new Lecturer(lectEmail)); 
				lecturer = (Lecturer) uManager.getUser(lectEmail);
			} 
			catch (SQLException ignored) {}
			manager.associate(course, lecturer);
			++i;
		} 
		
		request.setAttribute(COURSE_ID_ATTRIBUTE_NAME, course_id);
		request.setAttribute(COURSE_CREATION_ERROR_MESSAGE, "");
		RequestDispatcher rd = request.getRequestDispatcher("enrollment.jsp");
		rd.forward(request, response);
	}

	private Date constructDate(String day, String month, String year) {
		// check whether all fields are filled
		if (day == null || month == null || year == null)
			return null;

		int dayInt = Integer.parseInt(day);
		int monthInt = Integer.parseInt(month);
		int yearInt = Integer.parseInt(year);

		// check whether all fields are filled correctly
		if (dayInt < 1 || dayInt > 31 || monthInt < 1 || monthInt > 12
				|| yearInt < 1900)
			return null;

		return Date.valueOf(year + "-" + month + "-" + day);
	}

}
package hwu.servlet;

import hwu.datamodel.Course;
import hwu.datamodel.Homework;
import hwu.datamodel.HomeworkForm;
import hwu.datamodel.users.Lecturer;
import hwu.datamodel.users.User;
import hwu.db.managers.HomeworkManager;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CreateHomework
 */
@WebServlet("/CreateHomework")
public class CreateHomework extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CreateHomework() {
		super();
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("UTF-8");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");

		User user = (User) request.getSession().getAttribute(
				User.ATTRIBUTE_NAME);
		HomeworkManager manager = (HomeworkManager) request.getServletContext()
				.getAttribute(HomeworkManager.ATTRIBUTE_NAME);
		if (user == null || !(user instanceof Lecturer)) {
			response.sendRedirect("index.jsp");
			return;
		}

		String course_id = request.getParameter(Course.COURSE_ID_PARAM_NAME);
		Homework hw = constructHomework(request);
		if (course_id == null || hw == null) {
			response.sendRedirect("createhomework.jsp");
			return;
		}

		hw = new Homework(manager.addHomework(hw,
				new Course(Integer.parseInt(course_id))));
		List<HomeworkForm> forms = addHomeworkForm(request);
		for (HomeworkForm form : forms)
			manager.addHomeworkForm(hw, form);

		response.sendRedirect("homework.jsp?id=" + hw.getID() + "&course_id="
				+ course_id);
	}

	private Homework constructHomework(HttpServletRequest request) {
		String number = request.getParameter("number");
		String name = request.getParameter("name");
		String description = request.getParameter("descr");
		Timestamp deadline = constructTimestamp(request.getParameter("day"),
				request.getParameter("month"), request.getParameter("year"),
				request.getParameter("hours"), request.getParameter("minutes"));
		if (deadline == null)
			return null;

		boolean forbidLateDays = request.getParameter("late_days") == null;
		boolean isActive = request.getParameter("active") != null;

		return new Homework(name, description, Integer.parseInt(number),
				deadline, isActive, forbidLateDays);
	}

	private Timestamp constructTimestamp(String day, String month, String year,
			String hours, String minutes) {
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

		return Timestamp.valueOf(year + "-" + month + "-" + day + " " + hours
				+ ":" + minutes + ":00");
	}

	private List<HomeworkForm> addHomeworkForm(HttpServletRequest request) {
		List<HomeworkForm> forms = new ArrayList<HomeworkForm>();
		int i = 0;
		while (true) {
			String regex = request.getParameter("regex" + i);
			String extension = request.getParameter("ext" + i);
			if (regex == null)
				break;
			// temporarily 128
			forms.add(new HomeworkForm(regex, extension));
			i++;
		}
		return forms;
	}

}

package hwu.servlet;

import hwu.datamodel.Course;
import hwu.datamodel.users.Student;
import hwu.datamodel.users.User;
import hwu.db.managers.CourseManager;
import hwu.db.managers.UserManager;
import hwu.util.ExcelParser;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class Enroll
 */
@WebServlet("/Enroll")
@MultipartConfig
public class Enroll extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String COURSE_CREATION_ERROR_MESSAGE = "error";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Enroll() {
		super();
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
		response.setContentType("UTF-8");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		CourseManager cManager = (CourseManager) getServletContext()
				.getAttribute(CourseManager.ATTRIBUTE_NAME);
		UserManager uManager = (UserManager) getServletContext().getAttribute(
				UserManager.ATTRIBUTE_NAME);
		int course_id = Integer.parseInt(request.getParameter
				(CreateCourse.COURSE_ID_ATTRIBUTE_NAME));

		Part filePart = request.getPart("file"); // Retrieves <input type="file"
													// name="file">
		InputStream filecontent = filePart.getInputStream();

		List<Student> students = new ArrayList<Student>();
		List<User> tutors = new ArrayList<User>();
		ExcelParser.getStudentList(filecontent, students, tutors);

		filecontent.close();
		filePart.delete(); // probably necessary to free up the server storage

		if (students.isEmpty()) {
			request.setAttribute(COURSE_CREATION_ERROR_MESSAGE,
					"ფაილის წაკითხვისას პრობლემა შეიქმნა");
			RequestDispatcher rd = request
					.getRequestDispatcher("enrollment.jsp");
			rd.forward(request, response);
			// TODO: revert to the previous page
			return;
		}

		for (int i = 0; i < students.size(); ++i) {
			try {
				Student st = students.get(i);
				uManager.tryAddUser(st);
				students.set(i, (Student) uManager.getUser(st.getEmail()));
			} catch (SQLException ignored) {}
		}
		List<User> tutorsWithID = new ArrayList<User>();
		
		for(int i = 0; i < tutors.size(); ++i) {
			User tutor = tutors.get(i);
			try {
				uManager.tryAddUser(tutor);
				tutor = uManager.getUser(tutor.getEmail());
				tutors.set(i, tutor);
				if(!contains(tutorsWithID, tutor)) {
					tutorsWithID.add(tutor);
					uManager.makeTutor(tutor);
				}
			} catch (SQLException ignored) {}
		}
		
		Course course = new Course(course_id);

		if (tutors.isEmpty())
			cManager.enroll(students, course);
		else {
			cManager.addTutorship(tutorsWithID, course);
			cManager.enroll(students, tutors, course);
		}

		response.sendRedirect("course.jsp?id=" + course_id);
	}

	private boolean contains(List<User> tutors, User u) {
		for(User t: tutors) {
			if(t.getEmail().equals(u.getEmail()))
				return true;
		}
		return false;
	}

}
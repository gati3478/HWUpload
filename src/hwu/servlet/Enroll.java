package hwu.servlet;

import hwu.datamodel.Course;
import hwu.datamodel.users.Student;
import hwu.datamodel.users.User;
import hwu.db.managers.CourseManager;
import hwu.util.ExcelParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Enroll
 */
@WebServlet("/Enroll")
public class Enroll extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Enroll() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("UTF-8");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		CourseManager manager = (CourseManager) getServletContext()
				.getAttribute(CourseManager.ATTRIBUTE_NAME);
		int course_id = Integer.parseInt(request.getParameter("course_id"));
		File file = (File)request.getAttribute("datafile");
		List<Student> students = new ArrayList<Student>();
		List<User> tutors = new ArrayList<User>();
		ExcelParser.getStudentList(file, students, tutors);
		if(students.isEmpty()){
			//TODO: revert to the previous page
		}
		else{
			Course course = new Course(course_id);
			manager.enroll(students, course);
			//TODO: whatever we do with tutors
		}
	}

}

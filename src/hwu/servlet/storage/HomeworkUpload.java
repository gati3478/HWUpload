package hwu.servlet.storage;

import hwu.datamodel.Course;
import hwu.datamodel.Homework;
import hwu.datamodel.users.Student;
import hwu.datamodel.users.User;
import hwu.db.managers.CourseManager;
import hwu.db.managers.HomeworkManager;
import hwu.db.managers.LateDaysManager;
import hwu.util.PathGenerator;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.FileUtils;

/**
 * Servlet implementation class AvatarUpload
 */
@WebServlet("/HomeworkUpload")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 128, maxRequestSize = 1024 * 1024 * 256)
public class HomeworkUpload extends DiskStorage {
	private static final long serialVersionUID = 1L;
	private static final String UPLOAD_LOCATION_PROPERTY_KEY = "storage.location";
	private String uploadsDirName;

	@Override
	public void init() throws ServletException {
		super.init();
		uploadsDirName = property(UPLOAD_LOCATION_PROPERTY_KEY);
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
				|| !thisHomework.isActive() || !(currUser instanceof Student)
				|| !manager.isAssociated(currUser, thisCourse)) {
			response.sendRedirect("index.jsp");
			return;
		}

		// checking on deadline
		Timestamp deadline = thisHomework.getDeadline();
		int lateDaysTaken = 0;
		try {
			lateDaysTaken = ldManager.usedLateDaysForHomework(thisHomework,
					(Student) currUser);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int lateDayLength = thisCourse.getLateDaysLength();
		int totalDays = lateDaysTaken * lateDayLength;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(deadline.getTime());
		cal.add(Calendar.DAY_OF_MONTH, totalDays);
		deadline = new Timestamp(cal.getTimeInMillis());

		if (deadline.getTime() < System.currentTimeMillis()) {
			response.sendRedirect("homework.jsp?id=" + thisHomework.getID()
					+ "&course_id=" + thisCourse.getID());
		}

		// constructing file saving directory
		String relativeSaveDir = PathGenerator.getRelativePath(thisCourse,
				thisHomework, (Student) currUser);
		String finalSaveDir = uploadsDirName + relativeSaveDir;
		File finalSaveDirFile = new File(finalSaveDir);
		boolean directoryCleaned = false;

		// storing on disk
		for (Part part : request.getParts()) {
			String fileName = extractFileName(part);
			if (!fileName.isEmpty()) {
				if (!directoryCleaned) {
					if (finalSaveDirFile.exists())
						FileUtils.cleanDirectory(finalSaveDirFile);
					else
						finalSaveDirFile.mkdirs();
					directoryCleaned = true;
					// attemping to remove old files from the database (if
					// applicable)
					hwManager.removeStudentHomeworkFilenames(thisHomework,
							(Student) currUser);
				}
				String savePath = finalSaveDir + File.separator + fileName;
				hwManager.addHomeworkFilename(thisHomework, (Student) currUser,
						fileName, new Timestamp(System.currentTimeMillis()));
				part.write(savePath);
			}
		}

		// redirecting back to homework page
		response.sendRedirect("homework.jsp?id=" + thisHomework.getID()
				+ "&course_id=" + thisCourse.getID());
	}

	/*
	 * Extracts file name from HTTP header content-disposition
	 */
	private String extractFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 2, s.length() - 1);
			}
		}
		return "";
	}

}

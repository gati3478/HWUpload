<%@page import="hwu.db.managers.CourseManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="hwu.datamodel.users.User"%>
<%@page import="hwu.datamodel.users.Lecturer"%>
<%@page import="hwu.datamodel.users.Student"%>
<%@page import="hwu.datamodel.AcadYear"%>
<%@page import="hwu.datamodel.Course"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<link rel="shortcut icon" href="images/favicon.ico" />
<link rel="stylesheet" type="text/css" href="css/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>აკადემიური წლები</title>
<%
	User user = (User) session.getAttribute(User.ATTRIBUTE_NAME);
	CourseManager manager = (CourseManager) request.getServletContext()
			.getAttribute(CourseManager.ATTRIBUTE_NAME);
	if (user == null || !(user instanceof Lecturer)) {
		response.sendRedirect("index.jsp");
		return;
	}
%>
</head>
<body>
	<div class="content_wrapper">
		<div class="left">
			<h2>ჩემი კურსები:</h2>
			<%
				for (AcadYear year : manager.getCourses((Lecturer) user)) {
					out.println("<p> " + year.getStartYear() + " - "
							+ year.getEndYear() + " </p>");
					out.println("<ul>");
					for (Course course : year.getCourses()) {
						// links to course pages
						out.println("<li> <a href='course.jsp?id=" + course.getID()
								+ "'> " + course.getName() + " </a> </li>");
					}
					out.println("</ul>");
				}
			%>
		</div>
		<div class="right">
			<%
				if (user.isTutor())
					out.println("<a class=\"topright\" href=\"tutor.jsp\">სატუტორო კურსები</a>");
			%>
			<a class="topright" href="SignOut">სისტემიდან გასვლა</a>
		</div>
	</div>
</body>
</html>
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Academic Years</title>

</head>
<body>
	<h2>Lecturer's Academic Years</h2>
<%
	User user = (User) session.getAttribute(User.ATTRIBUTE_NAME);
	CourseManager manager = (CourseManager) request.getServletContext().
			getAttribute(CourseManager.ATTRIBUTE_NAME);
	if (user == null || user instanceof Student || manager == null) {
		response.sendRedirect("index.jsp");
		return;
	}

	
	for(AcadYear year : manager.getCourses((Lecturer)user)) {
		out.println("<p> " + year.getStartYear() + "/" + year.getEndYear() + " </p>");
		out.println("<ul>");
		for(Iterator<Course> courses = year.iterator(); courses.hasNext(); ) {
			Course course = courses.next();
			// links to course pages
			out.println("<li> <a href='somejsp.jsp?id=" + course.getID() + "'> " + 
				course.getName() + " </a> </li>");
		}
		out.println("</ul>");
	}
	
%>	
</body>
</html>
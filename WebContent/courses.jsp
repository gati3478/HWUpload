<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="hwu.datamodel.users.User"%>
<%@page import="hwu.datamodel.users.Student"%>
<%@page import="hwu.datamodel.Course"%>
<%@page import="hwu.db.managers.CourseManager"%>
<%@page import="hwu.db.managers.UserManager"%>
<!DOCTYPE html>
<html>
<head>
<link rel="shortcut icon" href="images/favicon.ico" />
<link rel="stylesheet" type="text/css" href="css/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	User user = (User) session.getAttribute(User.ATTRIBUTE_NAME);
	UserManager userManager = (UserManager) request.getServletContext()
			.getAttribute(UserManager.ATTRIBUTE_NAME);
	CourseManager manager = (CourseManager) request.getServletContext()
			.getAttribute(CourseManager.ATTRIBUTE_NAME);
	if (user == null || !(user instanceof Student)) {
		response.sendRedirect("index.jsp");
		return;
	}
%>
<title>მიმდინარე კურსები</title>
</head>
<body>
	<div class="content_wrapper">
		<div class="left">
			<h2>მიმდინარე კურსები:</h2>
			<%
				out.println("<ul>");
				for (Course course : manager.getCourses((Student) user)) {
					// links to course pages
					out.println("<li><a href='course.jsp?id=" + course.getID()
							+ "'>" + course.getName() + "</a></li>");
				}
				out.println("</ul>");
			%>
		</div>
		<div class="right">
			<%
				if (userManager.isTutor(user))
					out.println("<a class=\"topright\" href=\"tutor.jsp\">სატუტორო კურსები</a>");
			%>
			<a class="topright" href="SignOut">სისტემიდან გასვლა</a>
		</div>
	</div>
</body>
</html>
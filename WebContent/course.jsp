<%@page import="hwu.db.managers.HomeworkManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="hwu.datamodel.users.User"%>
<%@page import="hwu.datamodel.users.Student"%>
<%@page import="hwu.datamodel.users.Lecturer"%>
<%@page import="hwu.datamodel.Course"%>
<%@page import="hwu.datamodel.Homework"%>
<%@page import="hwu.db.managers.CourseManager"%>
<%@page import="hwu.db.managers.UserManager"%>
<%@page import="hwu.db.managers.HomeworkManager"%>
<%@page import="hwu.db.managers.LateDaysManager"%>
<!DOCTYPE html>
<html>
<head>
<link rel="shortcut icon" href="images/favicon.ico" />
<link rel="stylesheet" type="text/css" href="css/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	User user = (User) session.getAttribute(User.ATTRIBUTE_NAME);
	UserManager userManager = (UserManager) application
			.getAttribute(UserManager.ATTRIBUTE_NAME);
	CourseManager manager = (CourseManager) application
			.getAttribute(CourseManager.ATTRIBUTE_NAME);
	HomeworkManager hwManager = (HomeworkManager) application
			.getAttribute(HomeworkManager.ATTRIBUTE_NAME);
	LateDaysManager ldManager = (LateDaysManager) application
			.getAttribute(LateDaysManager.ATTRIBUTE_NAME);
	String courseIdStr = request.getParameter("id");
	if (user == null || courseIdStr == null) {
		response.sendRedirect("index.jsp");
		return;
	}
	Integer course_id = null;
	try {
		course_id = Integer.parseInt(courseIdStr);
	} catch (NumberFormatException e) {
		response.sendRedirect("index.jsp");
		return;
	}
	Course thisCourse = manager.getCourse(course_id);
	if (thisCourse == null || !manager.isAssociated(user, thisCourse)) {
		response.sendRedirect("index.jsp");
		return;
	}
%>
<title><%=thisCourse.getName()%></title>
</head>
<body>
	<div class="content_wrapper">
		<div class="left">
			<h2><%=thisCourse.getName()%></h2>
			<h4>
				აღწერა:
				<%=thisCourse.getDescription()%></h4>
			<h5>
				კურსის პერიოდი:
				<%=thisCourse.getStartDate().toString() + "-დან "
					+ thisCourse.getEndDate().toString() + "-მდე"%></h5>
			<h5>
				გადავადებათა რაოდენობა:
				<%=thisCourse.getLateDaysNumber()%></h5>
			<h5>
				გადავადების ხანგრძლივობა:
				<%=thisCourse.getLateDaysLength() + " დღე"%></h5>
			<%
				if (user instanceof Lecturer) {
					out.println("<a href=\"editcourse.jsp?id=" + thisCourse.getID()
							+ "\">კურსის ცვლილება</a>");
				}
			%>
			<%
				if (user instanceof Student) {
					out.print("<h5>");
					out.print("დარჩენილი გადავადებები: ");
					out.print(ldManager.lateDaysRemaining(thisCourse,
							(Student) user));
					out.println("</h5>");
				}
			%>
			<%
				out.println("<ul>");
				for (Homework hw : hwManager.getHomework(thisCourse)) {
					// links to course pages
					out.println("<li><a href='homework.jsp?id=" + hw.getID() + "'>"
							+ hw.getName() + "</a></li>");
				}
				out.println("</ul>");
			%>
		</div>
		<div class="right">
			<%
				if (userManager.isTutor(user))
					out.println("<a class=\"topright\" href=\"tutor.jsp\">სატუტორო კურსები</a>");
			%>
			<a class="topright" href="SignOut">სისტემიდან გასვლა (<%=user.getFirstName() + " " + user.getLastName()%>)
			</a>
		</div>
	</div>
</body>
</html>
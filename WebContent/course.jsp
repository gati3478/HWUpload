<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="hwu.datamodel.users.User"%>
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
	if (user == null) {
		response.sendRedirect("index.jsp");
		return;
	}
%>
<title>Insert title here</title>
</head>
<body>

</body>
</html>
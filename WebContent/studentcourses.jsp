<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="hwu.datamodel.users.User"%>
<%@page import="hwu.datamodel.users.Lecturer"%>
<!DOCTYPE html>
<html>
<head>
<link rel="shortcut icon" href="images/favicon.ico" />
<link rel="stylesheet" type="text/css" href="css/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	User user = (User) session.getAttribute(User.ATTRIBUTE_NAME);
	if (user == null || user instanceof Lecturer) {
		response.sendRedirect("index.jsp");
		return;
	}
%>
<title>Current Courses</title>
</head>
<body>
	<h2>Student's Courses:</h2>
	<a href="SignOut">Sign Out</a>
</body>
</html>
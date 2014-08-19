<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="hwu.datamodel.users.User"%>
<!DOCTYPE html>
<html>
<head>
<link rel="shortcut icon" href="images/favicon.ico" />
<link rel="stylesheet" type="text/css" href="css/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	User user = (User) session.getAttribute(User.ATTRIBUTE_NAME);
	if (user == null || !user.isTutor()) {
		response.sendRedirect("index.jsp");
		return;
	}
%>
<title>ტუტორის კურსები</title>
</head>
<body>
	<h2>ჩემი (ტუტორის) კურსები:</h2>
	<a href="SignOut">სისტემიდან გასვლა (Sign Out)</a>
</body>
</html>
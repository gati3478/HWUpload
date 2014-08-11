<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="hwu.datamodel.users.User"%>
<%@page import="hwu.datamodel.users.Student"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	User user = (User) session.getAttribute(User.ATTRIBUTE_NAME);
	if (user == null || user instanceof Student) {
		response.sendRedirect("index.jsp");
		return;
	}
%>
<title>Academic Years</title>
</head>
<body>
	<h2>Lecturer's Academic Years</h2>
</body>
</html>
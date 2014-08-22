<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="hwu.datamodel.users.User"%>
<%@page import="hwu.datamodel.users.Lecturer"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create Homework</title>
</head>
<body>
<% 
User user = (User) session.getAttribute(User.ATTRIBUTE_NAME);
if (user == null || !(user instanceof Lecturer)) {
	response.sendRedirect("index.jsp");
	return;
}
%>
<form action="CreateCourse" method="post">
Number: <input type="text" name="number" maxlength="2" size="2" required> <br>
Name: <input type="text" name="name" maxlength="64" required> <br>
Description: <br> 
<textarea rows="4" cols="50" name="descr"></textarea> <br>
<fieldset>
  <legend>Deadline date </legend>
  Day: <input type="text" maxlength="2" size="2" name="eday" required> 
  Month: <input type="text" maxlength="2" size="2" name="emonth" required> 
  Year: <input type="text" maxlength="4" size="4" name="eyear" required>
</fieldset>
<input type="checkbox" name="late_days" value="1" checked> Allow late days <br>
<input type="checkbox" name="active" value="1" checked> Is active <br>
<br>
<br>
Files to upload: <br>
<ul id="list"> </ul>
<button type="button" onclick="addField()">+</button>
<button type="button" onclick="removeField()">-</button> 
<p> <button> Submit </button> </p>
<script src='plusminus.js'> </script>
</form>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<%@page import="hwu.datamodel.Course"%>
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
	String courseID = request.getParameter(Course.COURSE_ID_PARAM_NAME);
	
	courseID = "1";
	
	if (user == null || !(user instanceof Lecturer) || courseID == null) {
		//response.sendRedirect("index.jsp");
		//return;
	}
%>
<form action="CreateHomework" method="post">
<% out.println("<input type='hidden' name='" + Course.COURSE_ID_PARAM_NAME + "' value='" + courseID + "'>"); %>
<div> Number: <input type="text" name="number" maxlength="2" size="2" required> </div>
<div> Name: <input type="text" name="name" maxlength="64" required> </div>
<div> Description: <br>
<textarea rows="4" cols="50" name="descr"></textarea> </div>
<div><fieldset>
<legend>Deadline date </legend>
Day: <input type="text" maxlength="2" size="2" name="day" required>
Month: <input type="text" maxlength="2" size="2" name="month" required>
Year: <input type="text" maxlength="4" size="4" name="year" required>
Time: <select name="hours" id="hours"> </select> <select name="minutes" id="minutes"> </select>
</fieldset></div>
<div>
<input type="checkbox" name="late_days" value="1" checked> Allow late days <br>
<input type="checkbox" name="active" value="1" checked> Is active </div>
<br>
<div> Files to upload: </div>
<ul id="list"> </ul>
<button type="button" onclick="addField()">+</button>
<button type="button" onclick="removeField()">-</button>
<div> <button> Submit </button> </div>
<script src='js/plusminus.js'> </script>
<script src='js/timechoice.js'> </script>
</form>
</body>
</html>
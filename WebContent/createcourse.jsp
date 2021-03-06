<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="hwu.datamodel.users.User"%>
<%@page import="hwu.datamodel.users.Lecturer"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create Course</title>
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
		Name: <input type="text" name="name" maxlength="64" required>
		<br> Description: <br>
		<textarea rows="4" cols="50" name="descr"></textarea>
		<br>
		<fieldset>
			<legend>Starting date </legend>
			Day: <input type="text" maxlength="2" size="2" name="fday" required>
			Month: <input type="text" maxlength="2" size="2" name="fmonth"
				required> Year: <input type="text" maxlength="4" size="4"
				name="fyear" required>
		</fieldset>
		<fieldset>
			<legend>Ending date </legend>
			Day: <input type="text" maxlength="2" size="2" name="eday" required>
			Month: <input type="text" maxlength="2" size="2" name="emonth"
				required> Year: <input type="text" maxlength="4" size="4"
				name="eyear" required>
		</fieldset>
		<input type="checkbox" name="late_days" value="1"> Allow late
		days <br> Enter number of late days here: <input type="text"
			name="late_day_num" size="2"> <br> Enter length of late
		days here: <input type="text" name="late_day_len" size="2"> <br>
		<p>
			<strong> Add other lecturers here: </strong> <br> (add fields
			and enter e-mails)
			<ul id="list">
			</ul>
			<input class="button" type="button" value="+" onclick="addField()">
			<input class="button" type="button" value="-" onclick="removeField()">
			<br>
		</p>
		<input class="button" type="submit" value="Continue to enrollment">
	</form>
</body>
<script src='js/plusminuslecturer.js'></script>
</html>
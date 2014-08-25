<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>კურსის შეყვანა</title>
</head>
<body>
<form action="Enroll"
enctype="multipart/form-data" method="post">
<p>
შეიყვანეთ კურსის ექსელ ფაილი:<br>
<input type="file" name="datafile" size="40">
</p>
<div>
<input type="hidden" name="course" value="<%= request.getParameter("course_id") %>">
</div>
<div>
<input type="submit" value="Send">
</div>
</form>

</body>
</html>
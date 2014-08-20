<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create Course</title>
</head>
<body>
<form action="CreateCourse" method="post">
Name: <input type="text" name="name" maxlength="64" required> <br>
Description: <br> 
<textarea rows="4" cols="50" name="descr"></textarea> <br>
<fieldset>
  <legend>Starting date </legend>
  Day: <input type="text" maxlength="2" size="2" name="fday"> 
  Month: <input type="text" maxlength="2" size="2" name="fmonth"> 
  Year: <input type="text" maxlength="4" size="4" name="fyear">
</fieldset>
<fieldset>
  <legend>Ending date </legend>
  Day: <input type="text" maxlength="2" size="2" name="eday"> 
  Month: <input type="text" maxlength="2" size="2" name="emonth"> 
  Year: <input type="text" maxlength="4" size="4" name="eyear">
</fieldset>
<input type="checkbox" name="late_days" value="1"> Allow late days <br>
Enter number of late days here: <input type="text" name="late_day_num" size="2"> <br>
Enter length of late days here: <input type="text" name="late_day_len" size="2"> <br>
<input class="button" type="submit" value="Continue to enrollment">
</form>
</body>
</html>
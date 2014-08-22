<%@page import="hwu.datamodel.HomeworkForm"%>
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
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<link rel="shortcut icon" href="images/favicon.ico" />
<link rel="stylesheet" type="text/css" href="css/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="refresh" content="240">
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
	String homeworkIdStr = request.getParameter("id");
	String courseIdStr = request.getParameter("course_id");
	if (user == null || homeworkIdStr == null || courseIdStr == null) {
		response.sendRedirect("index.jsp");
		return;
	}
	Integer homework_id = null;
	Integer course_id = null;
	try {
		homework_id = Integer.parseInt(homeworkIdStr);
		course_id = Integer.parseInt(courseIdStr);
	} catch (NumberFormatException e) {
		response.sendRedirect("index.jsp");
		return;
	}
	Homework thisHomework = hwManager.getHomework(homework_id);
	Course thisCourse = manager.getCourse(course_id);
	if (thisHomework == null
			|| thisCourse == null
			|| !manager.isAssociated(user, thisCourse)
			|| (!thisHomework.isActive() && !(user instanceof Lecturer))) {
		response.sendRedirect("index.jsp");
		return;
	}
%>
<title><%=thisHomework.getName() + " (" + thisCourse.getName()
					+ ")"%></title>
</head>
<body>
	<div class="content_wrapper">
		<div class="right">
			<%
				if (user instanceof Student)
					out.println("<a class=\"topright\" href=\"courses.jsp\">ჩემი კურსები</a>");
				if (userManager.isTutor(user))
					out.println("<a class=\"topright\" href=\"tutor.jsp\">სატუტორო კურსები</a>");
			%>
			<a class="topright" href="SignOut">სისტემიდან გასვლა (<%=user.getFirstName() + " " + user.getLastName()%>)
			</a>
		</div>
		<div class="left">
			<h2>
				დავალება #<%=thisHomework.getNumber() + ": " + thisHomework.getName()%></h2>
			<%
				Timestamp deadline = thisHomework.getDeadline();
				long currTime = System.currentTimeMillis();
				int lateDaysTaken = 0;
				boolean hasAlreadyWritten = false;
				if (thisHomework.latedaysDisabled()) {
					out.println("<h4>ამ დავალებაზე გადავადება არ გამოიყენება</h4>");
				} else if (user instanceof Student) {
					hasAlreadyWritten = hwManager.hasWrittenHomework(thisHomework,
							(Student) user);
					lateDaysTaken = ldManager.usedLateDaysForHomework(thisHomework,
							(Student) user);
					if (lateDaysTaken > 0) {
						int lateDayLength = thisCourse.getLateDaysLength();
						int totalDays = lateDaysTaken * lateDayLength;
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(deadline.getTime());
						cal.add(Calendar.DAY_OF_MONTH, totalDays);
						deadline = new Timestamp(cal.getTimeInMillis());
						out.print("<h4>თქვენი დედლაინი: ");
						out.print(deadline.toString());
						out.println("</h4>");
						out.print("<h4>თქვენ ამ დავალებისთვის გამოიყენეთ: ");
						out.print(lateDaysTaken + " ("
								+ thisCourse.getLateDaysLength()
								+ "-დღიანი) გადავადება");
						out.println("</h4>");
					}
					out.print("<h4>თქვენ დაგრჩათ: ");
					int lateDaysLeft = thisCourse.getLateDaysNumber()
							- lateDaysTaken;
					out.print(lateDaysLeft + " გადავადება");
					out.println("</h4>");
					if (lateDaysLeft > 0) {
						out.print("<form action=\"UseLateDay\" method=\"post\">");
						out.print("<input type=\"hidden\" name=\"hw\" value=\""
								+ thisHomework.getID() + "\">");
						out.print("<input type=\"hidden\" name=\"course\" value=\""
								+ thisCourse.getID() + "\">");
						out.print("<input type=\"submit\" value=\"გადავადების გამოყენება\">");
						out.println("</form>");
					}
				}
				if (user instanceof Student) {
					long deadlineTime = deadline.getTime();
					Timestamp submissionDate = null;
					if (hasAlreadyWritten) {
						submissionDate = hwManager.getHomeworkSubmissionDate(
								thisHomework, (Student) user);
						out.print("<h4>თქვენ დავალება გაგზავნილი გაქვთ ("
								+ submissionDate.toString() + ")!");
						if (currTime <= deadlineTime)
							out.print(" შენიშნვა: გასწორდება თქვენ მიერ მხოლოდ ბოლოს გამოგზავნილი დავალება");
						else
							out.println("<h4>");
						// shestavaze tavisi dzveli davalebis chamotvirtva
					}
					if (currTime > deadlineTime && !hasAlreadyWritten) {
						out.print("<h4>თქვენ დავალების დედლაინს გადააცილეთ!<h4>");
					}

					if (currTime < deadlineTime) {
						out.print("<h4>დედლაინამდე დარჩენილია: ");
						Timestamp diff = new Timestamp(currTime - deadlineTime);
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(deadlineTime - currTime);
						out.print(cal.get(Calendar.MONTH) + " თვე, ");
						out.print(cal.get(Calendar.DAY_OF_MONTH) + " დღე, ");
						out.print(cal.get(Calendar.HOUR_OF_DAY) + " საათი და ");
						out.print(cal.get(Calendar.MINUTE) + " წუთი ");
						out.println("</h4>");
					}
				}
			%>
			<h4>
				ოფიციალური დედლაინი:
				<%=thisHomework.getDeadline().toString()%></h4>
			<h5>
				კურსი: <a href='course.jsp?id=<%=thisCourse.getID()%>'> <%=thisCourse.getName()%></a>
			</h5>
			<h5>
				აღწერა:
				<%=thisHomework.getDescription()%></h5>
			<%
				if (user instanceof Lecturer) {
					out.println("<a href=\"edithomework.jsp?id="
							+ thisHomework.getID() + "&course_id="
							+ thisCourse.getID() + "\">კურსის ცვლილება</a>");
				}
			%>
			<%
				if (user instanceof Student) {
					if (deadline.getTime() > System.currentTimeMillis()) {
						// drawing upload form
						List<HomeworkForm> forms = hwManager
								.getHomeworkForms(thisHomework);
						out.println("<p>დავალების გაგზავნა</p>");
						out.println("<ul>");
						out.println("გასაგზავნი ფაილები: ");
						out.println("<form action=\"HomeworkUpload\" method=\"post\" enctype=\"multipart/form-data\">");
						for (HomeworkForm form : forms) {
							out.println("<li>");
							String emailCred = user.getEmail().toLowerCase();
							String firstName = user.getFirstName().toLowerCase();
							String lastName = user.getLastName().toLowerCase();
							String regex = form.getRegex();
							regex = regex.replaceAll(HomeworkForm.FIRST_NAME_EX,
									firstName);
							regex = regex.replaceAll(HomeworkForm.LAST_NAME_EX,
									lastName);
							regex = regex.replaceAll(HomeworkForm.INITIAL_EX, ""
									+ firstName.charAt(0));
							regex = regex.replace(HomeworkForm.GROUP_EX,
									"{თქვენი ჯგუფის ნომერი}");
							String extension = form.getFileExtension();
							int maxFileSize = form.getMaxFileSize();
							out.print("<input type=\"file\" name=\"file\" size=\"70\"");
							out.print("accept=\"" + extension + "\"/>");
							out.print(regex + extension);
							out.println("</li>");
						}
						out.println("<br/>");
						out.println("დამატებითი: ");
						// zip file
						out.println("<li>");
						out.println("<input type=\"file\" name=\"file\" size=\"70\"");
						out.print("accept=\".zip, .rar\"/>");
						out.println("ნებისმიერი .zip ან .rar გაფართოების ფაილი");
						out.println("</li>");
						out.println("<br/>");
						// some hidden values to be passed
						out.println("<input type=\"hidden\" name=\"hw\" value=\""
								+ thisHomework.getID() + "\"/>");
						out.println("<input type=\"hidden\" name=\"course\" value=\""
								+ thisCourse.getID() + "\"/>");
						// submit button
						out.println("<input type=\"submit\" value=\"დავალების გაგზავნა\"/>");
						if (hasAlreadyWritten)
							out.println("<p>ღილაკზე დაჭერის შემთხვევაში თქვენი ძველი დავალება წაიშლება</p>");
						out.println("</form>");
						out.println("</ul>");
					}
				}

				if (user instanceof Lecturer || userManager.isTutor(user)) {
					out.println("<ul>");
					for (Homework hw : hwManager.getHomework(thisCourse)) {
						// links to course pages
						if (hw.isActive() || user instanceof Lecturer) {
							out.println("<li><a href='homework.jsp?id="
									+ hw.getID() + "&course_id="
									+ thisCourse.getID() + "'>" + hw.getName()
									+ "</a></li>");
						}
					}
					out.println("</ul>");
				}
			%>
		</div>
	</div>
</body>
</html>
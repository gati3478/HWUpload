<%@page import="hwu.util.auth.GoogleAuthHelper"%>
<%@page import="hwu.datamodel.users.User"%>
<%@page import="hwu.datamodel.users.Student"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/main.css">
<link rel="stylesheet" type="text/css" href="css/auth.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Homework Manager</title>
</head>
<body>
	<img id="main_logo" src="images/logo_freeuni_ka.png">
	<%
		/*
		 * The GoogleAuthHelper handles all the heavy lifting, and contains all "secrets"
		 * required for constructing a google login url.
		 */
		User user = (User) session.getAttribute(User.ATTRIBUTE_NAME);
		if (user != null) {
			if (user instanceof Student)
				response.sendRedirect("studentcourses.jsp");
			else
				response.sendRedirect("acadyears.jsp");
			return;
		}
	%>
	<%
		if (request.getAttribute("error") != null) {
			request.removeAttribute("error");
			out.println("<p class=\"login-center\">Please, try again with Freeuni E-Mail<p>");
		}
	%>
	<div class="oauthbtn">
		<%
			final GoogleAuthHelper helper = (GoogleAuthHelper) application
					.getAttribute(GoogleAuthHelper.ATTRIBUTE_NAME);
			if (request.getParameter("code") == null
					|| session.getAttribute("state") == null) {
				/*
				 * Initial visit to the page
				 */
				out.println("<a href='" + helper.buildLoginUrl()
						+ "'>FreeUni Login</a>");

				/*
				 * Set the secure state token in session to be able to track what we sent to google
				 */
				session.setAttribute("state", helper.getStateToken());
			}
		%>
	</div>
</body>
</html>
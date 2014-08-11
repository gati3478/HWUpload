<%@page import="hwu.util.auth.GoogleAuthHelper"%>
<%@page import="hwu.datamodel.users.User"%>
<%@page import="hwu.datamodel.users.Student"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Homework Manager</title>
</head>
<body>
	<div class="oauthDemo">
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
			final GoogleAuthHelper helper = new GoogleAuthHelper();

			if (request.getParameter("code") == null
					|| request.getParameter("state") == null) {
				/*
				 * Initial visit to the page
				 */
				out.println("<a href='" + helper.buildLoginUrl()
						+ "'>log in with google</a>");

				/*
				 * Set the secure state token in session to be able to track what we sent to google
				 */
				session.setAttribute("state", helper.getStateToken());
			}
		%>
	</div>
</body>
</html>
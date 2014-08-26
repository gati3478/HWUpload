package hwu.servlet.auth;

import hwu.datamodel.users.Lecturer;
import hwu.datamodel.users.Student;
import hwu.datamodel.users.User;
import hwu.db.managers.UserManager;
import hwu.util.auth.GoogleAuthHelper;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Servlet implementation class GoogleSign
 */
@WebServlet("/GoogleSign")
public class GoogleSign extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GoogleSign() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (request.getParameter("code") != null
				&& request.getParameter("state") != null
				&& request.getParameter("state").equals(
						session.getAttribute("state"))) {
			final GoogleAuthHelper helper = (GoogleAuthHelper) getServletContext()
					.getAttribute(GoogleAuthHelper.ATTRIBUTE_NAME);
			session.removeAttribute("state");

			// building JSON containing user information
			String info = helper.getUserInfoJson(request.getParameter("code"));
			JsonElement jelement = new JsonParser().parse(info);
			JsonObject jobject = jelement.getAsJsonObject();
			// String id = jobject.get("id").getAsString();
			String email = jobject.get("email").getAsString();
			// we don't store whole email, credentials only
			int atSymbolPos = email
					.indexOf('@' + GoogleAuthHelper.HOSTED_DOMAIN);
			if (atSymbolPos == -1) {
				request.getSession().setAttribute("login_error", new String());
				response.sendRedirect("index.jsp");
			} else {
				User user = null;
				String email_cred = email.substring(0, atSymbolPos);
				String firstName = jobject.get("given_name").getAsString();
				String lastName = jobject.get("family_name").getAsString();
				boolean isStudent = UserManager.isStudentEmail(email_cred);
				// constructing new user
				if (isStudent)
					user = new Student(email_cred, firstName, lastName);
				else
					user = new Lecturer(email_cred, firstName, lastName);
				// adding user to the database (if it didn't exist before)
				UserManager userManager = (UserManager) request
						.getServletContext().getAttribute(
								UserManager.ATTRIBUTE_NAME);
				try {
					userManager.tryAddUser(user);
					user = userManager.getUser(email_cred);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// saving user in session
				request.getSession().setAttribute(User.ATTRIBUTE_NAME, user);
				// redirecting to appropriate page
				if (user instanceof Student)
					response.sendRedirect("courses.jsp");
				else
					response.sendRedirect("acadyears.jsp");
			}
		}
	}

}

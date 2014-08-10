package hwu.servlet.auth;

import hwu.datamodel.users.Lecturer;
import hwu.datamodel.users.Student;
import hwu.datamodel.users.User;
import hwu.db.managers.UserManager;
import hwu.util.auth.GoogleAuthHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
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
			final GoogleAuthHelper helper = new GoogleAuthHelper();
			PrintWriter out = response.getWriter();
			session.removeAttribute("state");

			String info = helper.getUserInfoJson(request.getParameter("code"));
			JsonElement jelement = new JsonParser().parse(info);
			JsonObject jobject = jelement.getAsJsonObject();
			String id = jobject.get("id").getAsString();
			String email = jobject.get("email").getAsString();
			int atSymbolPos = email
					.indexOf('@' + GoogleAuthHelper.HOSTED_DOMAIN);
			String email_cred = email.substring(0, atSymbolPos);
			String firstName = jobject.get("given_name").getAsString();
			String lastName = jobject.get("family_name").getAsString();
			String hostedDomain = jobject.get("hd").getAsString();
			out.print(hostedDomain);
			if (!hostedDomain.equals(GoogleAuthHelper.HOSTED_DOMAIN)) {
				request.setAttribute("error", true);
				RequestDispatcher dispatcher = request
						.getRequestDispatcher("index.jsp");
				dispatcher.forward(request, response);
			} else {
				User user = null;
				boolean isStudent = false;
				if (email.length() == 22 && Character.isDigit(email.charAt(5))
						&& Character.isDigit(email.charAt(6)))
					isStudent = true;
				if (isStudent)
					user = new Student(email, firstName, lastName, false);
				else
					user = new Lecturer(email, firstName, lastName, false);
				UserManager userManager = (UserManager) request
						.getServletContext().getAttribute(
								UserManager.ATTRIBUTE_NAME);
				try {
					userManager.tryAddUser(user);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				out.print(jobject.toString());
			}
		}
	}

}

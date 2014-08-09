package hwu.servlet.auth;

import hwu.util.auth.GoogleAuthHelper;

import java.io.IOException;
import java.io.PrintWriter;

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
			String id = jobject.get("id").toString();
			String email = jobject.get("email").toString();
			String firstName = jobject.get("given_name").toString();
			String lastName = jobject.get("family_name").toString();
			String hostedDomain = jobject.get("hd").toString();

			if (!hostedDomain.equals(GoogleAuthHelper.HOSTED_DOMAIN)) {
				request.setAttribute("error", true);
				RequestDispatcher dispatcher = request
						.getRequestDispatcher("index.jsp");
				dispatcher.forward(request, response);
			} else {
				out.println(id);
				out.println(email);
				out.println(firstName);
				out.println(lastName);
				out.println(hostedDomain);
				out.print(jobject.toString());
			}
		}
	}

}

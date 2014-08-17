package hwu.listener;

import hwu.db.DBInfo;
import hwu.db.managers.CourseManager;
import hwu.db.managers.HomeworkManager;
import hwu.db.managers.UserManager;
import hwu.util.auth.GoogleAuthHelper;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;

/**
 * Application Lifecycle Listener implementation class ContextListener
 *
 */
@WebListener
public class ContextListener implements ServletContextListener {

	/**
	 * Default constructor.
	 */
	public ContextListener() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		try {
			AbandonedConnectionCleanupThread.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource dataSource = (DataSource) envContext.lookup("jdbc/"
					+ DBInfo.DB_NAME);
			UserManager userManager = new UserManager(dataSource);
			HomeworkManager hwManager = new HomeworkManager(dataSource);
			CourseManager courseManager = new CourseManager(dataSource);
			GoogleAuthHelper oauth2helper = new GoogleAuthHelper();
			ServletContext servletContext = arg0.getServletContext();
			servletContext.setAttribute("DataSource", dataSource);
			servletContext
					.setAttribute(UserManager.ATTRIBUTE_NAME, userManager);
			servletContext.setAttribute(CourseManager.ATTRIBUTE_NAME,
					courseManager);
			servletContext.setAttribute(HomeworkManager.ATTRIBUTE_NAME,
					hwManager);
			servletContext.setAttribute(GoogleAuthHelper.ATTRIBUTE_NAME,
					oauth2helper);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

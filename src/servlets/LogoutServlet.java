package servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.cs361.Form;
/**
 * This Servlet class handles the logoff from the CMS system, and deletes the cookie.
 * @author 5guys
 */
@SuppressWarnings("serial")
public class LogoutServlet extends HttpServlet
{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		Form.deleteCookie(resp);
			
		resp.sendRedirect("index.html");
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		doGet(req, resp);
	}
}

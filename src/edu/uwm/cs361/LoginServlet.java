package edu.uwm.cs361;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet
{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/html");

		resp.getWriter().println("<form action='/login' method='POST'>");
		resp.getWriter().println("<input type='text' name='name' />");
		resp.getWriter().println("<input type='submit' value='Login' />");
		resp.getWriter().println("</form>");
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		Cookie c = new Cookie("username", req.getParameter("name"));

		resp.addCookie(c);
		resp.sendRedirect("/user");
	}
}

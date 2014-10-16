package edu.uwm.cs361;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class LogoutServlet extends HttpServlet
{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		Cookie c = new Cookie("username", null);
		c.setMaxAge(0);

		resp.addCookie(c);
		resp.sendRedirect("/index.html");
	}
}

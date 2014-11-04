package edu.uwm.cs361;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class UserSearchServlet extends HttpServlet
{
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
		resp.sendRedirect("user-search.html");
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		doGet(req, resp);
	}
}
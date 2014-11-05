package edu.uwm.cs361;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ViewCoursesServlet extends HttpServlet
{
	private HttpServletResponse _resp;
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
		//_resp = resp;
		resp.sendRedirect("courses.html");
		printContents();
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		doGet(req, resp);
	}
	
	private void printContents() throws IOException {
		
		//_resp.getWriter().println( );
	}
}
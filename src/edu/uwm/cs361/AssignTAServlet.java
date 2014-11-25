package edu.uwm.cs361;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This Servlet class assigns a Teaching Assistant to the CMS system. 
 * @author 5guys
 */
@SuppressWarnings("serial")
public class AssignTAServlet extends HttpServlet
{
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
		// WE MAY DECIDE TO COMBINE WITH ASSIGN PROF
		resp.sendRedirect("assign-ta.html");
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		doGet(req, resp);
	}
}
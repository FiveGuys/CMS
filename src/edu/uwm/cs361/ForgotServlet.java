package edu.uwm.cs361;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ForgotServlet extends HttpServlet
{
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
		
		Datastore.addAdmin();
		
		Datastore ds = new Datastore(req, resp, new ArrayList<String>(0));
		
		resp.getWriter().println();
		resp.getWriter().println("Created One Admin");
		
		//resp.sendRedirect("forgot.html");
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		doGet(req, resp);
	}
}
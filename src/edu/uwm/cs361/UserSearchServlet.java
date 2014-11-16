package edu.uwm.cs361;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class UserSearchServlet extends HttpServlet implements CallBack
{
	//TODO this may need to change since we are allowing Admin to edit account info on this page...
	private final int ACCESS_LEVEL = ACCESS_ALL;
	
	private HttpServletRequest _req;
	
	private HttpServletResponse _resp;
	
	private List<String> _errors;
	
	private Form _form;
	
	private Map<String, String> param;
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
		//resp.sendRedirect("user-search.html");//most likely removing this when done
		
		_req = req;
		
		_resp = resp;
		
		_errors = new ArrayList<String>();
		
		_form = new Form(_req, _resp, _errors);
		
		param = _form.getParameters();
		
		_form.handleGet("Search For Users", 4, this, "searchUser", ACCESS_LEVEL);
		
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		doGet(req, resp);
	}

	@Override
	public void printContent() throws IOException {
		// TODO Finish this shit
		_resp.getWriter().println(
			"<div id='left-nav'>" +
				"<form action='user-search' method='post' class='standard-form'>"+
					"<h1 class='heading'>Search by Name:</h1>" +

					"<label for='firstname'>First Name:</label>" +
					"<input type='text' name='FirstName'><br>" +
					"<label for='lastname'>Last Name:</label>" +
					"<input type='text' name='lastname'><br>" +
					_form.EndSearchUser() +
			"</div>" +

			"<div id='main-section'>" +
				"<img src='images/People.png' alt='PeopleIcon' height='30' width='30'>" +
				
					"<div class='name-heading'></div>" +
					"<br /><br />" +
					 "<label>Email:</label>" +
					 "<div class='boxedResult'></div>" +
					 "<br>" +

					"<label>Phone:</label>" +
					"<div class='boxedResult'></div>" +
					"<br>" +
					"<label>Office:</label>" +
					"<div class='boxedResult'></div>" +
					"<br>" +
					"<label>Position:</label>" +
					"<div class='boxedResult'></div>" +
			"</div>" +
			"<div class='clear'></div>"
			
		);

	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		
	}
}

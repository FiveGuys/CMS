package edu.uwm.cs361;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class UserSearchServlet extends HttpServlet implements CallBack
{
	private final int ACCESS_LEVEL = ACCESS_ALL;
	
	private HttpServletRequest _req;
	
	private HttpServletResponse _resp;
	
	private List<String> _errors;
	
	private Form _form;
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
		_req = req;
		
		_resp = resp;
		
		_errors = new ArrayList<String>();
		
		_form = new Form(_req, _resp, _errors);
		
		_form.handleGet("Search For Users", 4, this, "searchUser", ACCESS_LEVEL);
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		doGet(req, resp);
	}

	@Override
	public void printContent() throws IOException {
		// TODO Finish this shit
		// For admin should be able to edit the fields or should redirect to edit info?
		// Everybody else is read only
		// CSS Styling
		
		Boolean userFound = (_form.isSubmit() && _errors.size() == 0);
		
		String html = 
				"<div id='left-nav' "+(!userFound ? "class='notFound'" : "")+">" +
					"<form action='user-search' method='post' class='standard-form'>"+
					"<h1 class='heading'>Search by Name</h1>" +
					
					"<label for='firstname'>First Name:</label>" +
					"<input type='text' name='FirstName'><br>" +
					"<label for='lastname'>Last Name:</label>" +
					"<input type='text' name='lastname'><br>" +
					_form.EndSearchUser() +
				"</div>";
		
		// Display info here...
		// Display name also...
		if(userFound) {

			Datastore ds = new Datastore(_req, _resp, _errors);
			
			User user = ds.getUser();
			
			html += "<div id='main-section'>" +
				"<img src='images/People.png' alt='PeopleIcon' height='30' width='30'>" +
				
					"<div class='name-heading'></div>" +
					"<br /><br />" +
					 "<label>Email:</label>" +
					 "<div class='boxedResult'>"+user.getEmail()+"</div>" +
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
			"<div class='clear'></div>";
		}
		
		_resp.getWriter().println(html);
	}

	@Override
	public void validate() {}
	
}
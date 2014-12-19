package servlets;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.cs361.CallBack;
import edu.uwm.cs361.Form;

/**
 * This Servlet class adds a user into the system. 
 * @author 5guys
 */
@SuppressWarnings("serial")
public class AddUserServlet extends HttpServlet implements CallBack
{
	private final int ACCESS_LEVEL = ACCESS_ADMIN;
	
	private HttpServletRequest _req;
	
	private HttpServletResponse _resp;
	
	private List<String> _errors;
	
	private Form _form;
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
   
		_req = req;
		
		_resp = resp;
		
		_errors = new ArrayList<String>();
		
		_form = new Form(_req, _resp, _errors);
		
		_form.handleGet("Add User", 0, this, "addUser", ACCESS_LEVEL);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		doGet(req, resp);
	}

	/**
	 * Prints content
	 */
	@Override
	public void printContent() throws IOException {
		
		_resp.getWriter().println( 
				
			"<form action='/add-user' method='post' class='standard-form'>" + 
				"<h1>Add User</h1>" +
				"<label>" +
					"<span>First Name:</span>" +
					"<input type='text' name='FirstName' value='"+getParam("FirstName")+"' />" +
				"</label>" +
				"<label>" +
				  "<span>Last Name: </span>" +
				  "<input type='text' name='LastName' value='"+getParam("LastName")+"' />" +
				"</label>" +
				"<label>" +
				  "<span>Email: </span>" +
				  "<input type='Email' name='Email' value='" + getParam("Email")+ " ' placeholder='xxx@uwm.edu' />" +
				"</label>" +				
				"<textarea style='height: 45px; width: 500px' placeholder='If you are adding a TA, please enter in their skills separated by semi colons. example: HTML; CSS; Eating; Java' name='taSkills' rows='4' cols='50'>" + 
			    "</textarea>" +
				"<label>" +   
				  "<span>Access Level: </span>" +
					"<select name='Access'>" +
						"<option value='3' "+ selected("3") +">Administrator</option>" +
						"<option value='2' "+ selected("2") +">Professor</option>" +
						"<option value='1' "+ selected("1") +">TA</option>" +
					"</select>" +
				"</label>" +
			"<div class='submit'><input type='submit' name='submit' class='button' value='Save' /></div>" +
			"</form>"
		);
	}

	/**
	 * Calls all validation methods to make sure that every single field entered is valid.
	 */
	@Override
	public void validate() {
		
		isRequired(_req.getParameter("FirstName"), "First Name");
		isRequired(_req.getParameter("LastName"), "Last Name");
		isRequired(_req.getParameter("Email"), "Email");
		checkValidUwmEmail(_req.getParameter("Email"));
	}
	
	/**
	 * Checks if the current field is required
	 * @param param
	 * @param name
	 */
	public void isRequired(String param, String name) {
		
		if (param.isEmpty()) {
			
			_errors.add(name + " is required");
		}
	}
	
	/**
	 * Checks if it is a valid UWM smtp address
	 * @param email
	 */
	private void checkValidUwmEmail(String email){
		if((email.indexOf('@') != -1) && (!email.substring(email.indexOf('@')).equalsIgnoreCase("@uwm.edu")))
			_errors.add("Please enter a valid uwm email address");
	}
	
	/**
	 * @param name
	 * @return name parameter if is not null and if the _errors list is empty. Otherwise returns an empty string.
	 */
	private String getParam(String name) {
		
		return (_req.getParameter(name) != null && _errors.size() != 0 ? 
				_req.getParameter(name) : "");
	}
	
	/**
	 * @param index
	 * @return string "selected" if equal to index parameter. Otherwise returns an empty string.
	 */
	private String selected(String index) {
		
		return (getParam("Access").equals(index) ? "selected" : "");
	}
}

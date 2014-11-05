package edu.uwm.cs361;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class AddUserServlet extends HttpServlet implements CallBack
{
	private final int ACCESS_LEVEL = ACCESS_ADMIN;
	
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
		
		_form.handleGet("Add User", 0, this, "addUser", ACCESS_LEVEL);
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		doGet(req, resp);
	}

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

	@Override
	public void validate() {
		
		isRequired(_req.getParameter("FirstName"), "First Name");
		
		isRequired(_req.getParameter("LastName"), "Last Name");
	}
	
	public void isRequired(String param, String name) {
		
		if (param.isEmpty()) {
			
			_errors.add(name + " is required");
		}
	}
	
	private String getParam(String name) {
		
		return (_req.getParameter(name) != null && _errors.size() != 0 ? 
				_req.getParameter(name) : "");
	}
	
	private String selected(String index) {
		
		return (getParam("Access").equals(index) ? "selected" : "");
	}
}

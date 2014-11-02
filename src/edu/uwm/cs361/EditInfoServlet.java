package edu.uwm.cs361;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("serial")
public class EditInfoServlet extends HttpServlet implements CallBack
{
	private final int ACCESS_LEVEL = 1;
	
	private HttpServletRequest _req;
	
	private HttpServletResponse _resp;
	
	private List<String> _errors;
	
	private Form form;
	
	private Map<String, String> param;
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		_req = req;
		
		_resp = resp;
		
		_errors = new ArrayList<String>();
		
		form = new Form(_req, _resp, _errors);
		
		param = form.getParameters();
		
		form.handleGet("Edit Your Information", 3, this, "updateInfo", ACCESS_LEVEL);
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		doGet(req, resp);
	}
	
	@Override
	public void printContent()  throws IOException {

		_resp.getWriter().println( 
				
				"<form action='edit-info' method='post' class='standard-form'>" +
				
				"<h1 class='heading'></h1>" +
				
				"<div id= 'image'>" +
					"<img src='images/blank-picture.png'/><br />" +
					"<button class='simple'>Change Photo</button>" +
				"</div>" +

				"<div id='name-form'>" +
					"First Name <br/>" +
					"<input type='text' name='firstName' value=''><br/>" +
					"Middle Name <br/>" +
					"<input type='text' name='middleName' value=''><br/>" +
					"Last Name<br/>" +
					"<input type='text' name='lastName' value=''><br/>" +
				"</div>" +
				
				"<div class='section'>Contact Information</div>" +
				
				"<div id='contact-form'>" +
					"Email<br/><input type='text' name='email'  value=''><br/>" +
					"Office Phone<br/><input type='text' name='phone'  placeholder='XXX-XXX-XXXX'>" +
				"</div>" +

				"<div id='contact-form2'>" +
					"Office Location<br/><input type='text' name='location' value=''><br/>" +
					"Alt Phone<br/><input type='text' name='altPhone' placeholder='XXX-XXX-XXXX'>" +
				"</div>" +
				
				"<div class='section'>Office Hours</div>" +
				
				"<div id='contact-form3'>" +
				
					form.printOfficeHours(1) +
					
					form.printOfficeHours(2) +
					
					form.printOfficeHours(3) +
					
				"</div>" +
					
				form.End()
		);
	}
	
	@Override
	public void validate() {
		
	}
}

package edu.uwm.cs361;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class EditInfoServlet extends HttpServlet implements CallBack
{
	private final int ACCESS_LEVEL = ACCESS_ALL;
	
	private HttpServletRequest _req;
	
	private HttpServletResponse _resp;
	
	private List<String> _errors;
	
	private Form _form;
	
	private Map<String, String> param;
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		_req = req;
		
		_resp = resp;
		
		_errors = new ArrayList<String>();
		
		_form = new Form(_req, _resp, _errors);
		
		param = _form.getParameters();
		
		_form.handleGet("Edit Your Information", 3, this, "updateUser", ACCESS_LEVEL);
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		doGet(req, resp);
	}
	
	@Override
	public void printContent()  throws IOException {

		_resp.getWriter().println( 

				"<form action='edit-info' method='post' class='standard-form'>" +
				
				"<h1 class='heading'>" +
					param.get("FirstName") + " " +
					param.get("MiddleName") + " " +
					param.get("LastName") +
				"</h1>" +
				
				"<div id= 'image'>" +
					"<img src='images/blank-picture.png'/><br />" +
					"<button class='simple'>Change Photo</button>" +
				"</div>" +

				"<div id='name-form'>" +
					"First Name <br/>" +
					"<input type='text' name='FirstName' readonly='readonly' value='"+param.get("FirstName")+"'><br/>" +
					"Middle Name <br/>" +
					"<input type='text' name='MiddleName' value='"+param.get("MiddleName")+"'><br/>" +
					"Last Name<br/>" +
					"<input type='text' name='LastName' readonly='readonly' value='"+param.get("LastName")+"'><br/>" +
				"</div>" +
				
				"<div class='section'>Contact Information</div>" +
				
				"<div id='contact-form'>" +
					"Email<br/><input type='text' name='Email'  value='"+param.get("Email")+"'><br/>" +
					"Office Phone<br/><input type='text' name='Phone' value='"+param.get("Phone")+"'  placeholder='XXX-XXX-XXXX'>" +
				"</div>" +

				"<div id='contact-form2'>" +
					"Office Location<br/><input type='text' name='Location' value='"+param.get("Location")+"'><br/>" +
					"Alt Phone<br/><input type='text' name='AltPhone' value='"+param.get("AltPhone")+"' placeholder='XXX-XXX-XXXX'>" +
				"</div>" +
				
				"<div class='section'>Office Hours</div>" +
				
				"<div id='contact-form3'>" +
				
					printOfficeHours(1) +
					
					printOfficeHours(2) +
					
					printOfficeHours(3) +
					
				"</div>" +
					
				_form.End()
		);
	}
	
	@Override
	public void validate() {
		
		isEmail(param.get("Email"));
		
		isPhone(param.get("Phone"), "Office Phone");
		
		isPhone(param.get("AltPhone"), "Alt Phone");
	}
	
	private void isEmail(String email) {
		
		isValid(email, "Email", "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"); 
	}
	
	private void isPhone(String phone, String name) {
		
		isValid(phone, name, "\\d{3}-\\d{3}-\\d{4}");
	}
	
	private void isValid(String param, String name, String regex) {

		if(!param.isEmpty()) {
			
			Pattern pattern = Pattern.compile(regex);  
			Matcher matcher = pattern.matcher(param);

			if(!matcher.matches()) {

				_errors.add(name + " is invalid");
			}
		}
	}
	
	private String printOfficeHours(int index) {

		List<String> officeDay = Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri");
		
		String[] arr = "Wed;0;00;0;00".split(";");
		
		if(param.get("OfficeHour" + index) != null) {
			
			arr = param.get("OfficeHour" + index).split(";");
			
		}
		
		return "Office Hour "+index+"<br/>" +
				
			_form.getSelectField("office-day-"+index, arr[0], "", officeDay) +
			
			_form.selectTime("office-hours-"+index+"-start-1", "office-hours-"+index+"-end-1", arr[1], arr[2], "") +
					"&nbsp; - &nbsp;" + 
			_form.selectTime("office-hours-"+index+"-start-2", "office-hours-"+index+"-end-2", arr[3], arr[4], "") +
			
			"</br></br>";	
	}
}

package edu.uwm.cs361;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
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
					"<input type='text' name='FirstName' value='"+param.get("FirstName")+"'><br/>" +
					"Middle Name <br/>" +
					"<input type='text' name='MiddleName' value='"+param.get("MiddleName")+"'><br/>" +
					"Last Name<br/>" +
					"<input type='text' name='LastName' value='"+param.get("LastName")+"'><br/>" +
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
					
				form.End()
		);
	}
	
	@Override
	public void validate() {
		
		isEmpty(param.get("FirstName"), "First Name");
		
		isEmpty(param.get("LastName"), "Last Name");
		
		isEmail(param.get("Email"));
		
		isPhone(param.get("Phone"));
		
		isPhone(param.get("AltPhone"));
	}
	
	private String printOfficeHours(int index) {

		List<String> officeDay = Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri");
		
		String[] arr = "Wed;0;00;0;00".split(";");
		
		if(param.get("OfficeHour" + index) != null) {
			
			arr = param.get("OfficeHour" + index).split(";");
			
		} else {
			
			System.out.println("I got null");
		}
		
		return "Office Hour "+index+"<br/>" +
				
			form.getSelectField("office-day-"+index, arr[0], "", officeDay) +
			
			form.selectTime("office-hours-"+index+"-start-1", "office-hours-"+index+"-end-1", arr[1], arr[2], "") +
					"&nbsp; - &nbsp;" + 
			form.selectTime("office-hours-"+index+"-start-2", "office-hours-"+index+"-end-2", arr[3], arr[4], "") +
			
			"</br></br>";	
	}
	
	private void isEmail(String email) {
		
		try {

			InternetAddress internetAddress = new InternetAddress(email);
			internetAddress.validate();
			
		} catch (AddressException e) {
			
			_errors.add("Email is invalid format");
		}
	}
	
	private void isPhone(String phone) {
		
		Pattern pattern = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");  
		Matcher matcher = pattern.matcher(phone);
		
		if(!matcher.matches()) {
			_errors.add("Phone is invalid format");
		}
	}
	
	private void isEmpty(String str, String errorName) {
		
		if(str.isEmpty()) {
			
			_errors.add(errorName + " is required");
		}
	}
}

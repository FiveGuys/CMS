package edu.uwm.cs361;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

/**
 * This Servlet class edits a user's general information.
 * @author 5guys
 */
@SuppressWarnings("serial")
public class EditInfoServlet extends HttpServlet implements CallBack
{
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	
	private final int ACCESS_LEVEL = ACCESS_ALL;
	
	private HttpServletRequest _req;
	
	private HttpServletResponse _resp;
	
	private List<String> _errors;
	
	private Form _form;
	
	private User _user = null;
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		_req = req;
		
		_resp = resp;
		
		_errors = new ArrayList<String>();
		
		_form = new Form(_req, _resp, _errors);
		
		_form.handleGet("Edit Your Information", 3, this, "updateUser", ACCESS_LEVEL);
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		doGet(req, resp);
	}
	
	/**
	 * Prints Datastore content
	 */
	@Override
	public void printContent()  throws IOException {

		Datastore ds = new Datastore(_req, _resp, _errors);
		
		_user = ds.getUser();
		
		_resp.getWriter().println( 
//TODO remove Javascript - DL
				"<div id= 'image'>" +
						(_user.getImage() == null ? 
								"<img src='images/blank-picture.png'/>" 
								: "<img src='/serve?blob-key="+_user.getImage()+"' />")	+
								
						"<form action="+blobstoreService.createUploadUrl("/upload") +" method='post' enctype='multipart/form-data'>" +
							"<input type='file' name='myFile' onchange='javascript:this.form.submit()'>" +
						"</form>"+
					"</div>" +
						
				"<form action='edit-info' method='post' class='standard-form'>" +
				
				"<h1 class='heading'>" +
					setField(_user.getFirstName(), "FirstName") + " " +
					setField(_user.getMiddleName(), "MiddleName") + " " +
					setField(_user.getLastName(), "LastName") +
				"</h1>" +
				
				"<div id='name-form'>" +
					"First Name <br/>" +
					"<input type='text' name='FirstName' readonly='readonly' value='"+setField(_user.getFirstName(), "FirstName")+"'><br/>" +
					"Middle Name <br/>" +
					"<input type='text' name='MiddleName' value='"+setField(_user.getMiddleName(), "MiddleName")+"'><br/>" +
					"Last Name<br/>" +
					"<input type='text' name='LastName' readonly='readonly' value='"+setField(_user.getLastName(), "LastName")+"'><br/>" +
				"</div>" +
				
				"<div class='section'>Contact Information</div>" +
				
				"<div id='contact-form'>" +
					"Email<br/><input type='text' name='Email'  value='"+setField(_user.getEmail(), "Email")+"'><br/>" +
					"Office Phone<br/><input type='text' name='Phone' value='"+setField(_user.getPhone(),"Phone")+"'  placeholder='XXX-XXX-XXXX'>" +
				"</div>" +

				"<div id='contact-form2'>" +
					"Office Location<br/><input type='text' name='Location' value='"+setField(_user.getLocation(), "Location")+"'><br/>" +
					"Alt Phone<br/><input type='text' name='AltPhone' value='"+setField(_user.getAltPhone(),"AltPhone")+"' placeholder='XXX-XXX-XXXX'>" +
				"</div>" +
				
				"<div class='section'>Office Hours</div>" +
				
				"<div id='contact-form3'>" +
				
					printOfficeHours(1) +
					
					printOfficeHours(2) +
					
					printOfficeHours(3) +
					
				"</div>" +
					
				"<div id='password'>" +

					printPassword("Old") +
					
					printPassword("New") +
					
				"</div>" +
				
				_form.End()
		);
	}
	
	/**
	 * Sets a form field.
	 * @param userField
	 * @param param
	 * @return
	 */
	private String setField(String userField, String param) {
		
		return (!_form.isSubmit() || _errors.size() == 0) ? 
				 userField : _form.getParam(param);
	}
	
	/**
	 * Sets Office Hours field.
	 * @param userField
	 * @param index
	 * @return
	 */
	private String setOfficeHours(String userField, int index) {
		
		return (!_form.isSubmit() || _errors.size() == 0) ? 
				 userField : Form.calcOfficeHours(index, _req);
	}

	/**
	 * Calls all validation methods to make sure that every single field entered is valid.
	 */
	@Override
	public void validate() {
		
		isEmail("Email");
		
		isPhone("Phone", "Office Phone");
		
		isPhone("AltPhone", "Alt Phone");
		
		isOfficeHour(1);
		
		isOfficeHour(2);
		
		isOfficeHour(3);
		
		isOfficeHourOverlap();
		
		isPassword();
	}
	
	/**
	 * Checks that the email address provided follows the smtp address format.
	 * @param email
	 */
	private void isEmail(String email) {
		
		isValid(email, "Email", "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"); 
	}
	
	/**
	 * Checks that the phone number provided follows the US phone number standard.
	 * @param phone
	 * @param name
	 */
	private void isPhone(String phone, String name) {
		
		isValid(phone, name, "\\d{3}-\\d{3}-\\d{4}");
	}
	
	/**
	 * Compares the regular expressions for validation purposes.
	 * @param param
	 * @param name
	 * @param regex
	 */
	private void isValid(String param, String name, String regex) {

		String temp = _req.getParameter(param);
		
		if(!temp.isEmpty()) {
			
			Pattern pattern = Pattern.compile(regex);  
			Matcher matcher = pattern.matcher(temp);

			if(!matcher.matches()) {

				_errors.add(name + " is invalid");
			}
		}
	}
	
	/**
	 * Checks that the office hours entered are in a valid time format.
	 * @param index
	 */
	private void isOfficeHour(int index) {
		
		String[] temp = Form.calcOfficeHours(index, _req).split(";");

		if((Integer.parseInt(temp[1]) == Integer.parseInt(temp[3]) &&
				Integer.parseInt(temp[2]) > Integer.parseInt(temp[4])) ||
				Integer.parseInt(temp[1]) > Integer.parseInt(temp[3])) {

			_errors.add("Office Hour "+index+" is invalid");
		}
	}
	
	/**
	 * Checks that the office hours selected are not already in use.
	 */
	private void isOfficeHourOverlap() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Matches with the old password.
	 */
	private void isPassword() {
		
		String old = _req.getParameter("PasswordOld");
		old.trim();
		if((!_user.getPassword().equals(old) && (!old.equalsIgnoreCase("")))) {
			
			_errors.add("Old Password doesn't match");
		}
	}
	
	/**
	 * Prints office hours.
	 * @param index
	 * @return
	 */
	private String printOfficeHours(int index) {

		List<String> officeDay = Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri");
		
		String officeHour = setOfficeHours(_user.getOfficeHour(1), index);
		
		String[] arr = officeHour.split(";");
		
		return "Office Hour "+index+"<br/>" +
				
			_form.getSelectField("office-day-"+index, arr[0], "", officeDay) +
			
			_form.selectTime("office-hours-"+index+"-start-1", "office-hours-"+index+"-end-1", arr[1], arr[2], "") +
					"&nbsp; - &nbsp;" + 
			_form.selectTime("office-hours-"+index+"-start-2", "office-hours-"+index+"-end-2", arr[3], arr[4], "") +
			
			"</br></br>";	
	}
	
	/**
	 * This method prints a password.
	 * @param name
	 * @return
	 */
	private String printPassword(String name) {
	
		return name+" Password<br/><input type='text' name='Password"+name+"' /><br/>";
	}
}

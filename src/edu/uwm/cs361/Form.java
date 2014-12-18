package edu.uwm.cs361;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdo.Course;

/**
 * This class manages the webpage view.
 * @author 5guys
 */
public class Form
{
	private HttpServletRequest _req;
	
	private HttpServletResponse _resp;
	
	private List<String> _errors;
		
	public Form(HttpServletRequest req, HttpServletResponse resp, List<String> errors) {
		
		_req = req;
		
		_resp = resp;
		
		_errors = errors;
	}

	/**
	 * Displays the form.
	 * @param header
	 * @param page
	 * @param servlet
	 * @throws IOException
	 */
	private void displayForm(String header, int page, CallBack servlet) throws IOException {
		
		printHeader(header, page);
		
		printMessage();
		
		servlet.printContent();
		
		printFooter();
	}
	
	/**
	 * If the access level requirement is satisfied, it creates a new datastore, validates the servlet and calls {@link #displayForm displayForm}.
	 * @param header
	 * @param page
	 * @param servlet
	 * @param method
	 * @param accessLevel
	 * @throws IOException
	 */
	public void handleGet(String header, int page, CallBack servlet, String method, int accessLevel) throws IOException {
		
		if(checkAccess(accessLevel)) {
		
			if(isSubmit()) {
				
				Datastore ds = new Datastore(_req, _resp, _errors);
				
				servlet.validate();
				
				ds.callMethod(method);
			}
			
			displayForm(header, page, servlet);
		}
	}
	
 	/**Prints the header.
 	 * @param title
 	 * @param page
 	 * @throws IOException
 	 */
 	public void printHeader(String title, int page) throws IOException {
		
 		HtmlOutputHelper.printHeader(_req, _resp, title, page);
 	}
 	
 	/**
 	 * Prints the footer.
 	 * @throws IOException
 	 */
 	public void printFooter() throws IOException {
 		
 		HtmlOutputHelper.printFooter(_resp);
 	}
 	
 	/**
 	 * Prints a message.
 	 * @throws IOException
 	 */
 	public void printMessage() throws IOException{
 		
 		PrintWriter out = _resp.getWriter();
 		
 		if(_errors.size() == 0 && _req.getParameter("submit") != null) {
 			
 			out.println("<div class='success-msg'>Successfully Saved</div>");
 			
 		} else if (_errors.size() > 0) {
 			
			out.println("<ul class='errors'>");

			for (String e : _errors) {
				
				out.println("  <li>" + e + "</li>");
			}

			out.println("</ul>");
		}
 	}
	
	/**
	 * Calculates office hours.
	 * @param num
	 * @param req
	 * @return
	 */
	public static String calcOfficeHours(int num, HttpServletRequest req) {
		
		return req.getParameter("office-day-"+num) + ";" +req.getParameter("office-hours-"+num+"-start-1") + ";" + req.getParameter("office-hours-"+num+"-end-1") + ";" + req.getParameter("office-hours-"+num+"-start-2") + ";" + req.getParameter("office-hours-"+num+"-end-2");
	}

	/**
	 * Retrieves the user from a saved cookie.
	 * @param req
	 * @return
	 */
	public static String getUserFromCookie(HttpServletRequest req) {
 		
 		Cookie cookies[] = req.getCookies();  

 		String user = null;
 		
 		if(cookies != null){  
 			
 			for (Cookie c : cookies) {
 				
				if (c.getName().equals("user")) {
					
					user = c.getValue();
					
					break;
				}
			} 
 		}
 		
 		return user;
 	}

	/**
	 * Deletes saved cookie.
	 * @param resp
	 * @throws IOException
	 */
	public static void deleteCookie(HttpServletResponse resp) throws IOException {
		
		Cookie c = new Cookie("user", null);
		
		c.setMaxAge(0);

		resp.addCookie(c);
	}
	
	/**
	 * Checks access level of user retrieved from cookie.
	 * @param accessLevel
	 * @throws IOException
	 */
	private boolean checkAccess(int accessLevel) throws IOException {
		
		boolean success = true;
		
		if(Form.getUserFromCookie(_req) == null) {
			
			_resp.sendRedirect("401.html");
			
			success = false;
			
		} else {
		
			Datastore ds = new Datastore(_req, _resp, _errors);
			
			if(accessLevel > Integer.parseInt(ds.getUser().getAccess())) {
				
				_resp.sendRedirect("401.html");
				
				success = false;
			}
		}
		
		return success;
	}

	private String formInput(String label, String cssClass, String html) {
		
		return "<label class='"+cssClass+"'>" +
					"<span>"+label+"</span>" +
					html +
				"</label>";
	}
	/**
	 * 
	 * @param label
	 * @param text
	 * @param action
	 * @return start of an HTML form as a string
	 */
	public String Start(String label, String text, String action) {
		
		return "<form action='"+action+"' method='post' class='standard-form'>" +
				"<h1>"+label+"<span>"+text+"</span></h1>";
	}
	
	/**
	 * 
	 * @return end of form with a save button at bottom
	 */
	public String End() {
		
		return "<div class='submit'><input type='submit' name='submit' class='button' value='Save' /></div></form>";
	}
	
	/**
	 * 
	 * @param label
	 * @param name
	 * @param val
	 * @param ph
	 * @param cssClass
	 * @return text field HTML for a form
	 */
	public String TextField(String label, String name, String val, String ph, String cssClass) {
		
		return formInput(label, cssClass, "<input type='text' id='"+name+"' name='"+name+"' value='"+val+"'placeholder='"+ph+"' />");
	}
	
	/**
	 * 
	 * @return last HTML line of user search form
	 */
	public String EndSearchUser() {
		
		return "<div class='search submit'><input type='submit' name='submit' class='button' value='Search' /></div></form>";
	}
	
	/**
	 * 
	 * @param label
	 * @param cssClass
	 * @param name
	 * @param checked
	 * @return an html checkbox string
	 */
	public String CheckBox(String label, String cssClass, String name, Boolean checked) {

		return formInput(label, cssClass,
				"<div class='checkbox'>" +
					"<input id='"+name+"' type='checkbox' name='"+name+"' value='"+name+"' "+(checked ? "checked" : "")+"></input>" +
					"<label for='"+name+"'></label>" +
				"</div>");
	}
	
	/**
	 * 
	 * @param label
	 * @param cssClass
	 * @param firstSelect
	 * @param secondSelect
	 * @return html string of date time label
	 */
	public String DateTime(String label, String cssClass, String firstSelect, String secondSelect){
		
		return formInput(label, cssClass, 
				  "<span class='startend'>Start:</span>" + firstSelect +
					"<span class='startend'>End:</span>" + secondSelect 
		);
	}
	
	public String WeekCheckBoxes() {
		
		//<TODO> CHANGE INTO 7 CHECKBOXES ON SCREEN </TODO>
		return "<label>" +
				  "<span>Days: </br>(ex: MWF) </span>" +
				  "<input id='' type='text' name='' placeholder='' />" +
				"</label>";
	}
	
	/**
	 * 
	 * @param startName
	 * @param endName
	 * @param selected1
	 * @param selected2
	 * @param cssClass
	 * @return creates a select date dropdown
	 */
	public String selectDate(String startName, String endName, String selected1, String selected2, String cssClass) {
		
		return getSelectField(startName, (selected1 != null ? selected1 : ""), cssClass, 1, 12) +
				"<div class='separator'>&nbsp; / &nbsp;</div>" +
				getSelectField(endName, (selected2 != null ? selected2 : ""), cssClass, 1, 31);
	}
	
	/**
	 * 
	 * @param startName
	 * @param endName
	 * @param selected1
	 * @param selected2
	 * @param cssClass
	 * @return creates a select date dropdown
	 */
	public String selectTime(String startName, String endName, String selected1, String selected2, String cssClass) {
		
		List<String> endTime = Arrays.asList("00","10","15","30","40","45","50");
		
		return getSelectField(startName, (selected1 != null ? selected1 : ""), cssClass, 0, 23) +
				"<div class='separator'>&nbsp; : &nbsp;</div>" +
				getSelectField(endName, (selected2 != null ? selected2 : ""), cssClass, endTime);
	}
	
	/**
	 * 
	 * @param name
	 * @param selected
	 * @param cssClass
	 * @param options
	 * @return return select list html with options param appended
	 */
	public String getSelectField(String name, String selected, String cssClass, List<String> options) {
		
		String select = "<select class='"+cssClass+"' name='"+name+"'>";
		
		for(String option : options) {
			
			select += "<option value='"+option+"' "+ (selected.equalsIgnoreCase(option) ? "selected" : "") +">"+option+"</option>";
		}
		
		select += "</select>";
		
		return select;
	}
    
	/**
	 * 
	 * @param name
	 * @param selected
	 * @param cssClass
	 * @param start
	 * @param end
	 * @return value of select in form
	 */
	public String getSelectField(String name, String selected, String cssClass, int start, int end) {
		
		String select = "<select class='"+cssClass+"' name='"+name+"'>";
		
		for(int index = start; index <= end; index++) {
			select += "<option value='"+index+"' "+ (selected.equalsIgnoreCase( Integer.toString(index)) ? "selected" : "")+">"+index+"</option>";
		}
		
		select += "</select>";
		
		return select;
	}

	/**
	 * @return True if the form has been submitted.
	 */
	public boolean isSubmit() {

		return (_req.getParameter("submit") != null);
	}
	
	/**
	 * @param param
	 * @return param parameter if not null and if the _errors list is empty. Otherwise returns an empty string.
	 */
	public String getParam(String param) {
		
		return (_req.getParameter(param) != null && _errors.size() != 0 ? 
				_req.getParameter(param) : "");
	}

	/**
	 * Shows Courses in the dropdown menu.
	 * @param _courseID 
	 * @param courses 
	 * @throws IOException
	 * @return dropdown list
	 */
	public String courseDropdown(List<Course> courses, String courseID) throws IOException {

		String html = "<select name='course'>";

		for(Course  course : courses) {


			if(course.getID().equals(courseID))
				html += "<option selected='selected' value='"+course.getID()+"'>"+course.getName()+"</option>";

			else
				html += "<option value='"+course.getID()+"'>"+course.getName()+"</option>";

		}

		html += "</select>";

		return html;
	}
}

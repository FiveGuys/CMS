package edu.uwm.cs361;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Form
{
	private final static String[] STYLE = {"", "course.css", "views.css", "edit-info.css", "user.css"};
	private HttpServletRequest _req;
	private HttpServletResponse _resp;
	private List<String> _errors;
	
	public Form(HttpServletRequest req, HttpServletResponse resp, List<String> errors) {
		
		_req = req;
		_resp = resp;
		_errors = errors;
	}

	private String formInput(String label, String cssClass, String html) {
		
		return "<label class='"+cssClass+"'>" +
					"<span>"+label+"</span>" +
					html +
				"</label>";
	}
	
	public String Start(String label, String text, String action) {
		
		return "<form action='"+action+"' method='post' class='standard-form'>" +
				"<h1>"+label+"<span>"+text+"</span></h1>";
	}
	
	public String End() {
		
		return "<div class='submit'><input type='submit' name='submit' class='button' value='Save' /></div></form>";
		
	}
	public String TextField(String label, String name, String val, String ph, String cssClass) {
		
		return formInput(label, cssClass, "<input type='text' id='"+name+"' name='"+name+"' value='"+val+"'placeholder='"+ph+"' />");
		
	}
	
	public String DropDown(String label,  String name, String selected, List<String> list, String cssClass) {
		
		return formInput(label, cssClass, getSelectField(name, selected, "", list));
	}
	
	public String CheckBox(String label, String cssClass, String name, Boolean checked) {

		return formInput(label, cssClass,
				"<div class='checkbox'>" +
					"<input id='"+name+"' type='checkbox' name='"+name+"' value='"+name+"' "+(checked ? "checked" : "")+"></input>" +
					"<label for='"+name+"'></label>" +
				"</div>");
	}
	
	public String DateTime(String label, String cssClass, String firstSelect, String secondSelect){
		
		return formInput(label, cssClass, 
				  "<span class='startend'>Start:</span>" +
						  firstSelect +
					"<span class='startend'>End:</span>" +
					secondSelect );
	}
	
	public String WeekCheckBoxes() {
		
		//<TODO> CHANGE INTO 7 CHECKBOXES ON SCREEN </TODO>
		return "<label>" +
				  "<span>Days: </br>(ex: MWF) </span>" +
				  "<input id='' type='text' name='' placeholder='' />" +
				"</label>";
	}
	
	public String selectDate(String startName, String endName, String cssClass) {
		
		return getSelectField(startName, testParam(startName), cssClass, 1, 12) +
				"<div class='separator'>&nbsp; / &nbsp;</div>" +
				getSelectField(endName, testParam(endName + "-2"), cssClass, 1, 31);
	}
	
	public String selectTime(String startName, String endName, String cssClass) {
		
		List<String> endTime = Arrays.asList("00","10","15","30","40","45","50");
		
		return getSelectField(startName, testParam(startName), cssClass, 0, 23) +
				"<div class='separator'>&nbsp; : &nbsp;</div>" +
				getSelectField(endName, testParam(endName), cssClass, endTime);
	}
	
	public String getSelectField(String name, String selected, String cssClass, List<String> options) {
		
		String select = "<select class='"+cssClass+"' name='"+name+"'>";
		
		for(String option : options) {
			
			select += "<option value='"+option+"' "+ (selected.equalsIgnoreCase(option) ? "selected" : "") +">"+option+"</option>";
		}
		
		select += "</select>";
		
		return select;
		
	}
	
	public String getSelectField(String name, String selected, String cssClass, int start, int end) {
		
		String select = "<select class='"+cssClass+"' name='"+name+"'>";
		
		for(int index = start; index <= end; index++) {
			select += "<option value='"+index+"' "+ (selected.equalsIgnoreCase( Integer.toString(index)) ? "selected" : "")+">"+index+"</option>";
		}
		
		select += "</select>";
		
		return select;
		
	}
	
	public String printOfficeHours(int index) {

		List<String> officeDay = Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri");
		
		return "Office Hour "+index+"<br/>" +
				
			getSelectField("office-day-"+index, "office-day-", "", officeDay) +
			
			selectTime("office-hours-"+index+"-start-1", "office-hours-"+index+"-end-1", "") +
					"&nbsp; - &nbsp;" + 
			selectTime("office-hours-"+index+"-start-2", "office-hours-"+index+"-end-2", "") +
			
			"</br></br>";	
	}
	
	public String testParam(String param) {
		
		return (_req.getParameter(param) != null ? _req.getParameter(param) : "");
	}
	
	private void displayForm(String header, int page, CallBack servlet) throws IOException {
		
		printHeader(header, page);
		
		printList(_errors, "errors");
		
		servlet.printContent();
		
		printFooter();
	}
	
	public void handleGet(String header, int page, CallBack servlet, String method, int accessLevel) throws IOException {
		
		Datastore ds = new Datastore(_req, _resp, _errors);
		
		ds.checkAccess(accessLevel);
		
		servlet.validate();
		
		ds.callMethod(method);
		
		displayForm(header, page, servlet);
		
	}
 	public void printHeader(String title, int index) throws IOException {
		
 		_resp.setContentType("text/html");
		
 		_resp.getWriter().println( 
 		"<!DOCTYPE html>" 
		+"<html>" 
		+"<head>" 
			+"<meta charset='utf-8'>" 
			+"<link rel='shortcut icon' href='images/favicon.ico'>" 
		    +"<link rel='stylesheet' type='text/css' href='css/main.css'>" 
		    +"<link rel='stylesheet' type='text/css' href='css/navbar.css'>" 
			+"<link rel='stylesheet' type='text/css' href='css/form.css'>" 
			+"<link rel='stylesheet' type='text/css' href='css/"+STYLE[index]+"'>" 
		    +"<meta name='viewport' content='width=device-width'>" 
		    +"<title>Course Management Site</title>" 
		+"</head>" 
		+"<body>" 
 		+"<div id='main-container'>" 
		+"<div id='header'>  " 
		    +"<div class='settings'><a href='add-user'>Add New User</a><a href='index.html'>Logout</a></div>" 
		      +"<div class='uwmlogo'>" 
		        +"<a href='https://www4.uwm.edu/' target='_new'>" 
		            +"<img src='images/logo_uwm.png'><img>" 
		                +"</a>" 
		            +"</div>" 
		        +"<div class='header-title'>"+title+"</div>" 
		        +"<div id='navbar'>" 
		        +"<ul>" 
		        +"<li class='active has-sub'><a "+( index == 1 ? "class='selected'" : "")+" href='#'>Courses</a>" 
		           +"<ul>" 
		            +"<li><a href='assign-prof'>Assign Professor</a></li>" 
		              +"<li><a href='assign-ta'>Assign TA</a></li>" 
		              +"<li><a href='create-course'>Create a Course</a></li>" 
		              +"<li><a href='create-lab-dis'>Create Lab/Discussion</a></li>	" 	
		              +"<li><a href='courses'>View All Courses</a></li>" 
		              +"</ul>" 
		            +"</li>" 
		           +"<li class='active has-sub'><a "+( index == 2 ? "class='selected'" : "")+" href='scheduleviews'>Schedule Views</a>" 
		           +"<ul style='display: none'>" 
		              +"<li class='has-sub'><a href='#'>View 1</a>" 
		                 +"<ul>" 
		                    +"<li><a href='#'>Sub View</a></li>" 
		                       +"<li class='last'><a href='#'>Sub View</a></li>" 
		                       +"</ul>" 
		                    +"</li>" 
		                 +"<li class='has-sub'><a href='#'>View 2</a>" 
		                 +"<ul>" 
		                    +"<li><a href='#'>Sub View</a></li>" 
		                       +"<li class='last'><a href='#'>Sub View</a></li>" 
		                       +"</ul>" 
		                    +"</li>" 
		                 +"</ul>" 
		              +"</li>" 
		           +"<li><a "+( index == 3 ? "class='selected'" : "")+" href='edit-info'>Edit Info</a></li>" 
		           +"<li><a "+( index == 4 ? "class='selected'" : "")+" href='user-search'>Search User</a></li>" 
		           +"</ul>" 
		        +"</div>" 
		      +"</div>"
		      + "<div id='content'>");
 	}
 	
 	public void printFooter() throws IOException {
 		
 		_resp.getWriter().println("</div></div></body></html>");
 	}
 	
 	public void printList(List<String> list, String cssClass) throws IOException{
 		
 		if (list.size() > 0) {
 			
 			PrintWriter out = _resp.getWriter();
 			
			out.println("<ul class='"+cssClass+"'>");

			for (String e : list) {
				out.println("  <li>" + e + "</li>");
			}

			out.println("</ul>");
		}
 	}
 	
 	public static String getUserFromCookie(HttpServletRequest req) {
 		
 		Cookie cookies[] = req.getCookies();  

 		String user = null;
 		
 		if(cookies != null){  
 			
 			for (Cookie c : cookies) {
 				
				if (c.getName().equals("user")) {
					
					user = c.getValue();
				}
			} 
 		}
 		
 		return user;
 	}

	public static void deleteCookie(HttpServletResponse resp) throws IOException {
		
		Cookie c = new Cookie("user", null);
		
		c.setMaxAge(0);

		resp.addCookie(c);
	}
	
	public Map<String, String> getParameters() {

		Map<String, String> map = new HashMap<String, String>();

		if(_req.getParameter("submit") != null) {

			Enumeration<?> params = _req.getParameterNames();

			while(params.hasMoreElements()) {

				String key = (String) params.nextElement(); 
				String value = _req.getParameter(key);

				map.put(key, value);
			}

		} else {

			Datastore ds = new Datastore(_req, _resp, _errors);
			
			map = ds.getUser();
		}
		
		return map;
	}
}

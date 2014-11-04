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
	private HttpServletRequest _req;
	
	private HttpServletResponse _resp;
	
	private List<String> _errors;
	
	public Form(HttpServletRequest req, HttpServletResponse resp, List<String> errors) {
		
		_req = req;
		
		_resp = resp;
		
		_errors = errors;
	}

	private void displayForm(String header, int page, CallBack servlet) throws IOException {
		
		printHeader(header, page);
		
		printMessage();
		
		servlet.printContent();
		
		printFooter();
	}
	
	public void handleGet(String header, int page, CallBack servlet, String method, int accessLevel) throws IOException {
		
		checkAccess(accessLevel);
		
		if(_req.getParameter("submit") != null) {
			
			Datastore ds = new Datastore(_req, _resp, _errors);
			
			servlet.validate();
			
			ds.callMethod(method);
		}
		
		displayForm(header, page, servlet);
	}
	
 	public void printHeader(String title, int page) throws IOException {
		
 		HtmlOutputHelper.printHeader(_resp, title, page);
 	}
 	
 	public void printFooter() throws IOException {
 		
 		HtmlOutputHelper.printFooter(_resp);
 	}
 	
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
 	
	public Map<String, String> getParameters() {

		Map<String, String> map = new HashMap<String, String>();

		if(_req.getParameter("submit") != null) {

			Enumeration<?> params = _req.getParameterNames();

			while(params.hasMoreElements()) {

				String key = (String) params.nextElement(); 
				String value = _req.getParameter(key);

				map.put(key, value);
			}
			
			// Must combine office hours into database
			if(_req.getParameter("office-day-1") != null){
				
				map.put("OfficeHour1", Form.calcOfficeHours(1, _req));
				map.put("OfficeHour2", Form.calcOfficeHours(2, _req));
				map.put("OfficeHour3", Form.calcOfficeHours(3, _req));
			}
			
		} else {

			Datastore ds = new Datastore(_req, _resp, _errors);
			
			map = ds.getUser();
		}
		
		return map;
	}
	
	public static String calcOfficeHours(int num, HttpServletRequest req) {
		
		return req.getParameter("office-day-"+num) + ";" +req.getParameter("office-hours-"+num+"-start-1") + ";" + req.getParameter("office-hours-"+num+"-end-1") + ";" + req.getParameter("office-hours-"+num+"-start-2") + ";" + req.getParameter("office-hours-"+num+"-end-2");
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
	
	private void checkAccess(int accessLevel) throws IOException {
		
		Datastore ds = new Datastore(_req, _resp, _errors);
		
		if(Form.getUserFromCookie(_req) == null ||
			accessLevel > Integer.parseInt(ds.getAttrFromUser("Access"))) {
			
			_resp.sendRedirect("401.html");
		}
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
	
	public String selectDate(String startName, String endName, String selected1, String selected2, String cssClass) {
		
		return getSelectField(startName, (selected1 != null ? selected1 : ""), cssClass, 1, 12) +
				"<div class='separator'>&nbsp; / &nbsp;</div>" +
				getSelectField(endName, (selected2 != null ? selected2 : ""), cssClass, 1, 31);
	}
	
	public String selectTime(String startName, String endName, String selected1, String selected2, String cssClass) {
		
		List<String> endTime = Arrays.asList("00","10","15","30","40","45","50");
		
		return getSelectField(startName, (selected1 != null ? selected1 : ""), cssClass, 0, 23) +
				"<div class='separator'>&nbsp; : &nbsp;</div>" +
				getSelectField(endName, (selected2 != null ? selected2 : ""), cssClass, endTime);
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
}

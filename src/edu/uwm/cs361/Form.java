package edu.uwm.cs361;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class Form
{
	private static String formInput(String label, String cssClass, String html) {
		
		return "<label class='"+cssClass+"'>" +
					"<span>"+label+"</span>" +
					html +
				"</label>";
	}
	
	public static String Start(String label, String text, String action) {
		
		return "<form action='"+action+"' method='post' class='standard-form'>" +
				"<h1>"+label+"<span>"+text+"</span></h1>";
	}
	
	public static String End() {
		
		return "<div class='submit'><input type='button' class='button' value='Save' /></div></form>";
		
	}
	public static String TextField(String label, String name, String val, String ph, String cssClass) {
		
		return formInput(label, cssClass, "<input type='text' id='"+name+"' name='"+name+"' value='"+val+"'placeholder='"+ph+"' />");
		
	}
	
	public static String DropDown(String label,  String name, String selected, List<String> list, String cssClass) {
		
		return formInput(label, cssClass, getSelectField(name, selected, "", list));
	}
	
	public static String CheckBox(String label, String cssClass, String name, Boolean checked) {

		return formInput(label, cssClass,
				"<div class='checkbox'>" +
					"<input id='"+name+"' type='checkbox' name='"+name+"' value='"+name+"' "+(checked ? "checked" : "")+"></input>" +
					"<label for='"+name+"'></label>" +
				"</div>");
	}
	
	public static String DateTime(String label, String cssClass, String html){
		
		return formInput(label, cssClass, 
				  "<span class='startend'>Start:</span>" +
					html +
					"<span class='startend'>End:</span>" +
					html );
	}
	
	public static String WeekCheckBoxes() {
		
		//<TODO> CHANGE INTO 7 CHECKBOXES ON SCREEN </TODO>
		return "<label>" +
				  "<span>Days: </br>(ex: MWF) </span>" +
				  "<input id='' type='text' name='' placeholder='' />" +
				"</label>";
	}
	
	public static String selectDate(String startName, String endName, HttpServletRequest req) {
		
		return getSelectField(startName + "-1", testParam(req, startName + "-1"), "datetime", 1, 12) +
				"<div class='separator'>&nbsp; / &nbsp;</div>" +
				getSelectField(endName + "-2", testParam(req, endName + "-2"), "datetime", 1, 31);
	}
	
	public static String selectTime(String startName, String endName, HttpServletRequest req) {
		
		List<String> endTime = Arrays.asList("00","10","15","30","40","45","50");
		
		return getSelectField(startName + "_1", testParam(req, startName + "-1"), "datetime", 0, 23) +
				"<div class='separator'>&nbsp; : &nbsp;</div>" +
				getSelectField(endName + "_2", testParam(req, endName + "-2"), "datetime", endTime);
	}
	
	private static String getSelectField(String name, String selected, String cssClass, List<String> options) {
		
		String select = "<select class='"+cssClass+"' name='"+name+"'>";
		
		for(String option : options) {
			
			select += "<option value='"+option+"' "+ (selected.equalsIgnoreCase(option) ? "selected" : "") +">"+option+"</option>";
		}
		
		select += "</select>";
		
		return select;
		
	}
	
	private static String getSelectField(String name, String selected, String cssClass, int start, int end) {
		
		String select = "<select class='"+cssClass+"' name='"+name+"'>";
		
		for(int index = start; index <= end; index++) {
			select += "<option value='"+index+"' "+ (selected.equalsIgnoreCase( Integer.toString(index)) ? "selected" : "")+">"+index+"</option>";
		}
		
		select += "</select>";
		
		return select;
		
	}
	
	private static String testParam(HttpServletRequest req, String name) {
		
		return (req.getParameter(name) != null ? req.getParameter(name) : "");
	}
	
}

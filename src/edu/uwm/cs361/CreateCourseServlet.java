/*package edu.uwm.cs361;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("serial")
public class CreateCourseServlet extends HttpServlet implements CallBack
{
	private final int ACCESS_LEVEL = 1;
	
	private HttpServletRequest _req;
	
	private HttpServletResponse _resp;
	
	private List<String> _errors;
	
	private Form form;
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
		_req = req;
		
		_resp = resp;
		
		_errors = new ArrayList<String>();
		
		form = new Form(_req, _resp, _errors);
		
		form.handleGet("Create Course", 1, this, "addCourse", ACCESS_LEVEL);
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		doGet(req, resp);
	}
	
	@Override
	public void printContent()  throws IOException {
		
		_resp.getWriter().println( 
				
				form.Start("Course Form", "Please fill all the texts in the fields.", "#") +
				
				form.TextField("Title: ", "course-name", testParam("course-name"), "Course Name", "") +
				
				form.TextField("Course #: ", "course-number", testParam("course-num"), "XXX", "") +
				
				form.TextField("Section: ", "section-number", testParam("section-num"), "XXX-LAB/LEC/DIS", "") +
				
				form.TextField("Class #: ", "class-number", testParam("class-num"), "XXXXX", "") +
				
				form.TextField("Credits: ", "credits", testParam("credits"), "Units", "") +
				
				form.DropDown("Instructor: ", "instructor", testParam("instructor"), Datastore.getAllInstructors(), "") +
				
				form.TextField("Room #: ", "room-number", testParam("room-number"), "EMS", "") +
				
				//form.DateTime("Hours: ", "", form.selectTime("time-start", "time-end", req)) +
				
				//form.DateTime("Dates: ", "", form.selectDate("date-start", "date-end", req)) +
				
				form.WeekCheckBoxes() +
				
				form.CheckBox("Has Lab: ", "", "has-lab", (testParam("has-lab") != "")) +
				
				form.CheckBox("Has Dis: ", "last", "has-dis", (testParam("has-dis") != "")) +
				
				form.End()
		);
	}
	
	@Override
	public void validate() {
		
	}
	
	private String testParam(String name) {
		
		return (_req.getParameter(name) != null ? _req.getParameter(name) : "");
	}
}
*/
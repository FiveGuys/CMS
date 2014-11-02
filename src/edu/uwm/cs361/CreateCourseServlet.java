package edu.uwm.cs361;

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
				
				Form.Start("Course Form", "Please fill all the texts in the fields.", "#") +
				
				Form.TextField("Title: ", "course-name", form.testParam("course-name"), "Course Name", "") +
				
				Form.TextField("Course #: ", "course-number", form.testParam("course-num"), "XXX", "") +
				
				Form.TextField("Section: ", "section-number", form.testParam("section-num"), "XXX-LAB/LEC/DIS", "") +
				
				Form.TextField("Class #: ", "class-number", form.testParam("class-num"), "XXXXX", "") +
				
				Form.TextField("Credits: ", "credits", form.testParam("credits"), "Units", "") +
				
				Form.DropDown("Instructor: ", "instructor", form.testParam("instructor"), Datastore.getAllInstructors(), "") +
				
				Form.TextField("Room #: ", "room-number", form.testParam("room-number"), "EMS", "") +
				
				//Form.DateTime("Hours: ", "", Form.selectTime("time-start", "time-end", req)) +
				
				//Form.DateTime("Dates: ", "", Form.selectDate("date-start", "date-end", req)) +
				
				Form.WeekCheckBoxes() +
				
				Form.CheckBox("Has Lab: ", "", "has-lab", (form.testParam("has-lab") != "")) +
				
				Form.CheckBox("Has Dis: ", "last", "has-dis", (form.testParam("has-dis") != "")) +
				
				Form.End()
		);
	}
	
	@Override
	public void validate() {
		
	}
}

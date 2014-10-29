package edu.uwm.cs361;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.*;

@SuppressWarnings("serial")
public class CreateCourseServlet extends HttpServlet
{
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
		resp.getWriter().println( testParam(req, "course") );
		
		HtmlOutputHelper.checkAccess(resp);
		
		List<String> errors = new ArrayList<String>();
		
		checkErrors(req, errors);
		
		Boolean success = Datastore.saveCourse(req, errors);
		
		displayForm(req, resp, errors, success);
		
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		doGet(req, resp);
	}
	
	private void displayForm(HttpServletRequest req, HttpServletResponse resp, List<String> errors, Boolean result) throws IOException {
		
		HtmlOutputHelper.printHeader(resp, "Create Course", 1);
		
		HtmlOutputHelper.printErrors(resp, errors);
		
		printContent(req, resp);
		
		HtmlOutputHelper.printFooter(resp);
	}
	
	private void printContent(HttpServletRequest req, HttpServletResponse resp)  throws IOException {
		
		resp.getWriter().println( 
				
				Form.Start("Course Form", "Please fill all the texts in the fields.", "#") +
				
				Form.TextField("Title: ", "course-name", testParam(req, "course-name"), "Course Name", "") +
				
				Form.TextField("Course #: ", "course-number", testParam(req, "course-num"), "XXX", "") +
				
				Form.TextField("Section: ", "section-number", testParam(req, "section-num"), "XXX-LAB/LEC/DIS", "") +
				
				Form.TextField("Class #: ", "class-number", testParam(req, "class-num"), "XXXXX", "") +
				
				Form.TextField("Credits: ", "credits", testParam(req, "credits"), "Units", "") +
				
				Form.DropDown("Instructor: ", "instructor", testParam(req, "instructor"), Datastore.getAllInstructors(), "") +
				
				Form.TextField("Room #: ", "room-number", testParam(req, "room-number"), "EMS", "") +
				
				Form.DateTime("Hours: ", "", Form.selectTime("time-start", "time-end", req)) +
				
				Form.DateTime("Dates: ", "", Form.selectDate("date-start", "date-end", req)) +
				
				Form.WeekCheckBoxes() +
				
				Form.CheckBox("Has Lab: ", "", "has-lab", (testParam(req, "has-lab") != "")) +
				
				Form.CheckBox("Has Dis: ", "last", "has-dis", (testParam(req, "has-dis") != "")) +
				
				Form.End()
		);
	}
	
	private List<String> checkErrors(HttpServletRequest req, List<String> errors) {
		return errors;
	}
	
	private String testParam(HttpServletRequest req, String name) {
		
		return (req.getParameter(name) != null ? req.getParameter(name) : "");
	}
}

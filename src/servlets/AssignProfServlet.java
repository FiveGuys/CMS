package servlets;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdo.Course;
import jdo.Section;
import jdo.User;
import edu.uwm.cs361.CallBack;
import edu.uwm.cs361.Datastore;
import edu.uwm.cs361.Form;

/**
 * This Servlet class assigns a Professor to the CMS system. 
 * @author 5guys
 */
@SuppressWarnings("serial")
public class AssignProfServlet extends HttpServlet implements CallBack
{
	private int ACCESS_LEVEL = ACCESS_ADMIN;
	
	private HttpServletRequest _req;
	
	private HttpServletResponse _resp;
	
	private Form _form;
	
	private List<Course> _courses;
	
	private List<Section> _sections;
	
	private String _courseID;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
		_req = req;
		
		_resp = resp;
		
		_courses = Datastore.getCourses(null);
		
		_courseID = getCourseID();
		
		_sections = Datastore.getSections("CourseID=='"+_courseID+"' && ClassType=='LEC'");
		
		_form = new Form(_req, _resp, new ArrayList<String>());
		
		_form.handleGet("Assign Professor", 1, this, "updateProf", ACCESS_LEVEL);
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		doGet(req, resp);
	}
	
	/**
	 * Prints html content.
	 */	
	@Override
	public void printContent()  throws IOException {
		
		printCourse();
		
		if(_sections.size() > 0) {

			printSection();
		
		} else {
			
			printNoSectionFound();
		}
	}
	
	private void printNoSectionFound() throws IOException {
		
		String html = "<form action='assign-prof' method='post' class='standard-form no-border'>" +
					"<div class='not-found'>No Lectures Found</div> </form>";
		
		_resp.getWriter().println(html);
	}

	/**
	 * Validates page, has to be here because 
	 * of CallBack interface
	 */
	@Override
	public void validate(){	}
	
	/**
	 * Prints course table
	 * @throws IOException
	 */
	private void printCourse() throws IOException{
		
		String html = "<form action='assign-prof' method='post' class='standard-form'>"+
						
						_form.courseDropdown(_courses, _courseID) +
						"<div class='submit inline'>"
							+ "<input type='submit' name='save' class='button' value='Submit' />"
						+ "</div>"
						+ "</form>";
		
		_resp.getWriter().println(html);
	}
	
	/**
	 * Prints section
	 * @throws IOException
	 */
	private void printSection() throws IOException{
		
		String html =
		"<form action='assign-prof' method='post' class='standard-form no-border'>"+
				"<table class='assign-table'>"+
				"<tr>"+
					"<td><b>Class Type<b/></td>"+
					"<td><b>Section #</b></td>"+
					"<td><b>Professor</b></td>"+
				"</tr>";
		
		int i = 0;
		
		 for(Section section : _sections){
				 
				 html += "<tr>"+
							"<td>"+section.getClassType() +"</td>"+
							"<td>"+section.getSection()+"</td>"+
							"<td>"+profDropdown(section.getInstructorID(), "prof"+i)+"</td>"+
						"</tr>";
			i++;
		 }
		 
		 html += "</table>" +
		 		 "<input type='hidden' name='courseID' value='"+_courseID+"' />"+
				 _form.End();
		 
		 _resp.getWriter().println(html);
	}
	
	private String profDropdown(String instructorID, String name) {
		
		List<User> users = Datastore.getUsers("Access=='2'");
		
		String html = "<select class='prof' name='"+name+"'><option value=''></option>";

		for(User user : users) {

				html += "<option "+ (user.getID().equals(instructorID) ? "selected='selected'" : "") +
						" value='"+user.getID()+"'>"+ user.getFirstName() + " " + user.getLastName()+
						"</option>";
		}

		html += "</select>";
		
		return html;
	}
	
	/**
	 * Gets the course ID
	 * @return
	 */
	public String getCourseID() {

		return (_req.getParameter("course") != null ? 
				_req.getParameter("course") : _courses.get(0).getID());
	}
	
}
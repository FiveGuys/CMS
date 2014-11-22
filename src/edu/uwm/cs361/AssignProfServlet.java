package edu.uwm.cs361;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.*;

@SuppressWarnings("serial")
public class AssignProfServlet extends HttpServlet implements CallBack
{
	private int ACCESS_LEVEL=ACCESS_LIMITED;
	
	private HttpServletRequest _req;
	
	private String _courseID;
	
	private HttpServletResponse _resp;
	
	private List<String> _errors;
	
	private Form _form;
	
	private List<Course> _courses;
	
	private List<Section> _sections;
	
	private List<User> _users;
	
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
		_req = req;
		
		_resp = resp;
		
		_courses = Datastore.getCourses(null);
		
		_courseID = getCourseID();
		
		_sections = Datastore.getSections(null);
		
		_users = Datastore.getUsers(null);
		
		_form = new Form(_req, _resp, new ArrayList<String>());
		
		_form.handleGet("Assign Proffesor", 3, this, "", ACCESS_LEVEL);
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		doGet(req, resp);
	}
	
	@Override
	public void printContent()  throws IOException {
		_resp.getWriter().println(
		"<form action='assign-prof' method='post' class='standard-form'>"+
		"<label>"+
			"<span class='assign'>Course: </span>");
			printCourse();
			_resp.getWriter().println(
		"</label>");
			printSection();
			_resp.getWriter().println(
		"</table>"+
		"<div class='submit'>"+ 
			"<input type='button' class='button' value='Save' />"+
		"</div>"+
	"</form>"+
	"</div>");
	}
	
	@Override
	public void validate(){	
	}
	
	private void printCourse() throws IOException{
		String html = "<form action='assign-prof' method='post' class='standard-form'>"
				+"<select name='course'>";
		
		Datastore ds = new Datastore(_req, _resp, _errors);
		if (Integer.parseInt(ds.getUser().getAccess())==ACCESS_ADMIN)
		{
			
		for(Course  course : _courses) {
			

			if(course.getID().equals(_courseID))
				html += "<option selected='selected' value='"+course.getID()+"'>"+course.getName()+"</option>";
			
			else
				html += "<option value='"+course.getID()+"'>"+course.getName()+"</option>";

		}
			
		
		}
		else
		{
			boolean containsProf = false;
			for(Course  course : _courses) {
				for(Section section: _sections){
					
					if(section.getClassType().equals("LEC")&& section.getCourseID().equals(course.getID())&&  section.getInstructor().equals(ds.getUser().getLastName()+", "+ds.getUser().getFirstName())){
						containsProf = true;
					}		
				}
			if(course.getID().equals(_courseID)&& containsProf )
				html += "<option selected='selected' value='"+course.getID()+"'>"+course.getName()+"</option>";	
			
			else if(containsProf)
				html += "<option value='"+course.getID()+"'>"+course.getName()+"</option>";
			
			containsProf=false;
			}
		}
		html += "</select>"
				+"<div class='submit'><input type='submit' name='submit' class='button' value='Submit' /></div>"
			+"</form>";
		_resp.getWriter().println(html);
	}
	
	private void printSection() throws IOException{
		String html ="<table class='assign-table'>"+
			"<tr>"+
				"<td><strong> Class Type</strong></td>"+
				"<td><strong> Section #</strong></td>"+
				"<td><strong> Current Instructor</strong></td>"+         
			"</tr>";
		
		 for(Section section: _sections){
			 if(section.getCourseID().equals(_courseID)){
				 html += "<tr>"+
							"<td><span>"+section.getClassType() +"</span></td>"+
							"<td><span>"+section.getSection()+"</span></td>"+
							"<td><input value='"+section.getInstructor()+"' type='textbox'></td>"+         
						"</tr>";
			 }
		 }
		 
		 _resp.getWriter().println(html);
	}
	private String getCourseID() {

		return (_req.getParameter("course") != null ? 
				_req.getParameter("course") : _courses.get(0).getID());
	}
	
}
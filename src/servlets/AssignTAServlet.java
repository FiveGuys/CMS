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
 * This Servlet class assigns a Teaching Assistant to the CMS system. 
 * @author 5guys
 */
@SuppressWarnings("serial")
public class AssignTAServlet extends HttpServlet implements CallBack
{
	private int ACCESS_LEVEL = ACCESS_LIMITED;
	
	private HttpServletRequest _req;
	
	private HttpServletResponse _resp;
	
	private Form _form;
	
	private List<Course> _courses;
	
	private List<Section> _sections;
	
	private String _courseID;

	private boolean isAdmin;
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
		Datastore ds = new Datastore(req, resp, new ArrayList<String>());
		
		isAdmin = (Integer.parseInt(ds.getUser().getAccess()) >= ACCESS_ADMIN);
		
		String callback = isAdmin ? "adminAssignTA" : "profAssignTA";
		
		_req = req;
		
		_resp = resp;
		
		_courses = Datastore.getCourses(null);
		
		_courseID = getCourseID();
		
		_sections = Datastore.getSections("CourseID=='"+_courseID+"' && ClassType!='LEC'");
		
		System.out.println(_sections.size());
		
		_form = new Form(_req, _resp, new ArrayList<String>());
		
		_form.handleGet("Assign TA", 1, this, callback, ACCESS_LEVEL);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		doGet(req, resp);
	}
	
	/**
	 * Prints html content.
	 */	
	@Override
	public void printContent()  throws IOException {
		
		if(isAdmin) {
			
			printAdminTA();
			if(_req.getParameter("save")!=null){
				
				adminAssignTA();
			}
			
		} else {
			
			printCourse();
			
			if(_req.getParameter("save") != null) {
				
				profAssignTA();
			}
		}
	}
	
	/**
	 * @throws IOException
	 */
	private void profAssignTA() throws IOException {
		
		printSection();
	}

	/**
	 * @throws IOException
	 */
	private void adminAssignTA() throws IOException {
		
		printTA();
	}

	/**
	 * Prints TA
	 * @throws IOException
	 */
	private void printTA() throws IOException {
		// CTRL - Windows , Command on Mac
		String html = "<form action='assign-ta' method='post' class='standard-form'>"+
		
				TADropdown("1", "prof", "multiple") + 
				"<input type='hidden' name='courseID' value='"+_courseID+"' />"+
				_form.End();
		
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
	private void printAdminTA() throws IOException{
		
		String html = "<form action='assign-ta' method='post' class='standard-form'>"+
						"<label><span class='assign'>Course: </span>" +
							_form.courseDropdown(_courses, _courseID) +
						"</label>" +
						(isAdmin ? "<label><span class='assign'>Keyword: </span>"
									+ "<textarea class='keyword' name='keyword'></textarea>"
								+ "</label>" : "") +
						
						"<div class='submit'>"
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
		"<form action='assign-ta' method='post' class='standard-form'>"+
				"<table class='assign-table'>"+
				"<tr>"+
					"<td><b>Class Type<b/></td>"+
					"<td><b>Section #</b></td>"+
					"<td><b><div>TA Name &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div> </b></td>"+
				"</tr>";
		
		int i = 0;
		
		 for(Section section : _sections){
				 
				 html += "<tr>"+
							"<td>"+section.getClassType() +"</td>"+
							"<td>"+section.getSection()+"</td>"+
							"<td>"+getAssignedTa(section.getInstructorID(), "prof"+i)+"</td>"+
						"</tr>";
			i++;
		 }
		 
		 html += "</table>" +
		 		 "<input type='hidden' name='courseID' value='"+_courseID+"' />"+
				 _form.End();
		 
		 _resp.getWriter().println(html);
	}
	
	/**
	 * Displays dropdown list of TAs
	 * @param instructorID
	 * @param name
	 * @param multiple
	 * @return
	 */
	private String TADropdown(String instructorID, String name, String multiple) {
		
		List<User> users = Datastore.getUsers("Access=='1'");
		
		String html = "<select "+multiple+" class='prof' "+(multiple != null ? "size='"+users.size()+"'" : "") + " name='"+name+"'>";

		for(User user : users) {
			if(checkKeyWord(user.getKeyword())){
				html += "<option "+ (user.getID().equals(instructorID) ? "selected='selected'" : "") +
						" value='"+user.getID()+"'>"+ user.getFirstName() + " " + user.getLastName()+
						"</option>";
			}
		}

		html += "</select>";
		
		return html;
	}
	
	/**
	 * Checks keyword
	 * @param key
	 * @return
	 */
	private boolean checkKeyWord(String key){
		String input = _req.getParameter("keyword");
		String[] inputWord=input.split("[;]");
		String[] userWords=key.split("[;]");
		boolean check=false;
		if(input.replaceAll("\\s+","").equals(""))
			return true;
		for(String inputKey : inputWord)
		{
			for(String userKey : userWords){
				if(inputKey.replaceAll("\\s+","").equalsIgnoreCase(userKey.replaceAll("\\s+","")))
					check=true;
			}
			if(!check)
				return false;
			check=false;
		}
		return true;
	}
	
	
	/**
	 * Finds assigned TA
	 * @param instructorID
	 * @param name
	 * @return
	 */
	private String getAssignedTa(String instructorID, String name){
		String html="<select class='ta' name='"+name+"'>";
		html+="<option value=''> </option>";
		List<Course> _course = Datastore.getCourses("CourseID=='"+_courseID+"'");
		if(!_course.isEmpty()&&_course.get(0).getUserID()!=null)
		{
		String[] userIDs =_course.get(0).getUserID().split("[;]");
		
		// html = "<select class='ta' name='"+name+"'>";

		for(String userID : userIDs) {

				User user = Datastore.getUserFromID(userID);
				
				html += "<option "+ (user.getID().equals(instructorID) ? "selected='selected'" : "") +
						" value='"+user.getID()+"'>"+ user.getFirstName() + " " + user.getLastName()+
						"</option>";
		}
		}
		html += "</select>";
		
		return html;
	}
	
	/**
	 * Prints assigned courses
	 * @throws IOException
	 */
	private void printCourse() throws IOException{
		String html = "<form action='assign-ta' method='post' class='standard-form'>"
				+"<select name='course'>";

		List<Section> temp;
		
		boolean containsProf = false;
		
		Datastore ds = new Datastore(_req, _resp, new ArrayList<String>());

		for(Course  course : _courses) {
			
			temp  = Datastore.getSections("CourseID=='"+course.getID()+"' && ClassType=='LEC' && InstructorID=='"+ds.getUser().getID()+"'");

			if(temp.size() > 0 ){
				
				html += "<option "+(course.getID().equals(_courseID) ? "selected='selected" : "")+
						" value='"+course.getID()+"'>"+course.getName()+"</option>";	
				
				containsProf = true;
			}
		}
		
		html += "</select>"
				+"<div class='submit'><input type='submit' name='save' class='button' value='Submit' /></div>"
				+"</form>";
		
		if(containsProf) {
			
			_resp.getWriter().println(html);
			
		} else {
			
			_resp.getWriter().println("<div class='not-found'>No Lectures Found</div>");
		}
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
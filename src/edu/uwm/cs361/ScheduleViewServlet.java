package edu.uwm.cs361;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Arrays;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

@SuppressWarnings("serial")
public class ScheduleViewServlet extends HttpServlet{
	
	private String[][] table = new String[11][5];
	private boolean buildTable = false;
	private String courseID = "";
	//Yes, I wrote out this array because I am a scrub
	private String[] times = {"8:00 AM","9:00 AM","10:00 AM","11:00 AM","12:00 PM","1:00 PM","2:00 PM","3:00 PM","4:00 PM","5:00 PM","6:00 PM"};
	private char[] dates = {'M','T','W','R','F'};
	private String[] vals =new String[3];
	private List<Course> courses;
	private List<Section> sections;
	private HttpServletRequest _req;
	private HttpServletResponse _resp;
	//private final int ACCESS_LEVEL = ACCESS_ALL;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		_req = req;
		
		_resp = resp;
		
		courses = Datastore.getCourses();
		
		courseID = (req.getParameter("course") != null ? 
						req.getParameter("course") : courses.get(0).getID());
		
		sections = Datastore.getSections("CourseID=='"+courseID+"' && ClassType=='LEC'");
		
		for(int i = 0; i < table.length; ++i){
			for(int j = 0; j< table[i].length; ++j){
				table[i][j] = "<td></td>";
			}
		}
		
		//_form = new Form(_req, _resp, new ArrayList<String>());
		
		//_form.handleGet("Edit Your Information", 3, this, "updateUser", ACCESS_LEVEL);
		
		displayForm(courseID);
	}
	
	 //TODO doPost should take the class, instructor, or ta, from the select, and input elements from the datastore into the array table
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		doGet(req, resp);
	}
	
	public void displayForm(String course) throws IOException{
		
		_resp.setContentType("text/html");
		
		HtmlOutputHelper.printHeader(_resp, "Schedule Viewer", 2);
		
		createContent();
		
		HtmlOutputHelper.printFooter(_resp);
	}
	
	private void createContent() throws IOException{
		
		courseDropdown();
		
		createHTMLTable();
	}

	private void courseDropdown() throws IOException{
		
		String html = "<form action='schedule-view' method='post' class='standard-form'>"
				+"<select name='course'>";
		
		for(Course  course : courses) {
			
			html += "<option value='"+course.getID()+"'>"+course.getName()+"</option>";
		}
			
		html += "</select>"
					+"<div class='submit'><input type='submit' name='submit' class='button' value='Submit' /></div>"
				+"</form>";
		
		_resp.getWriter().println(html);
	}
	
	private void createHTMLTable() throws IOException{
		
		courseBuilder();
		
		String htmlTable = "<table id='schedule'>"
			+"<tr>"
				+"<th>Time</th>"
				+"<th>Monday</th>"
				+"<th>Tuesday</th>"
				+"<th>Wednesday</th>"
				+"<th>Thursday</th>"
				+"<th>Friday</th>"
			+"</tr>";
		
		for(int i = 0; i < table.length; ++i) {
			
			htmlTable += "<tr><td>"+times[i]+"</td>";
			
			for(int j = 0; j < table[i].length; ++j) {
				
				htmlTable += table[i][j];
			}
			
			htmlTable += "</tr>";
		}
		
		htmlTable += "</table>";
		
		_resp.getWriter().println(htmlTable);
	}
	
	//Creates the HTML formating for the table element
	private void courseBuilder(){
		
		String element = "";
		
		int rowspan=1;

		for(Section section : sections){
			
			System.out.println("Course Name: "+section.getName());
			
			if(ScheduleViewTests.testCourseValues(section)) {
				
				System.out.println("Passed Test!");
				
				LocalTime start = LocalTime.parse(section.getStartTime(), DateTimeFormat.forPattern("h:m a"));
				LocalTime end = LocalTime.parse(section.getEndTime(), DateTimeFormat.forPattern("h:m a"));


				rowspan += end.getValue(0)-start.getValue(0);
				
				element +=  "<td class='course' rowspan='"+rowspan+"'>"
							+"<b>"+section.getName()+"</b><br>"
							+section.getClassType()+"<br>"
							+start.toString("h:mm a")+" - "+end.toString("h:mm a")+"<br>"
							+section.getLocation()+"</td>";
				
				for(char day: section.getDay().toCharArray()) {
					
					this.table[Arrays.asList(times).indexOf(start.toString("h:mm a"))][new String(dates).indexOf(day)] = element;
					
					if(rowspan==2) {
						
						this.table[Arrays.asList(times).indexOf(start.toString("h:mm a"))+1][new String(dates).indexOf(day)] = "";
					}
				}
			}
		}
	}
}

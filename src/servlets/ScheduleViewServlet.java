package servlets;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdo.Course;
import jdo.Section;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import edu.uwm.cs361.CallBack;
import edu.uwm.cs361.Datastore;
import edu.uwm.cs361.Form;

/**
 * This servlet class implements the view to schedule Courses.
 * @author 5guys
 */
@SuppressWarnings("serial")
public class ScheduleViewServlet extends HttpServlet implements CallBack{
	
	private final int ACCESS_LEVEL = ACCESS_ALL;
	
	private String _courseID;
	
	private List<Course> _courses;
	
	private List<Section> _sections;
	
	private HttpServletRequest _req;
	
	private HttpServletResponse _resp;
	
	private Form _form;
	
	private String[][] _table = new String[11][5];
	
	private String[] _times = {"8:00 AM","9:00 AM","10:00 AM","11:00 AM","12:00 PM","1:00 PM","2:00 PM","3:00 PM","4:00 PM","5:00 PM","6:00 PM"};
	
	private char[] _dates = {'M','T','W','R','F'};
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		_req = req;
		
		_resp = resp;
		
		_courses = Datastore.getCourses(null);
		
		_courseID = getCourseID();
		
		_sections = Datastore.getSections("CourseID=='"+_courseID+"' && ClassType=='LEC'");
		
		_form = new Form(_req, _resp, new ArrayList<String>());
		
		_form.handleGet("Schedule Viewer", 2, this, "", ACCESS_LEVEL);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		doGet(req, resp);
	}
	
	/**
	 * Calls {@link #courseDropdown courseDropdown} and {@link #createSchedule createSchedule}.
	 */
	@Override
	public void printContent() throws IOException{
		
		courseDropdown();
		
		createSchedule();
	}

	/**
	 * Calls all validation methods to make sure that every single field entered is valid.
	 */
	@Override
	public void validate() {}
	
	/**
	 * Shows Courses in the dropdown menu.
	 * @throws IOException
	 */
	private void courseDropdown() throws IOException{
		
		String html = "<form action='schedule-view' method='post' class='standard-form'>"
				+"<select name='course'>";
		
		for(Course  course : _courses) {
			

			if(course.getID().equals(_courseID))
				html += "<option selected='selected' value='"+course.getID()+"'>"+course.getName()+"</option>";
			
			else
				html += "<option value='"+course.getID()+"'>"+course.getName()+"</option>";

		}
			
		html += "</select>"
					+"<div class='submit'><input type='submit' name='submit' class='button' value='Submit' /></div>"
				+"</form>";
		
		_resp.getWriter().println(html);
	}
	
	/**
	 * Creates the schedule.
	 * @throws IOException
	 */
	private void createSchedule() throws IOException{
		
		initSchedule();
		
		courseBuilder();
		
		String htmlTable = 
		"<table id='schedule'>"
			+"<tr>"
				+"<th>Time</th>"
				+"<th>Monday</th>"
				+"<th>Tuesday</th>"
				+"<th>Wednesday</th>"
				+"<th>Thursday</th>"
				+"<th>Friday</th>"
			+"</tr>";
		
		for(int i = 0; i < _table.length; ++i) {
			
			htmlTable += "<tr><td>"+_times[i]+"</td>";
			
			for(int j = 0; j < _table[i].length; ++j) {
				
				htmlTable += _table[i][j];
			}
			
			htmlTable += "</tr>";
		}
		
		htmlTable += "</table>";
		
		_resp.getWriter().println(htmlTable);
	}
	
	/**
	 * Course Builder.
	 */
	private void courseBuilder(){
		
		int rowspan = 1;
		
		String element = "";
		
		for(Section section : _sections) {
			
			if(testSection(section)) {
				
				LocalTime start = parseTime(section.getStartTime());
				
				LocalTime end =parseTime(section.getEndTime());

				rowspan += end.getValue(0) - start.getValue(0);
				
				element +=  "<td class='course' rowspan='"+rowspan+"'>"
								+"<b>"+section.getName()+"</b><br>"
								+start.toString("h:mm a")+" - "+end.toString("h:mm a")+"<br>"
								+section.getLocation() + "<br />"
								+"Lecture<br>"
							+"</td>";
				
				for(char day: section.getDay().toCharArray()) {
					
					fillTable(start, day, element, rowspan);
				}
			}
		}
	}

	/**
	 * Fills the custom table.
	 * @param start
	 * @param day
	 * @param element
	 * @param rowspan
	 */
	private void fillTable(LocalTime start, char day, String element, int rowspan) {
		
		//Convert X:30/45 AM to X:00 AM to avoid errors
		String temp = start.toString("h:mm a");
		
		temp = temp.substring(0, temp.indexOf(':')) + ":00" + temp.substring(temp.indexOf(" "));
		
		int startIndex = Arrays.asList(_times).indexOf(temp);
		
		int dayIndex = new String(_dates).indexOf(day);
		
		_table[startIndex][dayIndex] = element;
		
		if(rowspan > 2){
			for(int j = 0; j < rowspan - 1 ; ++j){
				_table[startIndex+j+1][dayIndex] = "";
			}
		}
	}

	/**
	 * @param time
	 * @return Local time parsed from the input string.
	 */
	private LocalTime parseTime(String time) {
		
		return LocalTime.parse(time, DateTimeFormat.forPattern("h:m a"));
	}

	/**
	 * @return The courseID
	 */
	private String getCourseID() {

		return (_req.getParameter("course") != null ? 
				_req.getParameter("course") : _courses.get(0).getID());
	}
	
	/**
	 * Initializes html table for the schedule.
	 */
	private void initSchedule() {
		
		for(int i = 0; i < _table.length; ++i) {
			
			for(int j = 0; j< _table[i].length; ++j) {
				
				_table[i][j] = "<td></td>";
			}
		}
	}
	
	/**
	 * Tests Section.
	 * @param section
	 * @return
	 */
	private boolean testSection(Section section){
		
		return (section.getStartTime() == "" || 
				section.getEndTime() == "" || 
				section.getDay() == "") ? false : true;
	}
}

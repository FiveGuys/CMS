package edu.uwm.cs361;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

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
	
	@Override
	public void printContent() throws IOException{
		
		courseDropdown();
		
		createSchedule();
	}

	@Override
	public void validate() {}
	
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
	
	private void courseBuilder(){
		
		int rowspan = 1;
		
		String element = "";
		
		for(Section section : _sections) {
			
			if(testSection(section)) {
				
				LocalTime start = LocalTime.parse(section.getStartTime(), DateTimeFormat.forPattern("h:m a"));
				
				LocalTime end = LocalTime.parse(section.getEndTime(), DateTimeFormat.forPattern("h:m a"));

				rowspan += end.getValue(0) - start.getValue(0);
				
				element +=  "<td class='course' rowspan='"+rowspan+"'>"
								+"<b>"+section.getName()+"</b><br>"
								+start.toString("h:mm a")+" - "+end.toString("h:mm a")+"<br>"
								+section.getLocation()
								+"Lecture<br>"
							+"</td>";
				
				for(char day: section.getDay().toCharArray()) {
					
					this._table[Arrays.asList(_times).indexOf(start.toString("h")+":00 "+start.toString("a"))][new String(_dates).indexOf(day)] = element;
					
					if(rowspan==2) {
						System.out.println(start.toString("h:mm a")+end.toString("h:mm a"));
						_table[Arrays.asList(_times).indexOf(start.toString("h")+":00"+start.toString("a"))+1][new String(_dates).indexOf(day)] = "";
					}
				}
			}
		}
	}

	private void fillTable(LocalTime start, char day, String element, int rowspan) {
		
		//Convert X:30/45 AM to X:00 AM to avoid errors
		String temp = start.toString("h:mm a");
		
		temp = temp.substring(0, temp.indexOf(':')) + ":00" + temp.substring(temp.indexOf(" "));
		
		int startIndex = Arrays.asList(_times).indexOf(temp);
		
		int dayIndex = new String(_dates).indexOf(day);
		
		_table[startIndex][dayIndex] = element;
		
		if(rowspan == 2) {
			
			_table[startIndex+1][dayIndex] = "";
		}
	}

	private LocalTime parseTime(String time) {
		
		return LocalTime.parse(time, DateTimeFormat.forPattern("h:m a"));
	}

	private String getCourseID() {

		return (_req.getParameter("course") != null ? 
				_req.getParameter("course") : _courses.get(0).getID());
	}
	
	private void initSchedule() {
		
		for(int i = 0; i < _table.length; ++i) {
			
			for(int j = 0; j< _table[i].length; ++j) {
				
				_table[i][j] = "<td></td>";
			}
		}
	}
	
	private boolean testSection(Section section){
		
		return (section.getStartTime() == "" || 
				section.getEndTime() == "" || 
				section.getDay() == "") ? false : true;
	}
}

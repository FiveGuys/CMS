package edu.uwm.cs361;

import java.util.List;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;

import org.joda.time.LocalTime;

@SuppressWarnings("serial")
public class ScheduleViewServlet extends HttpServlet{
	
	private String[][] table = new String[11][5];
	private boolean buildTable = false;
	private String courseID = "COMP SCI 201";
	//Yes, I wrote out this array because I am a scrub
	private String[] times = {"8:00 AM","9:00 AM","10:00 AM","11:00 AM","12:00 PM","1:00 PM","2:00 PM","3:00 PM","4:00 PM","5:00 PM","6:00 PM"};
	private char[] dates = {'M','T','W','R','F'};
	private boolean canCreateData = false;
	private String[] vals =new String[3];
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		for(int i = 0; i < table.length; ++i){
			for(int j = 0; j< table[i].length; ++j){
				table[i][j] = "<td></td>";
			}
		}
		if(canCreateData){
			createDummyContent();
			canCreateData = false;
		}
		displayForm(req, resp, courseID);
	}
	
	 //TODO doPost should take the class, instructor, or ta, from the select, and input elements from the datastore into the array table
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		courseID = req.getParameter("course");
		doGet(req, resp);
	}
	
	public void displayForm(HttpServletRequest req, HttpServletResponse resp, String course) throws IOException{
		
		HtmlOutputHelper.printHeader(resp, "Schedule Viewer", 2);
		
		createContent(req, resp, course);
		
		HtmlOutputHelper.printFooter(resp);
	}
	
	private void createContent(HttpServletRequest req, HttpServletResponse resp, String course) throws IOException{
		resp.setContentType("text/html");
		
		resp.getWriter().println(
			"<form action='schedule-view' method='post' class='standard-form'>"
				+"<select name='course'>"
					+Datastore.getAllCourses()
				+"</select>"
				+"<div class='submit'><input type='submit' name='submit' class='button' value='Submit' /></div>"
			+"</form>"
			+ createHTMLTable(req, resp, course)
		);
	}
	
	private String createHTMLTable(HttpServletRequest req, HttpServletResponse resp, String course) throws IOException{
		
		courseBuilder(course);
		
		String htmlTable = "<table id='schedule'>"
		+"<tr>"
			+"<th>Time</th>"
			+"<th>Monday</th>"
			+"<th>Tuesday</th>"
			+"<th>Wednesday</th>"
			+"<th>Thursday</th>"
			+"<th>Friday</th>"
		+"</tr>";
		
		for(int i = 0; i < table.length; ++i){
			
			htmlTable += "<tr>"+"<td>"+times[i]+"</td>";
			
			for(int j = 0; j < table[i].length; ++j){
				htmlTable += table[i][j];
			}
			
			htmlTable += "</tr>";
		}
		
		htmlTable += "</table>";
		
		return htmlTable;
	}
	
	//Creates the HTML formating for the table element
	private void courseBuilder(String courseID){
		String element = "";
		int rowspan=1;
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Class");
		List<Entity> courses = ds.prepare(q).asList(FetchOptions.Builder.withDefaults());
		
		for(Entity course: courses){
			if(course.getProperty("classNumber").equals(courseID)){
				LocalTime start = LocalTime.parse(course.getProperty("startTime").toString());
				LocalTime end = LocalTime.parse(course.getProperty("endTime").toString());

				rowspan += end.getValue(0)-start.getValue(0);
				
				element +=  "<td class='course' rowspan='"+rowspan+"'>"
				+"<b>"+course.getProperty("classNumber")+"</b><br>"
				+course.getProperty("classType")+"<br>"
				+start.toString("h:mm a")+" - "+end.toString("h:mm a")+"<br>"
				+course.getProperty("location")+"</td>";
				
				for(char day: course.getProperty("days").toString().toCharArray()){
					this.table[Arrays.asList(times).indexOf(start.toString("h:mm a"))][new String(dates).indexOf(day)] = element;
					if(rowspan==2){
						this.table[Arrays.asList(times).indexOf(start.toString("h:mm a"))+1][new String(dates).indexOf(day)] = "";
					}
				}
				
				officeHourBuilder(courseID,course);
				
			}
		}
		
	}
	
	private void officeHourBuilder(String courseID, Entity course){
		String element1 =  "<td class='course' rowspan='2'>"
				+"<b>"+course.getProperty("classNumber")+"</b><br>"
				+"Office Hours<br>"
				+"Rock, Jayson<br>"
				+"3:00 - 5:00 PM<br>"
				+"EMS W340</td>";
		table[7][2] = element1;
		table[8][2] = "";
	}
	
	//BUILT FOR TESTING PORPOISES
	private void createDummyContent(){
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Entity cs201 = new Entity("Class");
		cs201.setProperty("classNumber", "COMP SCI 201");
		cs201.setProperty("classType","Lecture");
		cs201.setProperty("days","MWF");
		cs201.setProperty("startTime","10:00");
		cs201.setProperty("endTime","10:50");
		cs201.setProperty("location","EMS E145");
		cs201.setProperty("instructor","Rock, Jayson");
		ds.put(cs201);
		
		Entity cs251 = new Entity("Class");
		cs251.setProperty("classNumber", "COMP SCI 251");
		cs251.setProperty("classType","Lecture");
		cs251.setProperty("days","TR");
		cs251.setProperty("startTime","13:00");
		cs251.setProperty("endTime","14:50");
		cs251.setProperty("location","EMS E190");
		cs251.setProperty("instructor","Rock, Jayson");
		ds.put(cs251);
		
		Entity cs351 = new Entity("Class");
		cs351.setProperty("classNumber", "COMP SCI 351");
		cs351.setProperty("classType","Lecture");
		cs351.setProperty("days","MW");
		cs351.setProperty("startTime","11:00");
		cs351.setProperty("endTime","12:15");
		cs351.setProperty("location","EMS E190");
		cs201.setProperty("instructor","Rock, Jayson");
		ds.put(cs351);
	}
}

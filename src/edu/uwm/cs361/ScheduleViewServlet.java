package edu.uwm.cs361;

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
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Section.class);
		q.declareParameters("String NameParam");
		q.setFilter("Name == NameParam");
		
		@SuppressWarnings("unchecked")
		List<Section> selectCourse = (List<Section>)q.execute(req.getParameter("course"));
		courseID = selectCourse.get(0).getCourseID();
		System.out.println("DoPost Doing THings");
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
					+selectBuilder()
				+"</select>"
				+"<div class='submit'><input type='submit' name='submit' class='button' value='Submit' /></div>"
			+"</form>"
			+ createHTMLTable(req, resp, course)
		);
	}

	@SuppressWarnings("unchecked")
	private String selectBuilder(){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String select = "";
		
		Query q = pm.newQuery(Section.class);
		q.declareParameters("String CourseIDParam");
		q.setFilter("CourseID == CourseIDParam");
		
		List<Section> courses = null;
		
		for(int i = 1; i < 46; ++i){
			courses = (List<Section>)q.execute(((Integer)i).toString());
			
			if(courses != null){
				select += "<option>"+ courses.get(0).getName()+"</option>";
			}
			
		}
		return select;
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
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		String element = "";
		int rowspan=1;
		

		Query q = pm.newQuery(Section.class);
		q.declareParameters("String CourseIDParam");
		q.setFilter("CourseID == CourseIDParam");
		@SuppressWarnings("unchecked")
		List<Section> courses = (List<Section>)q.execute(courseID);

		for(Section course: courses){
			System.out.println("Course Name: "+course.getName());
			if(ScheduleViewTests.testCourseValues(course) && course.getClassType().equals("LEC")){
				System.out.println("Passed Test!");
				LocalTime start = LocalTime.parse(course.getStartTime(), DateTimeFormat.forPattern("h:m a"));
				LocalTime end = LocalTime.parse(course.getEndTime(), DateTimeFormat.forPattern("h:m a"));


				rowspan += end.getValue(0)-start.getValue(0);
				
				element +=  "<td class='course' rowspan='"+rowspan+"'>"
				+"<b>"+course.getName()+"</b><br>"
				+course.getClassType()+"<br>"
				+start.toString("h:mm a")+" - "+end.toString("h:mm a")+"<br>"
				+course.getLocation()+"</td>";
				
				for(char day: course.getDay().toCharArray()){
					this.table[Arrays.asList(times).indexOf(start.toString("h:mm a"))][new String(dates).indexOf(day)] = element;
					if(rowspan==2){
						this.table[Arrays.asList(times).indexOf(start.toString("h:mm a"))+1][new String(dates).indexOf(day)] = "";
					}
				}
				
			}
		}
		
	}
	
	/*private void officeHourBuilder(String courseID, Entity course){
		String element1 =  "<td class='course' rowspan='2'>"
				+"<b>"+course.getProperty("classNumber")+"</b><br>"
				+"Office Hours<br>"
				+"Rock, Jayson<br>"
				+"3:00 - 5:00 PM<br>"
				+"EMS W340</td>";
		table[7][2] = element1;
		table[8][2] = "";
	}*/
	
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

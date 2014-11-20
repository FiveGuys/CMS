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


public class ViewAllCoursesServlet {
	
	HttpServletRequest _req;
	
	HttpServletResponse _resp;
	
	List<Course> courses;
	
	List<Section> sections;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		_req = req;
		
		_resp = resp;
		
		courses = Datastore.getCourses();
		
		sections = Datastore.getSections(null);
		
		displayForm();
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		doGet(req,resp);
	}
	
public void displayForm() throws IOException{
		
		_resp.setContentType("text/html");
		
		HtmlOutputHelper.printHeader(_resp, "View Courses", 1);
		
		createContent();
		
		HtmlOutputHelper.printFooter(_resp);
	}
	
	private void createContent() throws IOException{
		String html = "<span class='assign'>Courses in Computer Science </span><br /><br />";
		
		for(Course course: courses){
			
		}
		
		
		_resp.getWriter().println(html);
	}
}

package edu.uwm.cs361;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

public class Datastore 
{
	private HttpServletRequest _req;
	
	private Map<String, String> _user;
	
	private DatastoreService _datastore;
	
	private List<String> _errors;
	
 	public Datastore(HttpServletRequest req, HttpServletResponse resp, List<String> errors) {
 		
 		_datastore = DatastoreServiceFactory.getDatastoreService();
 		
 		_req = req;
 		
		_errors = errors;
		
		_user = findUser();
	}

 	private Map<String, String> findUser() {
 		
		Map<String, String> map = new HashMap<String, String>();
		
		List<Entity> users = _datastore.prepare(new Query("User")).asList(FetchOptions.Builder.withDefaults());

		for(Entity user : users) {
			
			if(user.getProperty("UserName").toString().equals(Form.getUserFromCookie(_req))) {
				 
				for(Map.Entry<String, Object> entry : user.getProperties().entrySet()) {
					
					map.put(entry.getKey(), entry.getValue().toString());
				}
				
				map.put("ID", user.getKey().getId() + "");
			}
		}

		return map;
	}
 	
 	public Map<String, String> getUser() {
 		
 		return _user;
 	}
	
	public String getAttrFromUser(String attr) {
		
		return _user.get(attr);
	}
	
	public static List<Entity> getAllUsers() {
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		return ds.prepare(new Query("User")).asList(FetchOptions.Builder.withDefaults());
	}
	
	public static List<String> getAllInstructors() {
		
 		return new ArrayList<String>();
 	}
	
	public static String getAllCourses() {

		return "<option>COMP SCI 201</option><option>COMP SCI 251</option><option>COMP SCI 351</option>";
 	}

	public static String getAllClasses() {

		return "<option>COMP SCI 201</option><option>COMP SCI 251</option><option>COMP SCI 351</option>";
 	}
	
	private void addUser() {
		
		String firstName = _req.getParameter("FirstName");
		String lastName = _req.getParameter("LastName");
		String access = _req.getParameter("Access");
		
		if(!userExists(firstName+"."+lastName)) {
			
			Entity user = new Entity("User", getCount("User") + 1);
			
			user.setProperty("UserName", firstName + "." + lastName);
			user.setProperty("Password", lastName); 
			user.setProperty("FirstName", firstName);
			user.setProperty("MiddleName", "");
			user.setProperty("LastName", lastName);
			user.setProperty("Email", "");
			user.setProperty("Location", "");
			user.setProperty("Phone", "");
			user.setProperty("AltPhone", "");
			user.setProperty("OfficeHour1", "Wed;0;00;0;01");
			user.setProperty("OfficeHour2", "Wed;0;00;0;00");
			user.setProperty("OfficeHour3", "Wed;0;00;0;00");
			user.setProperty("Access", access);
			user.setProperty("Semester", "2149");
			
			_datastore.put(user);
		}
	}
	
	/* Creating new addCourse, keeping this here for reference
	 * 
	 * public void addCourse(String[] courseData) {
		
		Entity course = new Entity("Section", courseData[0]);
		
		String[] section = courseData[3].split(" ");
		String[] time = courseData[5].split("-");
		
		course.setProperty("Name", courseData[1]);
		course.setProperty("Units", courseData[2]);
		course.setProperty("ClassType", (section.length == 2 ? section[0] : ""));
		course.setProperty("Section",   (section.length == 2 ? section[1] : ""));
		course.setProperty("ClassNum", courseData[4]);
		course.setProperty("StartTime", (time.length == 2 ? time[0] : "") );
		course.setProperty("EndTime", (time.length == 2 ? time[1] : ""));
		course.setProperty("Day", courseData[6]);
		course.setProperty("Instructor", courseData[8]);
		course.setProperty("Location", courseData[9]);
		course.setProperty("CourseID", courseData[10]);
		
		_datastore.put(course);
	}
	*/
	
	public void addCourse(String[] CourseData) {
		String[] section = CourseData[3].split(" ");
		String[] time = CourseData[5].split("-");
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		try{
			Course course = new Course(
					section.length == 2 ? section[1] : "",
					CourseData[1],
					CourseData[2],
					section.length == 2 ? section[0] : "",
					CourseData[4],
					time.length == 2 ? time[0] : "", 
					time.length == 2 ? time[1] : "",
					CourseData[6],
					CourseData[8],
					CourseData[9],
					CourseData[10]);
			
			pm.makePersistent(course);
		}
		finally{
			pm.close();
		}
	}
	
	public void addClass(int classID, String className) {
		
		Entity classObj = new Entity("Course", classID);
		
		classObj.setProperty("Name", className);
		
		_datastore.put(classObj);
	}
	
	private void updateUser(String userID) {
		
		try {
			
			Entity user = _datastore.get(KeyFactory.createKey("User", Long.parseLong(userID, 10)));
			
			user.setProperty("MiddleName", _req.getParameter("MiddleName"));
			user.setProperty("Email", _req.getParameter("Email"));
			user.setProperty("Location", _req.getParameter("Location"));
			user.setProperty("Phone", _req.getParameter("Phone"));
			user.setProperty("AltPhone", _req.getParameter("AltPhone"));
			user.setProperty("OfficeHour1", Form.calcOfficeHours(1, _req));
			user.setProperty("OfficeHour2", Form.calcOfficeHours(2, _req));
			user.setProperty("OfficeHour3", Form.calcOfficeHours(3, _req));
			
			_datastore.put(user);
			
		} catch (EntityNotFoundException e) { }
	}

	public void callMethod(String methodName) throws IOException {
		
		if(_errors.size() == 0) {
			
			switch(methodName) {
			
				case "updateUser": this.updateUser(getAttrFromUser("ID")); break;
				case "addUser": this.addUser(); break;
				default: throw new IOException("Datastore.callMethod: "+methodName+" not found");
			}
		}
	}
	
	private boolean userExists(String username) {
		
		List<Entity> users = Datastore.getAllUsers();
		
		for(Entity user : users){
			
			if(username.equalsIgnoreCase((String) user.getProperty("UserName"))) {
				
				_errors.add("This person is already a user");
				
				return true;
			}
		}
		
		return false;
	}

	public void addAdmin() {
		
		Entity user = new Entity("User", 1);
		
		user.setProperty("UserName", "admin.pass"); 
		user.setProperty("Password", "pass"); 
		user.setProperty("FirstName", "admin");
		user.setProperty("MiddleName", "");
		user.setProperty("LastName", "pass");
		user.setProperty("Email", "admin@uwm.edu");
		user.setProperty("Location", "");
		user.setProperty("Phone", "");
		user.setProperty("AltPhone", "");
		user.setProperty("OfficeHour1", "Wed;0;00;0;00");
		user.setProperty("OfficeHour2", "Wed;0;00;0;00");
		user.setProperty("OfficeHour3", "Wed;0;00;0;00");
		user.setProperty("Access", "3");
		user.setProperty("Semester", "2149");
		
		_datastore.put(user);
		
	}

	public int getCount(String entity) {
		
		return _datastore.prepare(new Query(entity)).countEntities(FetchOptions.Builder.withDefaults());
	}
}

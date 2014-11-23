package edu.uwm.cs361;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import com.google.appengine.api.blobstore.BlobKey;

public class Datastore 
{
	private HttpServletRequest _req;
	
	private User _user;
	
	private List<String> _errors;
	
	private static final PersistenceManager _pm = PMF.get().getPersistenceManager();
	
 	public Datastore(HttpServletRequest req, HttpServletResponse resp, List<String> errors) {
 		
 		_req = req;
 		
		_errors = errors;
		
		_user = findUser();
	}
 	
 	public User getUser() {
 		
 		return _user;
 	}
	
	@SuppressWarnings("unchecked")
	public static List<User> getUsers(String query) {
		
		Query q = _pm.newQuery(User.class);
		
		if(query != null) {
			
			q.setFilter(query);
		}
		
		return (List<User>) q.execute();
	}
	
	@SuppressWarnings("unchecked")
	public static List<Course> getCourses(String query) {

		Query q = _pm.newQuery(Course.class);
		
		if(query != null) {

			q.setFilter(query);
		}
		
		List<Course> courses = (List<Course>) q.execute();
		
		Collections.<Course>sort(courses);

		return courses;
 	}
	
	@SuppressWarnings("unchecked")
	public static List<Section> getSections(String query) {

		Query q = _pm.newQuery(Section.class);
		
		if(query != null) {
			
			q.setFilter(query);
		}
		
		return (List<Section>) q.execute();
 	}
	
	public static List<User> getAllInstructors() {
		
 		return Datastore.getUsers("Access=='2'");
 	}

	private User findUser() {
 		
 		String username = Form.getUserFromCookie(_req);
 		
 		String query = "UserName=='"+username+"'";
 		
 		return Datastore.getUsers(query).get(0);
	}
	
	public void addCourse(String courseID, String name) {
		
		Course course = new Course(courseID, name);
		
		_pm.makePersistent(course);
	}

	public void addSection(String[] courseData) {
		
		String[] sectionNum = courseData[3].split(" ");
		String[] time = courseData[5].split("-");
		
		Section section = new Section();
		
		section.setID(courseData[0]);
		section.setName(courseData[1]);
		section.setUnits(courseData[2]);
		section.setClassNum(courseData[4]);
		section.setDay(courseData[6]);
		section.setInstructorID("");
		section.setLocation(courseData[9]);
		section.setCourseID(courseData[10]);
			
		section.setStartTime(time.length == 2 ? time[0] : "");
		section.setEndTime(time.length == 2 ? time[1] : "");
		
		section.setClassType(sectionNum.length == 2 ? sectionNum[0] : "");
		section.setSection(sectionNum.length == 2 ? sectionNum[1] : "");
		
		_pm.makePersistent(section);
	}
	
	// Calls private method depending on servlet
	public void callMethod(String methodName) throws IOException {
		
		if(_errors.size() == 0) {
			
			switch(methodName) {
			
				case "updateUser": this.updateUser(); break;
				case "addUser": this.addUser(); break;
				case "searchUser": this.searchUser(); break;
				case "editCourse": this.editCourse(); break;
				case "": break;
				default: throw new IOException("Datastore.callMethod: "+methodName+" not found");
			}
		}
	}
	
	private void editCourse() throws IOException {
		
		String SectionID = _req.getParameter("SectionID");
		
		Section section = Datastore.getSections("SectionID=='"+SectionID+"'").get(0);
		
		LocalTime start = LocalTime.parse(_req.getParameter("start-start")+":"+_req.getParameter("start-end"),DateTimeFormat.forPattern("H:m"));
		
		LocalTime end = LocalTime.parse(_req.getParameter("end-start")+":"+_req.getParameter("end-end"),DateTimeFormat.forPattern("H:m"));
		
		if((start.getValue(0)+(start.getValue(1)/60)) - (end.getValue(0)+(end.getValue(1)/60)) < 0){
			throw new IOException("Datastore.editCourse: end time is before start time"+'\n'+" Start: "+start.toString("h:mm a")+"End:"+end.toString("h:mm a"));
		}
		
		//TODO set section fields
		section.setSection(_req.getParameter("Section"));
		section.setClassNum(_req.getParameter("ClassNum"));
		section.setUnits(_req.getParameter("Units"));
		section.setLocation(_req.getParameter("Location"));
		section.setStartTime(start.toString("h:mm a"));
		section.setEndTime(end.toString("h:mm a"));
		
		_pm.makePersistent(section);
		
		
	}

	public static void addAdmin() {
		
		if(Datastore.getUsers(null).size() == 0) {
			
			User user = new User();
			
			user.setID(newUserID());
			user.setUserName("admin.pass"); 
			user.setPassword( "pass"); 
			user.setFirstName( "admin");
			user.setMiddleName( "");
			user.setLastName( "pass");
			user.setEmail( "admin@uwm.edu");
			user.setLocation( "");
			user.setPhone( "");
			user.setAltPhone( "");
			user.setOfficeHour1( "Wed;0;00;0;00");
			user.setOfficeHour2( "Wed;0;00;0;00");
			user.setOfficeHour3( "Wed;0;00;0;00");
			user.setAccess("3");
			user.setSemester("2149");
			
			_pm.makePersistent(user);
		}
	}
	
	private void updateUser() {
		
		User user = getUser();
		
		user.setID(newUserID());
//		user.setFirstName( _req.getParameter("FirstName"));
//		user.setLastName( _req.getParameter("LastName"));
//		user.setPassword( _req.getParameter("Password"));
		user.setMiddleName( _req.getParameter("MiddleName"));
		user.setEmail( _req.getParameter("Email"));
		user.setLocation( _req.getParameter("Location"));
		user.setPhone( _req.getParameter("Phone"));
		user.setAltPhone( _req.getParameter("AltPhone"));
		user.setOfficeHour1( Form.calcOfficeHours(1, _req));
		user.setOfficeHour2( Form.calcOfficeHours(2, _req));
		user.setOfficeHour3(Form.calcOfficeHours(3, _req));
		
		_pm.makePersistent(user);
	}

	private void searchUser() {
		
		String firstName = _req.getParameter("FirstName");
		
		String lastName = _req.getParameter("LastName");
		
		if(!userExists(firstName+"."+lastName)) {
			
			_errors.add("No User Found");
		}
	}

	private boolean userExists(String username) {
		
		List<User> users = Datastore.getUsers("UserName=='"+username+"'");
		
		return (users.size() != 0);
	}
 	
	private void addUser() {
		
		String firstName = _req.getParameter("FirstName");
		String lastName = _req.getParameter("LastName");
		String access = _req.getParameter("Access");
		
		if(!userExists(firstName+"."+lastName)) {
			
			User user = new User();
			
			user.setID(newUserID());
			user.setUserName( firstName + "." + lastName);
			user.setPassword( lastName); 
			user.setFirstName( firstName);
			user.setMiddleName( "");
			user.setLastName( lastName);
			user.setEmail("");
			user.setLocation( "");
			user.setPhone( "");
			user.setAltPhone( "");
			user.setOfficeHour1( "Wed;0;00;0;00");
			user.setOfficeHour2( "Wed;0;00;0;00");
			user.setOfficeHour3( "Wed;0;00;0;00");
			user.setAccess(access);
			user.setSemester("2149");
			
			_pm.makePersistent(user);
			
		} else {
			
			_errors.add("This person is already a user");
		}
	}

	public void deleteCourses() {
		
		List<Course> courses = Datastore.getCourses(null);
		List<Section> sections = Datastore.getSections(null);
		
		_pm.deletePersistentAll(courses);
		_pm.deletePersistentAll(sections);
	}
	
	private static String newUserID() {

		return (Datastore.getUsers(null).size() + 1) + "";
	}

	public void setImage(String blobKey) {
		
		_user.setImage(blobKey);
	}
}

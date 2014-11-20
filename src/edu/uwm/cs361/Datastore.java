package edu.uwm.cs361;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

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
	
	@SuppressWarnings({ "unchecked", "null" })
	public static List<Course> getCourses() {

		Query q = _pm.newQuery(Course.class);
		
		q.setFilter("name == CourseIDParam");
		
		q.declareParameters("String CourseIDParam");
		
		q.setUnique(true);
		
		List<Course> courses = new ArrayList<Course>();
		
		for(int i = 1; i < 46; ++i){
			
			Course course = (Course) q.execute(((Integer)i).toString());
			
			if(course != null)
				courses.add(course);
		}
		
		return courses;
 	}
	
	@SuppressWarnings("unchecked")
	public static List<Section> getSections(String query) {

		Query q = _pm.newQuery(Section.class,query);
		
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
	
	public void addCourse(String courseID, String name) {
		
		Course course = new Course(courseID, name);
		
		_pm.makePersistent(course);
	}

	public void addSection(String[] courseData) {
		
		String[] sectionNum = courseData[3].split(" ");
		String[] time = courseData[5].split("-");
		
		Section section = new Section(
			courseData[0],							//ID	
			sectionNum.length == 2 ? sectionNum[1] : "", //section
			courseData[1],//Name
			courseData[2],
			sectionNum.length == 2 ? sectionNum[0] : "",
			courseData[4],
			time.length == 2 ? time[0] : "", 
			time.length == 2 ? time[1] : "",
			courseData[6],
			courseData[8],
			courseData[9],
			courseData[10]
		);
			
		_pm.makePersistent(section);
	}
	
	// Calls private method depending on servlet
	public void callMethod(String methodName) throws IOException {
		
		if(_errors.size() == 0) {
			
			switch(methodName) {
			
				case "updateUser": this.updateUser(); break;
				case "addUser": this.addUser(); break;
				case "searchUser": this.searchUser(); break;
				case "": break;
				default: throw new IOException("Datastore.callMethod: "+methodName+" not found");
			}
		}
	}
	
	public static void addAdmin() {
		
		if(Datastore.getUsers(null).size() == 0) {
			
			User user = new User();
			
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

	//TODO Ryan see Dee for thee helpee
	
	private void searchUser() {
		
		String firstName = _req.getParameter("FirstName");
		String lastName = _req.getParameter("LastName");
		
		if(userExists(firstName+"."+lastName)) {
			//TODO not sure how to do this yet RJP
		}else{
			
		}
		
	}

	private boolean userExists(String username) {
		
		List<User> users = Datastore.getUsers("UserName=='"+_user.getUserName()+"'");
		
		if(users.size() != 0) {
			
			_errors.add("This person is already a user");
			
			return true;
		}
		
		return false;
	}
 	
	
	private void addUser() {
		
		String firstName = _req.getParameter("FirstName");
		String lastName = _req.getParameter("LastName");
		String access = _req.getParameter("Access");
		
		if(!userExists(firstName+"."+lastName)) {
			
			User user = new User();
			
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
		}
	}

	public void deleteCourses() {
		/*
		List<Course> course = Datastore.getAllCourses(null);
		List<Section> section = Datastore.getAllSections(null);
		
		_pm.deletePersistentAll(course);
		_pm.deletePersistentAll(section);
		*/
	}
}

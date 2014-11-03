package edu.uwm.cs361;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class Datastore 
{
	private HttpServletRequest _req;
	private HttpServletResponse _resp;
	private List<String> _errors;
	private DatastoreService ds;
 	public Datastore(HttpServletRequest req, HttpServletResponse resp, List<String> errors) {
 		
 		ds = DatastoreServiceFactory.getDatastoreService();
 		
 		_req = req;
		_resp = resp;
		_errors = errors;
	}

	public static List<String> getAllInstructors() {
 		return new ArrayList<String>();
 	}
 	
 	public static boolean saveCourse(HttpServletRequest req, List<String> errors) {
/*
		
		String firstName = req.getParameter("firstName");
		String lastName = req.getParameter("lastName");
		String access = req.getParameter("access");

		List<String> errors = new ArrayList<String>();

		if (firstName.isEmpty()) {
			errors.add("First Name is required.");
		}

		if (lastName.isEmpty()) {
			errors.add("Last Name is required.");
		}
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		System.out.println("Gotten username: " + firstName);
		System.out.println("GOtten last name" + lastName);
		System.out.println("Gotten access :" + access);
		//Key[] s = {KeyFactory.createKey("User", "username")};
		//ds.delete();
		Query q = new Query("User");
		
		List<Entity> users = ds.prepare(q).asList(FetchOptions.Builder.withDefaults());
		//int i = 0;
		//List<Key> keys = new ArrayList<Key>();
		int f = 1;
		for(Entity u : users){
			//keys.add(u.getKey());
			System.out.println("datastore first name" + (f++) + ":" + u.getProperty("FirstName").toString());
			if((u.getProperty("FirstName").toString().equalsIgnoreCase(firstName)) && u.getProperty("LastName").toString().equalsIgnoreCase(lastName)) 
				errors.add("This person is already a user");
		}
		//ds.delete(keys);
		if (errors.size() == 0) {
			Entity user = new Entity("User");
			user.setProperty("FirstName", firstName);
			user.setProperty("LastName", lastName);
			user.setProperty("Access", access);
			ds.put(user);			
		}
		*/
 		return true;
 	}

	public void callMethod(String methodName) {
		
		if(_errors.size() == 0 && _req.getParameter("Submit") != null) {
			
			switch(methodName) {
			
				case "addCourse": this.addCourse(); break;
				case "updateInfo": this.updateInfo(); break;
			}
		}
	}

	private void addCourse() {
		
		
	}

	private void updateInfo() {
		
		System.out.println("updateInfo");
	}

	public String getAttributeFromUser(String string) {
		// TODO Auto-generated method stub
		return "3";
	}

	public boolean validateLogin(String user, String pass) {
		
		if(user.equals("admin.pass") && pass.equals("pass")) {
			
			return true;
		}
		
		return false;
	}

	public Map<String, String> getUser() {
	
		Map<String, String> map = new HashMap<String, String>();
		
		List<Entity> users = ds.prepare(new Query("User")).asList(FetchOptions.Builder.withDefaults());

		for(Entity user : users){
			
			if(user.getProperty("UserName").toString().equals(Form.getUserFromCookie(_req))) {
				 
				for(Map.Entry<String, Object> entry : user.getProperties().entrySet())
				{
					map.put(entry.getKey(), entry.getValue().toString());
				}
			}
		}

		return map;
	}
}

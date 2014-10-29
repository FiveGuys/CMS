package edu.uwm.cs361;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class Datastore 
{
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
}

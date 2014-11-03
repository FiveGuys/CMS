package edu.uwm.cs361;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.*;

@SuppressWarnings("serial")
public class AddUserServlet extends HttpServlet
{
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
		displayForm(req, resp, new ArrayList<String>());
		
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

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

		//System.out.println("Gotten username: " + firstName);
		//System.out.println("GOtten last name" + lastName);
		//System.out.println("Gotten access :" + access);
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
			if(u.getProperty("UserName").toString().equalsIgnoreCase(firstName+"."+lastName)) 
				errors.add("This person is already a user");
		}
		//ds.delete(keys);
		if (errors.size() == 0) {
			
			Entity user = new Entity("User");
			
			user.setProperty("UserName", firstName + "." + lastName);
			user.setProperty("Password", lastName); 
			user.setProperty("FirstName", firstName);
			user.setProperty("MiddleName", "");
			user.setProperty("LastName", lastName);
			user.setProperty("Email", "");
			user.setProperty("Location", "");
			user.setProperty("Phone", "");
			user.setProperty("AltPhone", "");
			user.setProperty("OfficeHour1", "Wed;0;00;0;00");
			user.setProperty("OfficeHour2", "Wed;0;00;0;00");
			user.setProperty("OfficeHour3", "Wed;0;00;0;00");
			user.setProperty("Access", access);
			ds.put(user);			
		}
		
		displayForm(req, resp, errors);
	}
	
	private void displayForm(HttpServletRequest req, HttpServletResponse resp, List<String> errors) throws IOException {
		
		HtmlOutputHelper.printHeader(resp, "Add User", 0);
		
		HtmlOutputHelper.printErrors(resp, errors);
		
		printContent(resp);
		
		HtmlOutputHelper.printFooter(resp);
	}
	
	private void printContent(HttpServletResponse resp)  throws IOException {
		
		resp.getWriter().println( 
				"<form action='/add-user' method='post' class='standard-form'>" + 
					"<h1>Add User</h1>" +
					//(errors.size() == 0 ? "<span>User has been created</span>" : "") +
					"<label>" +
						"<span>First Name:</span>" +
						"<input id='firstName' type='text' name='firstName' />" +
					"</label>" +
					"<label>" +
					  "<span>Last Name: </span>" +
					  "<input id='lastName' type='text' name='lastName' />" +
					"</label>" +
					"<label>" +   
					  "<span>Access Level: </span>" +
						"<select name='access'>" +
							"<option value='3'>Administrator</option>" +
							"<option value='2'>Professor</option>" +
							"<option value='1'>TA</option>" +
						"</select>" +
					"</label>" +
				"<div class='submit'><input type='submit' class='button' value='Save' /></div>" +
				"</form>" +
			"</div>");
	}
}
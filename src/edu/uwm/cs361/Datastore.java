package edu.uwm.cs361;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	
	private HttpServletResponse _resp;
	
	private Map<String, String> _user;
	
	private DatastoreService _datastore;
	
	private List<String> _errors;
	
 	public Datastore(HttpServletRequest req, HttpServletResponse resp, List<String> errors) {
 		
 		_datastore = DatastoreServiceFactory.getDatastoreService();
 		
 		_req = req;
 		
		_resp = resp;
		
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
				
				default: throw new IOException("Datastore.callMethod: "+methodName+" not found");
			}
		}
	}
}

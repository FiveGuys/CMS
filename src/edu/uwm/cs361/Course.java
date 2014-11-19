package edu.uwm.cs361;

import javax.jdo.annotations.*;

@PersistenceCapable
public class Course {
	
	@PrimaryKey
	@Persistent
	private String name;
	public String getID() { return name; }
	public void setID(String ID) { this.name = ID; }
	
	@Persistent
	private String CourseName;
	public String getName() { return CourseName; }
	public void setName(String Name) { this.CourseName = Name; }
	
	public Course(String courseID, String Name) {
		
		// name is used to set ID/Name in datastore
		this.name = courseID;
		this.CourseName = Name;
	}
}
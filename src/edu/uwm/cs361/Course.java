package edu.uwm.cs361;

import javax.jdo.annotations.*;

import com.google.appengine.api.datastore.Key;

/**
 * This class defines the CMS course objects.
 * @author 5guys
 */
@PersistenceCapable
public class Course implements Comparable<Course> {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String CourseID;
	
	@Persistent
	private String CourseName;
	
	public String getID() { return CourseID; }
	public void setID(String ID) { this.CourseID = ID; }
	
	public String getName() { return CourseName; }
	public void setName(String Name) { this.CourseName = Name; }
	
	public Course(String CourseID, String Name) {
		
		// name is used to set ID/Name in datastore
		this.CourseID = CourseID;
		this.CourseName = Name;
	}
	
	/** Compares two Course IDs
	 * @param other if this ID is greater returns -1, otherwise returns 1
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Course other) {
		
		return (Integer.parseInt(this.getID()) > Integer.parseInt(other.getID())) ? 1 : -1;
	}
}

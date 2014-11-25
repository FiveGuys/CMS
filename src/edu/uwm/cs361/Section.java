package edu.uwm.cs361;

import javax.jdo.annotations.*;

import com.google.appengine.api.datastore.Key;

/**
 * This class manages a Section.
 * @author 5guys
 */
@PersistenceCapable
public class Section implements Comparable<Section> {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String SectionID;
	@Persistent
	private String Section;
	@Persistent
	private String CourseName;
	@Persistent
	private String Units;
	@Persistent
	private String ClassType;
	@Persistent
	private String ClassNum;
	@Persistent
	private String StartTime;
	@Persistent
	private String EndTime;
	@Persistent
	private String Day;
	@Persistent
	private String InstructorID;
	@Persistent
	private String Location;
	@Persistent
	private String CourseID;
	
	public String getID() { 
		return SectionID; 
	}
	public void setID(String ID) { 
		this.SectionID = ID; 
	}
	public String getSection() {
		return Section;
	}
	public void setSection(String section) {
		Section = section;
	}
	public String getName() {
		return CourseName;
	}
	public void setName(String name) {
		CourseName = name;
	}
	public String getUnits() {
		return Units;
	}
	public void setUnits(String units) {
		Units = units;
	}
	public String getClassType() {
		return ClassType;
	}
	public void setClassType(String classType) {
		ClassType = classType;
	}
	public String getClassNum() {
		return ClassNum;
	}
	public void setClassNum(String classNum) {
		ClassNum = classNum;
	}
	public String getStartTime() {
		return StartTime;
	}
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}
	public String getEndTime() {
		return EndTime;
	}
	public void setEndTime(String endTime) {
		EndTime = endTime;
	}
	public String getDay() {
		return Day;
	}
	public void setDay(String day) {
		Day = day;
	}
	public String getInstructorID() {
		return InstructorID;
	}
	public void setInstructorID(String instructorID) {
		InstructorID = instructorID;
	}
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}
	public String getCourseID() {
		return CourseID;
	}
	public void setCourseID(String courseID) {
		CourseID = courseID;
	}
	
	/** 
	 * Compares Sections.
	 * @return 1 if the current Section# is greater than the parameter section#. Else, returns -1.
	 */
	@Override
	public int compareTo(Section other) {
		if(this.getSection() == "" && other.getSection() != ""){
			return -1;
		}
		
		else if(this.getSection() != "" && other.getSection() == ""){
			return 1;
		}
		
		else if(this.getSection() == "" && other.getSection() == ""){
			return 1;
		}
		else{
			return (Integer.parseInt(this.getSection()) > Integer.parseInt(other.getSection())) ? 1 : -1;
		}
	}
}

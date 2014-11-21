package edu.uwm.cs361;

import javax.jdo.annotations.*;

@PersistenceCapable
public class Section implements Comparable<Section> {
	@PrimaryKey
	@Persistent
	private String name;
	
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
	private String Instructor;
	@Persistent
	private String Location;
	@Persistent
	private String CourseID;
	public Section(String ID, String section, String courseName, String units, String classType, String classNum,
			String startTime, String endTime, String day, String instructor,
			String location, String courseID) {
		this.name = ID;
		Section = section;
		CourseName = courseName;
		Units = units;
		ClassType = classType;
		ClassNum = classNum;
		StartTime = startTime;
		EndTime = endTime;
		Day = day;
		Instructor = instructor;
		Location = location;
		CourseID = courseID;
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
	public String getInstructor() {
		return Instructor;
	}
	public void setInstructor(String instructor) {
		Instructor = instructor;
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
	@Override
	public int compareTo(Section other) {
		
		return (Integer.parseInt(this.getSection()) >= Integer.parseInt(other.getSection())) ? 1 : -1;
	}
}

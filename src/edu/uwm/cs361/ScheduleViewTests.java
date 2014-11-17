package edu.uwm.cs361;

public class ScheduleViewTests {

	public static boolean testCourseValues(Course course){
		boolean returnValue = true;
		
		returnValue = course.getStartTime() == "" ? false : true && course.getEndTime() == "" ? false : true && course.getDay() == "" ? false : true;
		
		return returnValue;
	}
}

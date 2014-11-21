package edu.uwm.cs361;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class EditCourseServlet extends HttpServlet {
	
	List<Course> _courses;
	
	List<Section> _sections;
	
	Course selectCourse;
	
	Section selectSection;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		_courses = Datastore.getCourses(null);
		
		_sections = Datastore.getSections(null);
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		
	}
	
	private void printContent() throws IOException {
		
	}
	
	private String buildSelect(){
		
		String html = "<form action='edit-course' method='post' class='standard-form'>"
				+"<select name='course'>";
		
		for(Course course: _courses){
			html += "<option value='"+course.getID()+"'>"+course.getName()+"</option>";
		}
		
		
		return html;
	}

	

}

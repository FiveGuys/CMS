package edu.uwm.cs361;

import java.util.List;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ViewCoursesServlet extends HttpServlet implements CallBack {
	
	HttpServletRequest _req;
	
	HttpServletResponse _resp;
	
	List<Course> _courses;
	
	List<Section> _sections;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		_req = req;
		
		_resp = resp;
		
		_courses = Datastore.getCourses(null);
		
		_sections = Datastore.getSections(null);
		
		printContent();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		doGet(req,resp);
	}
	
	@Override
	public void printContent() throws IOException {
		displayForm();
	}
	
	public void displayForm() throws IOException{
		
		_resp.setContentType("text/html");
		
		HtmlOutputHelper.printHeader(_resp, "View Courses", 1);
		
		createContent();
		
		HtmlOutputHelper.printFooter(_resp);
	}
	
	private void createContent() throws IOException{
		_resp.setContentType("text/html");
		
		String html = "<div class='view-courses'><span class='assign'>Courses in Computer Science </span><br /><br />";
		
		for(Course course: _courses){
			
			html += "<span class='subhead'>"+course.getName()+"</span>"
					+"<table border='0' cellpadding='2' cellspacing='0'  width='100%'>"
					  +"<tbody>"
					  +"<tr class='body copy bold white' bgcolor='#666666'>"
					  +"<th>GER</th>"
	                  +"<th>&nbsp;</th>"
	                  +"<th>Units</th>"
	                  +"<th>Section</th>"
	                  +"<th>Class#</th>"
	                  +"<th>Hours</th>"
	                  +"<th>Days</th>"
	                  +"<th>Instructor</th>"
	                  +"<th>Room</th>"
	                  +"<th>Syllabus</th>"
	                  +"</tr>";
			
			for(Section section: _sections){
				
				if(section.getCourseID().equals(course.getID())){
					html+="<tr class='body copy' bgcolor='#F4F4F4'>"
							+"<td style=''>&nbsp;</td>"
							+"<td style=''>(FEE)</td>"
							+"<td style=''>"+section.getUnits()+"</td>"
							+"<td style=''>"+section.getClassType()+" "+section.getSection()+"</td>"
							+"<td style=''>"+section.getClassNum()+"</th>"
							+"<td style=''>"+section.getStartTime()+"-"+section.getEndTime()+"</td>"
							+"<td style=''>"+section.getDay()+"</td>"
							+"<td style=''>"+section.getInstructor()+"</td>"
							+"<td style=''>"+section.getLocation()+"</td>"
							+"<td style=''>&nbsp;</td>"
							+"</tr>";	
				}
			}
			html+="</tbody>"
				+"</table>";
		}
		
		html+="</div>";
		
		_resp.getWriter().println(html);
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		
	}
}

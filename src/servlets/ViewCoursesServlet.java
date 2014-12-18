package servlets;

import java.util.Collections;
import java.util.List;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdo.Course;
import jdo.Section;
import jdo.User;
import edu.uwm.cs361.Datastore;
import edu.uwm.cs361.HtmlOutputHelper;

/**
 * This servlet class manages the view of Courses.
 * @author 5guys
 */
@SuppressWarnings("serial")
public class ViewCoursesServlet extends HttpServlet {
	
	HttpServletRequest _req;
	
	HttpServletResponse _resp;
	
	List<Course> _courses;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		_req = req;
		
		_resp = resp;
		
		_courses = Datastore.getCourses(null);
		
		displayForm();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		doGet(req,resp);
	}
	
	/**
	 * Displays the form by calling {@link #printHeader printHeader}, 
	 * {@link #printContent printContent} and {@link #printFooter printFooter}.
	 * @throws IOException
	 */
	public void displayForm() throws IOException{
		
		_resp.setContentType("text/html");
		
		HtmlOutputHelper.printHeader(_req, _resp, "View Courses", 1);
		
		printContent();
		
		HtmlOutputHelper.printFooter(_resp);
	}
	
	/**
	 * Prints html content.
	 * @throws IOException
	 */
	private void printContent() throws IOException{
		
		String html = "<div class='view-courses'><span class='assign'>Courses in Computer Science </span><br /><br />";
		
		for(Course course: _courses){
			
			html += "<span>"+course.getName()+"</span>"
				+"<table>"
				  +"<tr>"
	                  +"<th>Section</th>"
	                  +"<th>Class #</th>"
	                  +"<th class='small'>Units</th>"
	                  +"<th>Hours</th>"
	                  +"<th class='small'>Days</th>"
	                  +"<th>Instructor</th>"
	                  +"<th>Room</th>"
	              +"</tr>";
			
			List<Section> sections = Datastore.getSections("CourseID=='"+course.getID()+"'");
			
			Collections.sort(sections);
			
			for(Section section : sections) {
				
				String link = "<a href='edit-course?SectionID="+section.getID()+"'>";
				
				String instructorName = getInstructorName(section.getInstructorID());
				
				html+="<tr>"
						+"<td>"+link+section.getClassType()+" "+section.getSection()+"</a></td>"
						+"<td>"+link+section.getClassNum()+"</a></td>"
						+"<td class='small'>"+link+section.getUnits()+"</a></td>"
						+"<td>"+link+section.getStartTime()+"-"+section.getEndTime()+"</a></td>"
						+"<td class='small'>"+link+section.getDay()+"</a></td>"
						+"<td>"+link+instructorName+"</a></td>"
						+"<td>"+link+section.getLocation()+"</a></td>"
						+"</tr>";	
			}
			
			html += "</table>";
		}
		
		html += "</div>";
		
		_resp.getWriter().println(html);
	}

	/**
	 * Retrieves the name of an instructor from the instructorID.
	 * @param instructorID
	 * @return Instructor name in string form.
	 */
	private String getInstructorName(String instructorID) {
		
		List<User> instrs = Datastore.getUsers("UserID=='"+instructorID+"'");
		
		String name = "";

		if(instrs.size() != 0) {

			name = instrs.get(0).getFirstName() + " " + instrs.get(0).getLastName();
		}

		 return name;
	}
}

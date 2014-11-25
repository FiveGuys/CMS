package edu.uwm.cs361;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

/**
 * This Servlet class is used to edit CMS courses.
 * @author 5guys
 */
@SuppressWarnings("serial")
public class EditCourseServlet extends HttpServlet implements CallBack
{
	private final int ACCESS_LEVEL = ACCESS_ADMIN;
	
	private HttpServletRequest _req;
	
	private HttpServletResponse _resp;
	
	private List<String> _errors;
	
	private Form form;
	
	private Section section;
	
	private Boolean sectionFound = false;
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
		_req = req;
		
		_resp = resp;
		
		_errors = new ArrayList<String>();
		
		handleGet();
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}
	
	/** 
	 * Prints html content.
	 * @see edu.uwm.cs361.CallBack#printContent()
	 */
	@Override
	public void printContent()  throws IOException {

		if(sectionFound) {

			//TODO I wrote this a long time ago for create courses but it can be used here - DL
			_resp.getWriter().println( 

				form.Start("Computer Science "+section.getName().substring(8, 11), "Please fill all the texts in the fields.", "#") +

				form.TextField("Section: ", "Section", section.getSection(), "XXX", "") +

				form.TextField("Class #: ", "ClassNum", section.getClassNum(), "XXXXX", "") +

				form.TextField("Credits: ", "Units", section.getUnits(), "Units", "") +

				form.TextField("Room #: ", "Location", section.getLocation(), "EMS", "") +
				//TODO ability to change hours and days ( checkboxes)
				form.DateTime("Hours: ", "", form.selectTime("start-start", "start-end",
						LocalTime.parse(section.getStartTime(), DateTimeFormat.forPattern("h:m a")).toString("h"),
						LocalTime.parse(section.getStartTime(), DateTimeFormat.forPattern("h:m a")).toString("m"),"datetime"),
						form.selectTime("end-start", "end-end",
						LocalTime.parse(section.getEndTime(), DateTimeFormat.forPattern("h:m a")).toString("h"),
						LocalTime.parse(section.getEndTime(), DateTimeFormat.forPattern("h:m a")).toString("m"),"datetime")) +

				//form.DateTime("Dates: ", "", form.selectDate("date-start", "date-end", req)) +

				//form.WeekCheckBoxes() +

				"<input type='hidden' name='SectionID' value='"+_req.getParameter("SectionID")+"' />"+

				form.End()
			);
			
		} else {
			
			_resp.getWriter().println( "<div class='error'>No Section Found</div>" );
		}
	}
	
	@Override
	public void validate() {}
	
	/**
	 * Gets SectionID.
	 * @throws IOException
	 */
	private void handleGet() throws IOException {
		
		String SectionID = _req.getParameter("SectionID");
		
		if(SectionID == null) {
			
			_resp.sendRedirect("view-courses");
		
		} else {
			
			getSection(SectionID);
		}
	}
	
	/**
	 * Gets a Section if the sections list is populated. Otherwise, redirects to the Course view.
	 * @param SectionID
	 * @throws IOException
	 */
	private void getSection(String SectionID) throws IOException {
		
		List<Section> sections = Datastore.getSections("SectionID=='"+SectionID+"'");
		
		if(sections.size() != 0) {

			sectionFound = true;
			
			section = sections.get(0);
			
			form = new Form(_req, _resp, _errors);
			
			form.handleGet(section.getName(), 1, this, "editCourse", ACCESS_LEVEL);
		
		} else {
			
			_resp.sendRedirect("view-courses");
		}
	}
	
}
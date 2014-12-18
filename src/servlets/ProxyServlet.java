package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.cs361.CallBack;
import edu.uwm.cs361.Form;

/**
 * This servlet class handles the Scraping.
 * @author 5guys
 */
@SuppressWarnings("serial")
public class ProxyServlet extends HttpServlet implements CallBack {
	
private final int ACCESS_LEVEL = ACCESS_ADMIN;
	
	private HttpServletRequest _req;
	
	private HttpServletResponse _resp;
	
	private List<String> _errors;
	
	private Form _form;
	
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	
    	_req = req;
		
		_resp = resp;
		
		_errors = new ArrayList<String>();
		
		_form = new Form(_req, _resp, _errors);
		
		_form.handleGet("Refresh Courses", 0, this, "refreshCourses", ACCESS_LEVEL);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
    	doGet(req, resp);
    }
    
	@Override
	public void printContent() throws IOException {
		
		_resp.getWriter().println( 
				
				"<form action='/refresh-courses' method='post' class='standard-form'>" + 
					"<h1>Change Semester</h1>" +
					"<label>" +   
					  "<span>Semester: </span>" +
						"<select name='Semester'>" +
							"<option value='2149' "+ selected("2149") +">Fall 2014</option>" +
							"<option value='2151' "+ selected("2151") +">UWinteriM 2015</option>" +
							"<option value='2152' "+ selected("2152") +">Spring 2015</option>" +
						"</select>" +
					"</label>" +
				//	_form.CheckBox("Test Instructor", "", "TestInstructor", false) + 
					
				"<div class='submit'><input type='submit' style='margin-top: 50px;' name='submit' class='button' value='Submit' /></div>" +
				"</form>"
			);
	}

	@Override
	public void validate() { }
	
	/**
	 * @param name
	 * @return name parameter if is not null and if the _errors list is empty. Otherwise returns an empty string.
	 */
	private String getParam(String name) {
		
		return (_req.getParameter(name) != null && _errors.size() != 0 ? 
				_req.getParameter(name) : "");
	}
	
	/**
	 * @param index
	 * @return string "selected" if equal to index parameter. Otherwise returns an empty string.
	 */
	private String selected(String index) {
		
		return (getParam("Access").equals(index) ? "selected" : "");
	}
}
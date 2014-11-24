package edu.uwm.cs361;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("serial")
public class AssignProfServlet extends HttpServlet implements CallBack
{
	private int ACCESS_LEVEL=ACCESS_LIMITED;
	
	private HttpServletRequest _req;
	
	private String _courseID;
	
	private String _sectionID;
	
	private String _holdID="";
	
	private HttpServletResponse _resp;
	
	private List<String> _errors;
	
	private Form _form;
	
	private List<Course> _courses;
	
	private List<Section> _sections;
	
	private int index=0;
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
		_req = req;
		
		_resp = resp;
		
		_courses = Datastore.getCourses(null);
		
		_errors = new ArrayList<String>();
		
		_courseID = getCourseID();
		
		_sectionID = "";
		
		_sections = Datastore.getSections(null);
		
		_form = new Form(_req, _resp, new ArrayList<String>());
		
		save();
		
		_form.handleGet("Assign Proffesor", 1, this, "", ACCESS_LEVEL);
		
		
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		doGet(req, resp);
	}
	
	@Override
	public void printContent()  throws IOException {
		_resp.getWriter().println(
		"<form action='assign-prof' method='post' class='standard-form'>"+
		"<label>"+
			"<span class='assign'>Course: </span>");
			printCourse();
			_resp.getWriter().println(
		"</label>");
			printSection();
			_resp.getWriter().println(
		"</table>"+
		"<div class='submit'>"+ 
			"<input type='submit' name='submit' class='button' value='Save' />"+
		"</div>"+
	"</form>"+
	"</div>");
	}
	
	@Override
	public void validate(){	
		for(int i=0;i<=index;++i){
			if(!isValid(_req.getParameter("profName"+i))){
				_errors.add("Name "+(i+1)+" is invalid");
				System.out.println("Name "+(i+1)+" is invalid");
			}
		}
	}
	
	private void printCourse() throws IOException{
		String html = "<form action='course' method='post' class='standard-form'>"
				+"<select name='course'>";
		
		Datastore ds = new Datastore(_req, _resp, _errors);
		if (Integer.parseInt(ds.getUser().getAccess())==ACCESS_ADMIN)
		{
			
		for(Course  course : _courses) {
			if(course.getID().equals(_courseID)){
				html += "<option selected='selected' value='"+course.getID()+"'>"+course.getName()+"</option>";
				_sectionID=course.getID();
			}
			else{
				html += "<option value='"+course.getID()+"'>"+course.getName()+"</option>";
			}
		}
		}
		else
		{
			boolean firstTime= true;
			boolean containsProf = false;
			for(Course  course : _courses) {
				for(Section section: _sections){
					
					if(section.getClassType().equals("LEC")&& section.getCourseID().equals(course.getID())&&  section.getInstructor().equals(ds.getUser().getLastName()+", "+ds.getUser().getFirstName())){
						containsProf = true;
					}		
				}
			if(course.getID().equals(_courseID)&& containsProf ){
				html += "<option selected='selected' value='"+course.getID()+"'>"+course.getName()+"</option>";	
				firstTime=false;
				_sectionID=course.getID();
			}
			else if(containsProf){
				html += "<option value='"+course.getID()+"'>"+course.getName()+"</option>";
				if(firstTime)
				{
					_sectionID=course.getID();
					firstTime=false;
				}
			}
			containsProf=false;
			}
		}
		html += "</select>"
				+"<div class='submit'><input type='submit' name='save' class='button' value='Submit' /></div>"
			+"</form>";
		_resp.getWriter().println(html);
	}
	
	private void printSection() throws IOException{
		Datastore ds = new Datastore(_req, _resp, _errors);
		String html ="<form action='assign-prof' method='post' class='standard-form'>"+
				"<table class='assign-table'>"+
			"<tr>"+
				"<td><strong> Class Type</strong></td>"+
				"<td><strong> Section #</strong></td>"+
				"<td><strong> Current Instructor</strong></td>"+         
			"</tr>";
		index =0;
		
		String name="profName";
		 for(Section section: _sections){
			 name="profName"+index;
			 if(section.getCourseID().equals(_sectionID) && Integer.parseInt(ds.getUser().getAccess())==ACCESS_ADMIN){
				 html += "<tr>"+
							"<td><span>"+section.getClassType() +"</span></td>"+
							"<td><span>"+section.getSection()+"</span></td>"+
							"<td><input value='"+section.getInstructor()+"' type='textbox' name='"+name+"'></td>"+         
						"</tr>";
				
				index +=1;
			 }
			 
			 else if(section.getCourseID().equals(_sectionID)){
				 if(section.getClassType().equals("LEC")){
					 html += "<tr>"+
							"<td><span>"+section.getClassType() +"</span></td>"+
							"<td><span>"+section.getSection()+"</span></td>"+
							"<td><input value='"+section.getInstructor()+"' type='textbox' name='"+name+"' readonly='readonly'></td>"+         
						"</tr>";
					 index+=1;
				 }
				 else{
					 html += "<tr>"+
								"<td><span>"+section.getClassType() +"</span></td>"+
								"<td><span>"+section.getSection()+"</span></td>"+
								"<td><input value='"+section.getInstructor()+"' type='textbox' name='"+name+"'></td>"+         
							"</tr>";
					index+=1;
				 }
			 }
			 
		 }
		 html+="</form>";
		 _resp.getWriter().println(html);
		 System.out.println(_sectionID);
		 _holdID=_sectionID;
	}
	
	private void save(){
		if (_req.getParameter("submit")!= null){
			validate();
			if(_errors.size() == 0){
				Datastore ds = new Datastore(_req, _resp, _errors);
				System.out.println(_holdID);
				ds.updateProf(_holdID);
			}
		}
	}
	
	
	private boolean isValid(String input){
		if(input!=null && !input.isEmpty()){
			Pattern pattern = Pattern.compile("[a-zA-Z]+[,]+[ ]+[a-zA-z]+([ '-][a-zA-Z]+)*");
			Matcher matcher = pattern.matcher(input);
			if(matcher.matches())
				return true;
			else
				return false;
		}
		return true;
	}
	
	public String getCourseID() {

		return (_req.getParameter("course") != null ? 
				_req.getParameter("course") : _courses.get(0).getID());
	}
	
}
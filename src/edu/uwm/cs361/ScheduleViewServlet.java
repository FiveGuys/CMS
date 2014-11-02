package edu.uwm.cs361;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class ScheduleViewServlet extends HttpServlet{
	
	private String[][] table = new String[9][5];
	//Yes, I wrote out this array because I am a fucking scrub
	private String[] times = {"8:00AM","9:00AM","10:00AM","11:00AM","12:00PM","1:00PM","2:00PM","3:00PM","4:00PM","5:00PM","6:00PM"};
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		 for(String[] row : table)
			 Arrays.fill(row, "");
		 
		 displayForm(req, resp);
	}
	
	 //TODO doPost should take the class, instructor, or ta, from the select, and input elements from the datastore into the array table
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		 
	}
	
	public void displayForm(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		
		HtmlOutputHelper.printHeader(resp, "Schedule Viewer", 2);
		
		createContent(req, resp);
		
		HtmlOutputHelper.printFooter(resp);
	}
	
	private void createContent(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		resp.setContentType("text/html");
		
		resp.getWriter().println(
				"<select class='classSelect'>"
				+"<option>COMPSCI 201</option><option>COMPSCI 251</option><option>COMPSCI 351</option>"
				+"</select>"
				+ createHTMLTable(req, resp)
				);
	}
	
	private String createHTMLTable(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		
		String htmlTable = "<table id='schedule'>"
		+"<tr>"
			+"<th>Time</th>"
			+"<th>Monday</th>"
			+"<th>Tuesday</th>"
			+"<th>Wednesday</th>"
			+"<th>Thursday</th>"
			+"<th>Friday</th>"
		+"</tr>";
		
		for(int i = 0; i < table.length; ++i){
			
			htmlTable += "<tr>"+"<td>"+times[i]+"<td>";
			
			for(int j = 0; j < table[i].length; ++j){
				htmlTable += "<td>" + table[i][j] + "</td>";
			}
			
			htmlTable += "</tr>";
		}
		
		htmlTable += "</table>";
		
		return htmlTable;
	}
	
	//Creates the HTML formating for the table element
	private void classBuilder(){
		
	}
	
	//BUILT FOR TESTING PORPOISES
	private void createDummyContent(){
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Entity Class = new Entity("Class");
	}
		
}

package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import jdo.User;
import edu.uwm.cs361.CallBack;
import edu.uwm.cs361.Datastore;
import edu.uwm.cs361.HtmlOutputHelper;

@SuppressWarnings("serial")
public class OfficeHourViewServlet extends HttpServlet implements CallBack{

	private HttpServletRequest _req;
	
	private HttpServletResponse _resp;
	
	private String[][] _table = new String[11][5];
	
	private String[] _times = {"8:00 AM","9:00 AM","10:00 AM","11:00 AM","12:00 PM","1:00 PM","2:00 PM","3:00 PM","4:00 PM","5:00 PM","6:00 PM"};
	
	private String[] _dates = {"Mon","Tue","Wed","Thur","Fri"};
	
	private List<User> userList;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		_req = req;
		
		_resp = resp;
		
		System.out.println(_req.getParameter("firstname"));
		
		userList = Datastore.getUsers("FirstName=='"+_req.getParameter("firstname")+"' && LastName=='"+_req.getParameter("lastname")+"'");
		
		printContent();
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		doGet(req, resp);
	}
	
	@Override
	public void printContent() throws IOException{
		HtmlOutputHelper.printHeader(_req, _resp, "Office Hours", 2);
		createSchedule();
		HtmlOutputHelper.printFooter(_resp);
	}
	
	private void createSchedule() throws IOException{
		
		initSchedule();
		
		parseOfficeHours();
		
		String htmlTable = 
		"<table id='schedule'>"
			+"<tr>"
				+"<th>Time</th>"
				+"<th>Monday</th>"
				+"<th>Tuesday</th>"
				+"<th>Wednesday</th>"
				+"<th>Thursday</th>"
				+"<th>Friday</th>"
			+"</tr>";
		
		for(int i = 0; i < _table.length; ++i) {
			
			htmlTable += "<tr><td>"+_times[i]+"</td>";
			
			for(int j = 0; j < _table[i].length; ++j) {
				
				htmlTable += _table[i][j];
			}
			
			htmlTable += "</tr>";
		}
		
		htmlTable += "</table>";
		
		_resp.getWriter().println(htmlTable);
	}

	private void initSchedule() {
	
		for(int i = 0; i < _table.length; ++i) {
		
			for(int j = 0; j< _table[i].length; ++j) {
			
				_table[i][j] = "<td></td>";
			}
		}
	}
	
	private void parseOfficeHours(){
		
		int rowspan = 1;
		
		User user = userList.get(0);
				
		for(int i = 1; i < 3; ++i){
			String[] hours = user.getOfficeHour(i).split(";");
			
			for(String s: hours)
			{
				System.out.println(s);
			}
			if(!hours[0].equals("Wed") && !hours[1].equals("0") && !hours[2].equals("00") && !hours[3].equals("0") && !hours[4].equals("00")){
				
				LocalTime start =  LocalTime.parse((hours[1]+":" +hours[2]), DateTimeFormat.forPattern("H:m"));
				LocalTime end  =  LocalTime.parse((hours[3]+":" +hours[4]), DateTimeFormat.forPattern("H:m"));
				
				rowspan += end.getValue(0) - start.getValue(0);
				
				int startIndex = Arrays.asList(_times).indexOf(start.toString("h:mm a"));
				
				int dayIndex = Arrays.asList(_dates).indexOf(hours[0]);
				
				_table[startIndex][dayIndex] = "<td class='course' rowspan='"+rowspan+"'><b>Office Hours</b><br>"
												+start.toString("h:mm a")+"-"+end.toString("h:mm a")+"<br>"
												+user.getLocation()+"</td><br>";
				
				if(rowspan > 2){
					for(int j = 0; j < rowspan - 1 ; ++j){
						_table[startIndex+j+1][dayIndex] = "";
					}
				}
			}
			
			
		}
		
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		
	}
	
	

}

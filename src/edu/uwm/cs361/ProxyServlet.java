package edu.uwm.cs361;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/* http://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/ */

@SuppressWarnings("serial")
public class ProxyServlet extends HttpServlet {
	
	private HttpServletRequest _req;
	
	private HttpServletResponse _resp;
	
	private String url; 
	
	private String urlParameters;
	
	private String semester;
	
	private HttpURLConnection con;
    
	private String response;
	
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	
    	/*
    	_req = req; 
    	
    	_resp = resp;
    	
    	semester = (req.getParameter("semester") != null ? req.getParameter("semester") : "2149");
    
		getScheduleFromUWM();
    	*/
    	//getEmailFromUWM("Rock, Jayson");
    	 
    }
    
 	private void getScheduleFromUWM() throws IOException {

 		url = "http://www4.uwm.edu/schedule/pdf/pf_dsp_soc_search_results.cfm?strm="+semester;
 		
 		initConnection(url, "POST");
 		
 		sendRequest();
 		
 		getResponse();
 		
 		parseSchedule();
 	}

 	private String getEmailFromUWM(String name) throws IOException {
		
 		url = "http://www4.uwm.edu/search.cfm?s=people&q="+ name.replace(", ", "%2C+");
 		
 		initConnection(url, "GET");
 		
 		getResponse();
 		
 		return parseEmail();
	}
 	
	private void initConnection(String url, String action) throws IOException {

		con = (HttpURLConnection) new URL(url).openConnection();

		con.setRequestMethod(action);

		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		
		if(action == "POST") {
			
			urlParameters = "mon=0&tue=0&wed=0&thu=0&fri=0&sat=0&sun=0&gergroup=100&checkurl=N&subject=COMPSCI&category=&lastname=&firstname=&EXACTWORDPHRASE=&COURSETYPE=ALL&timerangefrom=&timerangeto=&datebeginning=&strm="+semester+
					"&school=ALL&school_descrformal=&results_title=&term_descr="+getTerm(0)+"&term_status=L&term_season="+getTerm(1)+"&datasource=cf_web_soc&subjdtlhide=false&submit_couns=Printer-Friendly+Version";
		}
		
		System.out.println("\nSending "+action+" request to URL : " + url);
	}
	
	private void sendRequest() throws IOException {
		
		con.setDoOutput(true);
		
 		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
 		
 		wr.writeBytes(urlParameters);
 		
 		wr.flush();
 		
 		wr.close();
	}

	private void getResponse() throws IOException {
		
 		System.out.println("Response Code : " + con.getResponseCode());
  
 		BufferedReader in = new BufferedReader(
 		        new InputStreamReader(con.getInputStream()));
 		
 		String inputLine;
 		
 		StringBuffer temp = new StringBuffer();
  
 		while ((inputLine = in.readLine()) != null) {
 		
 			temp.append(inputLine);
 		}
 		
 		in.close();
 		
		response = temp.toString();
		
		_resp.getWriter().println(response);
	}

	private String parseEmail() {
		
		Document doc = Jsoup.parse(response.toString());
 		
 		Elements email = doc.select("td.email");
 		
		return email.text();
	}

	private void parseSchedule() {
		
		Document doc = Jsoup.parse(response.toString());
 		
 		Elements CourseNames = doc.select("span.subhead");
 		
 		Datastore ds = new Datastore(_req, _resp, new ArrayList<String>());
 		
 		ds.deleteCourses();
 		
 		int courseID = 1;
 		int sectionID = 1;
 		
 		for(Element e :CourseNames) {
 			
 			String[] courseData = new String[11];
 			
 			String courseName = getName(e.text());
 			
 			System.out.println(courseName);
 			
 			ds.addCourse(courseID+"", courseName);
 			
			Elements tr = e.parent().parent().parent().getElementsByClass("body");

			for(Element t : tr) {
				
				if(t.tagName() == "tr" && t.hasClass("copy") && !t.attr("bgcolor").equals("#666666") && !t.hasClass("bold")) {
					
					courseData[0] = sectionID + "";
					
					courseData[1] = courseName;
					
					for(int i = 2; i <= 9; i++) {
						
						System.out.println(i + " " + t.child(i).html());
						
						courseData[i] = (!t.child(i).html().equals("&nbsp;") ? t.child(i).html() : "");
					}
					
					courseData[10] = courseID + "";
					
					ds.addSection(courseData);
					
					sectionID++;
				}
			}
			
 			courseID++;
 		}
	}
	
	private String getName(String inputLine) {
		
		String temp = inputLine;
		
		if(inputLine.indexOf('-') != 7) {
			
			StringBuffer text = new StringBuffer(temp);

			text.replace(text.indexOf("("), text.indexOf(")") + 1, "");
			text.replace(7,8, "-");
			temp = text.toString();
		}
			
		return temp;
	}
	
	public static String md5(String message) { 

		String hash = null; 

		try { 
			
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			
			byte[] bytes = md.digest(message.getBytes("UTF-8")); 
			
			StringBuilder sb = new StringBuilder(2 * bytes.length); 
			
			for(byte b : bytes){ 
				
				sb.append(String.format("%02x", b&0xff)); 
			} 
			
			hash = sb.toString(); 
			
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) { 
			
		}
		
		return hash; 
	}
	
	private String getTerm(int type) {
		
		String term = "";
		
		if(semester.equals("2149")) {
			term = (type == 0 ? "Fall+2014" : "Fall+");
		}
		
		if(semester.equals("2151")) {
			term = (type == 0 ? "UWinteriM+2015" : "UWinteriM+");
		}
		
		if(semester.equals("2152")) {
			term = (type == 0 ? "Spring+2015" : "Spring+");
		}
		
		return term;
	}
}
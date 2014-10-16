package edu.uwm.cs361;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

/* http://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/ */

@SuppressWarnings("serial")
public class ProxyServlet extends HttpServlet {
	
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse rsp)
            						throws ServletException, IOException {
       
    	try {
    		sendPost(req,rsp);
    		printHeader(rsp, "Search for User", 4);
    		loadFile(rsp, "user-search.html");
    		printFooter(rsp);
    		
        } catch (Exception e) {
            e.printStackTrace(rsp.getWriter());

        }
    }
    
 // HTTP POST Request
 	private void sendPost(HttpServletRequest req, HttpServletResponse rsp) throws Exception {

 		String url = "http://www4.uwm.edu/schedule/pdf/pf_dsp_soc_search_results.cfm?strm=2149";
 		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
  
 		// Add Request Header
 		con.setRequestMethod("POST");
 		con.setRequestProperty("User-Agent", "Mozilla/5.0");
 		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
  
 		String urlParameters = "mon=0&tue=0&wed=0&thu=0&fri=0&sat=0&sun=0&gergroup=100&checkurl=N&subject=COMPSCI&category=&lastname=&firstname=&EXACTWORDPHRASE=&COURSETYPE=ALL&timerangefrom=&timerangeto=&datebeginning=&strm=2149&school=ALL&school_descrformal=&results_title=&term_descr=Fall+2014&term_status=L&term_season=Fall+&datasource=cf_web_soc&subjdtlhide=false&submit_couns=Printer-Friendly+Version";
  
 		// Send post request
 		con.setDoOutput(true);
 		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
 		wr.writeBytes(urlParameters);
 		wr.flush();
 		wr.close();
  
 		int responseCode = con.getResponseCode();
 		System.out.println("\nSending 'POST' request to URL : " + url);
 		System.out.println("Post parameters : " + urlParameters);
 		System.out.println("Response Code : " + responseCode);
  
 		BufferedReader in = new BufferedReader(
 		        new InputStreamReader(con.getInputStream()));
 		
 		String inputLine;
 		StringBuffer response = new StringBuffer();
  
 		while ((inputLine = in.readLine()) != null) {
 			
 			if(inputLine.contains("<span class=\"subhead\">")) {
 				System.out.println(inputLine.substring(inputLine.indexOf('>') + 1, inputLine.indexOf("</span>")));
 			}
 			
 			response.append(inputLine);
 		}
 		
 		in.close();
  
 		//print result
 		rsp.getWriter().println(response.toString());
  
 	}
 	
 	private void loadFile(HttpServletResponse rsp, String fileName) throws Exception {
 		
 		Document doc = Jsoup.parse(new File(fileName), "UTF-8");
 		
 		rsp.getWriter().println(doc.getElementById("content").html());
 		
 	}
 	
 	private void printHeader(HttpServletResponse rsp, String title, int index ) throws IOException {
 		String html = 
 		"<!DOCTYPE html>" 
		+"<html>" 
		+"<head>" 
			+"<meta charset='utf-8'>" 
			+"<link rel='shortcut icon' href='images/favicon.ico'>" 
		    +"<link rel='stylesheet' type='text/css' href='css/main.css'>" 
		    +"<link rel='stylesheet' type='text/css' href='css/navbar.css'>" 
			+"<link rel='stylesheet' type='text/css' href='css/form.css'>" 
			+"<link rel='stylesheet' type='text/css' href='css/user.css'>" 
		    +"<meta name='viewport' content='width=device-width'>" 
		    +"<title>Course Management Site</title>" 
		+"</head>" 
		+"<body>" 
 		+"<div id='main-container'>" 
		+"<div id='header'>  " 
		    +"<div class='settings'><a href='add-user.html'>Add New User</a><a href='index.html'>Logout</a></div>" 
		      +"<div class='uwmlogo'>" 
		        +"<a href='https://www4.uwm.edu/' target='_new'>" 
		            +"<img src='images/logo_uwm.png'><img>" 
		                +"</a>" 
		            +"</div>" 
		        +"<div class='header-title'>"+title+"</div>" 
		        +"<div id='navbar'>" 
		        +"<ul>" 
		        +"<li class='active has-sub'><a "+( index == 1 ? "class='selected'" : "")+" href='#'>Courses</a>" 
		           +"<ul>" 
		            +"<li><a href='assign-prof.html'>Assign Professor</a></li>" 
		              +"<li><a href='assign-ta.html'>Assign TA</a></li>" 
		              +"<li><a href='create-course.html'>Create a Course</a></li>" 
		              +"<li><a href='create-lab-dis.html'>Create Lab/Discussion</a></li>	" 	
		              +"<li><a href='courses.html'>View All Courses</a></li>" 
		              +"</ul>" 
		            +"</li>" 
		           +"<li class='active has-sub'><a "+( index == 2 ? "class='selected'" : "")+" href='views.html'>Schedule Views</a>" 
		           +"<ul style='display: none'>" 
		              +"<li class='has-sub'><a href='#'>View 1</a>" 
		                 +"<ul>" 
		                    +"<li><a href='#'>Sub View</a></li>" 
		                       +"<li class='last'><a href='#'>Sub View</a></li>" 
		                       +"</ul>" 
		                    +"</li>" 
		                 +"<li class='has-sub'><a href='#'>View 2</a>" 
		                 +"<ul>" 
		                    +"<li><a href='#'>Sub View</a></li>" 
		                       +"<li class='last'><a href='#'>Sub View</a></li>" 
		                       +"</ul>" 
		                    +"</li>" 
		                 +"</ul>" 
		              +"</li>" 
		           +"<li><a "+( index == 3 ? "class='selected'" : "")+" href='edit-info.html'>Edit Info</a></li>" 
		           +"<li><a "+( index == 4 ? "class='selected'" : "")+" href='user-search.html'>Search User</a></li>" 
		           +"</ul>" 
		        +"</div>" 
		      +"</div>"
		      + "<div id='content'>";
 		rsp.getWriter().println(html);
 	}
 	
 	private void printFooter(HttpServletResponse rsp) throws IOException {
 		String html = "</div>"
 				+ "</div>"
			 		+"</body>"
			 		+"</html>";
 		
 		rsp.getWriter().println(html);
 	}

}
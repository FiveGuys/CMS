package edu.uwm.cs361;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

public class HtmlOutputHelper 
{
	private final static String[] STYLE = {"", "course.css", "views.css", "edit-info.css", "user.css"};
	
 	public static void printHeader(HttpServletResponse rsp, String title, int index) throws IOException {
		
 		rsp.setContentType("text/html");
		
 		rsp.getWriter().println( 
 				
 		"<!DOCTYPE html>" 
		+"<html>" 
		+"<head>" 
			+"<meta charset='utf-8'>" 
			+"<link rel='shortcut icon' href='images/favicon.ico'>" 
		    +"<link rel='stylesheet' type='text/css' href='css/main.css'>" 
		    +"<link rel='stylesheet' type='text/css' href='css/navbar.css'>" 
			+"<link rel='stylesheet' type='text/css' href='css/form.css'>" 
			+"<link rel='stylesheet' type='text/css' href='css/"+STYLE[index]+"'>" 
		    +"<meta name='viewport' content='width=device-width'>" 
		    +"<title>Course Management Site</title>" 
		+"</head>" 
		+"<body>" 
 		+"<div id='main-container'>" 
		+"<div id='header'>  " 
		    +"<div class='settings'>"
//		    + "<form><select>"
//			    + "<option value='2149'>Fall 2014</option>"
//			    + "<option value='2151'>WinterIM 2015</option>"
//			    + "<option value='2152'>Spring 2015</option>"
//		    + "</select></form>"
		    + "<a href='add-user'>Add New User</a><a href='index.html'>Logout</a></div>" 
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
		           // +"<li style='display: none><a href='assign-prof'>Assign Professor</a></li>" 
		            //  +"<li style='display: none><a href='assign-ta'>Assign TA</a></li>" 
		              //+"<li><a href='create-course.html'>Create a Course</a></li>" 
		              //+"<li><a href='create-lab-dis.html'>Create Lab/Discussion</a></li>	" 	
		              +"<li><a href='view-courses'>View All Courses</a></li>" 
		              +"</ul>" 
		            +"</li>" 
		           +"<li class='active has-sub'><a "+( index == 2 ? "class='selected'" : "")+" href='schedule-view'>Schedule Views</a>" 
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
		           +"<li><a "+( index == 3 ? "class='selected'" : "")+" href='edit-info'>Edit Info</a></li>" 
		           //+"<li style='display: none><a "+( index == 4 ? "class='selected'" : "")+" href='user-search'>Search User</a></li>" 
		           +"</ul>" 
		        +"</div>" 
		      +"</div>"
		      + "<div id='content'>"
 		);
 	}
 	
 	public static void printFooter(HttpServletResponse rsp) throws IOException {
 		
 		rsp.getWriter().println("</div></div></body></html>");
 	}
 	
 	public static void printErrors(HttpServletResponse rsp, List<String> errors) throws IOException{
 		
 		PrintWriter out = rsp.getWriter();
 		
 		if (errors.size() > 0) {
 			
			out.println("<ul class='errors'>");

			for (String error : errors) {
				out.println("  <li>" + error + "</li>");
			}

			out.println("</ul>");
		}
 	}
}
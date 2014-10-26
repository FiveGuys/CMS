package edu.uwm.cs361;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

public class HtmlOutputHelper {
 	public static void printHeader(HttpServletResponse rsp, String title, int index) throws IOException {
		rsp.setContentType("text/html");
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
 	
 	public static void printFooter(HttpServletResponse rsp) throws IOException {
 		String html = "</div>"
 				+ "</div>"
			 		+"</body>"
			 		+"</html>";
 		
 		rsp.getWriter().println(html);
 	}
}

package edu.uwm.cs361;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdo.User;

/**
 * HTML output helper
 * @author 5guys
 */
public class HtmlOutputHelper 
{
	private final static String[] STYLE = {"", "course.css", "views.css", "edit-info.css", "user.css"};
	
 	/**
 	 * Prints header.
 	 * @param req
 	 * @param rsp
 	 * @param title
 	 * @param index
 	 * @throws IOException
 	 */
 	public static void printHeader(HttpServletRequest req, HttpServletResponse rsp, String title, int index) throws IOException {
		
 		rsp.setContentType("text/html");
		
 		Datastore ds = new Datastore(req, rsp, new ArrayList<String>());
 		
 		User user = ds.getUser();
 		
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
 			+"<div class='flyout-menu'>"
		    + "<div class='settings-content'>" 
		    + "<ul>"
		    + "<li><a href='add-user'>Add New User</a><li>" // TODO Admin only - DL
		    + "<li><a href='refresh-courses'>Refresh Courses</a><hr /></li>" //TODO Admin Only - DL
		    + "<li><a href='index.html'>Logout</a></li>"
		    + "</ul>"
		    + "</div>"
		    +"<div class='settings'>"
		    	+ (user.getImage() == null ? "<img src='images/blank-picture.png'/>" 
		    								: "<img src='/serve?blob-key="+user.getImage()+"' />")	
			    +"<span>"+user.getFirstName() + " " + user.getLastName() + "</span>"
		    + "</div>" 
		    + "</div>"
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
		            +"<li><a href='assign-prof'>Assign Professor</a></li>" 
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
		           +"<li><a "+( index == 4 ? "class='selected'" : "")+" href='user-search'>Search User</a></li>" 
		           +"</ul>" 
		        +"</div>" 
		      +"</div>"
		      + "<div id='content'>"
 		);
 	}
 	
 	/**
 	 * Prints footer.
 	 * @param rsp
 	 * @throws IOException
 	 */
 	public static void printFooter(HttpServletResponse rsp) throws IOException {
 		
 		rsp.getWriter().println("</div></div></body></html>");
 	}
}
package edu.uwm.cs361;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet
{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String message = "";
		
		if(Form.getUserFromCookie(req) != null) {
			
			Form.deleteCookie(resp);
			
			message = "You have been logged out";	
		}
			
		printContent(resp, message);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String user = req.getParameter("user");
		
		String pass = req.getParameter("pass");
		
		Datastore ds = new Datastore(req, resp, new ArrayList<String>());
		
		if(ds.validateLogin(user, pass)) {
			
			Cookie c = new Cookie("user", user);
			
			c.setMaxAge(60 * 60 * 24);
			
			resp.addCookie(c);
			
			resp.sendRedirect("edit-info");
			
		} else  {
			
			printContent(resp, "User:admin.pass Pass:pass");
		}
	}

	private void printContent(HttpServletResponse resp, String message) throws IOException {
		
		resp.getWriter().println(
			
			"<!DOCTYPE html>" +
			"<html>" +
			"<head>" +
			    "<link rel='shortcut icon' href='images/favicon.ico'>" +
			    "<link rel='stylesheet' type='text/css' href='css/login.css'>" +
			    "<meta name='viewport' content='width=device-width'>" +
			    "<title>Course Management Site</title>" +
			"</head>" +
			"<body>" +
			"<div class='page'>" +
			  "<div class='outer'>" +
			    "<div class='middle'>" +
			      "<div class='inner'>" +
			        "<div class='content'>" +
			          "<div class='login_form'>" +
			            "<div class='uwmlogo'>" + 
			            	"<a href='https://www4.uwm.edu/' target='_new'>" +
			                	"<img src='images/logo_uwm.png'><img>" +
			                "</a>" +
			            "</div>" +
			            "<div class='sitelogo'></div>" +
			            "<div id='errors'>"+message+"</div>" +
			            "<form action='login' method='post'>" +
			                "<label for='user'>Username</label>" +
			                "<input class='input-text' type='text' id='user' name='user' tabindex='1' required/>" +
			                "<label for='pass'>Password</label>" +
			                "<input class='input-text' type='password' id='pass' name='pass' tabindex='2' required/>" +
			                "<input class='input-btn' name='submit' type='submit' value='LOG IN' tabindex='3' />" +
			            "</form>" +
			            "<div class='footer'>" +
							"<a href='forgot' target='_new'>Forgot Password</a>" +
						"</div>" +
			          "</div>" +
			        "</div>" +
			      "</div>" +
			    "</div>" +
			  "</div>" +
			"</div>" +
			"</body>" +
			"</html>"
		);
	}
}

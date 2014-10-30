package edu.uwm.cs361;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet
{
	private final String USER = "admin";
	private final String PASS = "pass";

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		if(Form.getUserFromCookie(req) != null) {
			
			Form.deleteCookie(resp);
			
		} else {
			
			printContent(resp, "");
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String user = req.getParameter("user");
		String pass = req.getParameter("pass");
				
		if(USER.equals(user) && PASS.equals(pass)) {
			
			Cookie c = new Cookie("user", user);
			
			c.setMaxAge(60 * 60 * 24);
			
			resp.addCookie(c);
			
			resp.sendRedirect("create-course");
			
		} else  {
			
			printContent(resp, "<style> #errors{ display: block !important; } </style>");
		}
	}

	private void printContent(HttpServletResponse resp, String message) {
		
		try {
			
			File file = new File("WEB-INF/login.html");
			FileReader fReader = new FileReader(file);
			BufferedReader bReader = new BufferedReader(fReader);
			
			StringBuffer sBuffer = new StringBuffer();
			
			sBuffer.append(message);
			System.out.println(message);
			String html;
			
			while ((html = bReader.readLine()) != null) {
				
				sBuffer.append(html);
			}
			
			fReader.close();
			
			resp.getWriter().println(sBuffer.toString());
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
}

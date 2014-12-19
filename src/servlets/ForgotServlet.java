package servlets;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uwm.cs361.Datastore;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import jdo.User;

/**
 * This Servlet class is used to recover forgotten credentials.
 * @author 5guys
 */
@SuppressWarnings("serial")
public class ForgotServlet extends HttpServlet
{	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
		Datastore.addAdmin();
		
		sendEmail(req, resp);
	}
	

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		printContent(resp);
	}
	
	/**
	 * Prints Datastore content
	 */	
	private void printContent(HttpServletResponse resp) throws IOException {
		
		resp.setContentType("text/html");
		
		resp.getWriter().println("Enter Username: <input type='text' name='email' value='' />"
				 +"<input type='button' name='submit' value='submit' />");
		
	}
	
	/**
	 * Sends email with credentials
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void sendEmail(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		List<User> users = Datastore.getUsers("Email=='"+req.getParameter("Email")+"'");
		
		if(users.size() > 0) {
			
			User user = users.get(0);
			
			Properties props = new Properties();
			
			Session session = Session.getDefaultInstance(props, null);
	
			String msgBody = "Password: "+user.getPassword();
	
			try {
	
				InternetAddress from = new InternetAddress("fiveguys.uwm@gmail.com", "Password Recovery");
				
				InternetAddress to = new InternetAddress(user.getEmail(), user.getFirstName()+" "+user.getLastName());
				
				Message msg = new MimeMessage(session);
				
				msg.setFrom(from);
				
				msg.addRecipient(Message.RecipientType.TO, to);
				
				msg.setSubject("Password Recovery");
				
				msg.setText(msgBody);
				
				Transport.send(msg);
	
			} catch (MessagingException | UnsupportedEncodingException e) {
				
				e.printStackTrace();
			}
			
			resp.getWriter().println("Email Sent");
			
		} else {
			
			resp.getWriter().println("Email Not Found");
		}
	}
}
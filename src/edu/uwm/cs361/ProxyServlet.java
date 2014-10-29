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

/* http://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/ */

@SuppressWarnings("serial")
public class ProxyServlet extends HttpServlet {
	
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse rsp)
            						throws ServletException, IOException {
       
    	try {
    		
    		getScheduleFromUWM(req,rsp);
    		
        } catch (Exception e) {
        	
            e.printStackTrace(rsp.getWriter());

        }
    }
    
 	private void getScheduleFromUWM(HttpServletRequest req, HttpServletResponse rsp) throws Exception {

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
  
 		rsp.getWriter().println(response.toString());
  
 	}
}
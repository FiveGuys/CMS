package edu.uwm.cs361;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/* http://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/ */

@SuppressWarnings("serial")
public class ProxyServlet extends HttpServlet {
	
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            						throws ServletException, IOException {
    	try {
    		
    		getScheduleFromUWM(req,resp);
    		
        } catch (Exception e) {
        	
            e.printStackTrace(resp.getWriter());

        }
    }
    
 	private void getScheduleFromUWM(HttpServletRequest req, HttpServletResponse resp) throws Exception {

 		String url = "http://www4.uwm.edu/schedule/pdf/pf_dsp_soc_search_results.cfm?strm=2149";
 		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
  
 		con.setRequestMethod("POST");
 		con.setRequestProperty("User-Agent", "Mozilla/5.0");
 		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
  
 		String urlParameters = "mon=0&tue=0&wed=0&thu=0&fri=0&sat=0&sun=0&gergroup=100&checkurl=N&subject=COMPSCI&category=&lastname=&firstname=&EXACTWORDPHRASE=&COURSETYPE=ALL&timerangefrom=&timerangeto=&datebeginning=&strm=2149&school=ALL&school_descrformal=&results_title=&term_descr=Fall+2014&term_status=L&term_season=Fall+&datasource=cf_web_soc&subjdtlhide=false&submit_couns=Printer-Friendly+Version";
  
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
 		
 			response.append(inputLine);
 		}
 		
 		Document doc = Jsoup.parse(response.toString());
 		
 		Elements ClassNames = doc.select("span.subhead");
 		
 		Datastore ds = new Datastore(req, resp, new ArrayList<String>());
 		
 		int classID = 1;
 		int courseID = 1;
 		
 		for(Element e :ClassNames) {
 			
 			String[] courseData = new String[11];
 			
 			String ClassName = getName(e.toString());
 			
 			ds.addClass(classID, ClassName);
 			System.out.println(ClassName);
 			
			Elements tr = e.parent().parent().parent().getElementsByClass("body");

			for(Element t : tr) {
				
				if(t.tagName() == "tr" && t.hasClass("copy") && !t.attr("bgcolor").equals("#666666") && !t.hasClass("bold")) {
					
					courseData[0] = courseID + "";
					
					courseData[1] = ClassName;
					
					for(int i = 2; i <= 9; i++) {
						
						System.out.println(i + " " + t.child(i).html());
						
						courseData[i] = (!t.child(i).html().equals("&nbsp;") ? t.child(i).html() : "");
					}
					
					courseData[10] = classID + "";
					
					ds.addCourse(courseData);
					
					courseID++;
				}
			}
			
			classID++;
 		}
 		
 		in.close();
  
 		resp.getWriter().println(response.toString());
 	}

	private String getName(String inputLine) {
		
		String temp = inputLine.substring(inputLine.indexOf('>') + 1, inputLine.indexOf("</span>"));
			/*
			if(inputLine.indexOf('-') != 32) {
				
				StringBuffer text = new StringBuffer(temp);

				text.replace(text.indexOf("("), text.indexOf(")") + 1, "");
				text.replace(7,8, "-");
				temp = text.toString();
			}*/
			
			return temp;
	}
}
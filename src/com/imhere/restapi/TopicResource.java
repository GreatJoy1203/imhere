package com.imhere.restapi;

import java.sql.Connection;
import java.sql.SQLException;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;


import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.imhere.persistence.Session;
import com.imhere.persistence.Topic;

@Path("/bbs/topics")
public class TopicResource {
	@Path("{tid}")
	@Produces({"application/xml"})
	@GET
	public String getResponse(@PathParam("tid") int tid)
	{
		Context initCtx;
		Connection con = null;
		try {
			initCtx = new InitialContext();
			DataSource datasource;
			datasource = (DataSource)initCtx.lookup("java:comp/env/jdbc/heredb"); 
			con = datasource.getConnection(); //should return instantly
			System.out.println("getconnection");
			Topic topic=Session.loadTopic(tid,con);
			if(topic!=null)
				 return topic.exportXML();
			}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			 catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				finally {
					if (con!=null) try {con.close();}catch (Exception ignore) {}
	          } 	
		Document doce=DocumentHelper.createDocument();
		Element roote =doce.addElement("error");
		roote.addAttribute("type","topic_id not found");
		return doce.asXML();
	}
	
	@Produces({"application/xml"})
	@Consumes({"application/xml"})
	@POST
	public String createTopic(String representation)
	{
		Context initCtx;
		Connection con = null;
		try {
			initCtx = new InitialContext();
			DataSource datasource;
			datasource = (DataSource)initCtx.lookup("java:comp/env/jdbc/heredb"); 
			con = datasource.getConnection(); //should return instantly
			Topic topic=new Topic();
			if(topic.importXML(representation))
			{
				int result=Session.saveTopic(topic, con);
				if(result!=-1)
				{
					Document doce=DocumentHelper.createDocument();
					Element roote =doce.addElement("result");
					roote.addAttribute("status","success");
					roote.setText(""+result);
					return doce.asXML();
				}
			}
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			finally {
				if (con!=null) try {con.close();}catch (Exception ignore) {}
          } 
		Document doce=DocumentHelper.createDocument();
		Element roote =doce.addElement("result");
		roote.addAttribute("status","fail");
		return doce.asXML();
	}
	
}

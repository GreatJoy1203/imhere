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

import com.imhere.persistence.Forum;
import com.imhere.persistence.Session;


@Path("/bbs/forums")
public class ForumResource {
	@Path("{fid}")
	@Produces({"application/xml"})
	@GET
	public String getResponse(@PathParam("fid") int fid)
	{
		Context initCtx;
		Connection con = null;
		try {
			initCtx = new InitialContext();
			DataSource datasource;
			datasource = (DataSource)initCtx.lookup("java:comp/env/jdbc/heredb"); 
			con = datasource.getConnection(); //should return instantly
			Forum forum=Session.loadForum(fid,con);
			if(forum!=null)
				 return forum.exportXML();
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
		roote.addAttribute("type","forum_id not found");
		return doce.asXML();
	}
	
	@Produces({"application/xml"})
	@Consumes({"application/xml"})
	@POST
	public String createForum(String representation)
	{
		Context initCtx;
		Connection con = null;
		try {
			initCtx = new InitialContext();
			DataSource datasource;
			datasource = (DataSource)initCtx.lookup("java:comp/env/jdbc/heredb"); 
			con = datasource.getConnection(); //should return instantly
			Forum forum=new Forum();
			if(forum.importXML(representation))
			{
				int result=Session.saveForum(forum, con);
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

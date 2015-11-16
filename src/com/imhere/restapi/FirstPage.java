package com.imhere.restapi;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.dom4j.*;
import java.sql.*;

@Path("/")
public class FirstPage {
	@Produces({MediaType.APPLICATION_XML})
	@GET
	public String getFirstPage()
	{
		System.out.println("im in");
		Context initCtx;
		Connection con = null;
		try {
			initCtx = new InitialContext();
			DataSource datasource;
			datasource = (DataSource)initCtx.lookup("java:comp/env/jdbc/heredb"); 
			  con = datasource.getConnection(); //should return instantly
			  
			  System.out.println("get connection");
			  Statement st = con.createStatement();
			   ResultSet rs = st.executeQuery("select * from UserChart where user_id=1");
			 //建立XML的document
			   if(rs.next())
			   {
					Document doc=DocumentHelper.createDocument();
					//添加相关节点及信息
					Element root =doc.addElement("UserInfo");
					Element user_id=root.addElement("user_id");
					user_id.setText(rs.getString(1));
					Element user_name=root.addElement("user_name");
					user_name.setText(rs.getString(2));
					Element password=root.addElement("password");
					password.setText(rs.getString(3));
					return doc.asXML();
			   }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		finally {
            if (con!=null) try {con.close();}catch (Exception ignore) {}
          }  
		 
		Document doc=DocumentHelper.createDocument();
		Element root=doc.addElement("root");
		root.setText("hello world!");
		return doc.asXML();
		 
	}
}

/**
 * 文件名：LocationResource.java
 * 描述：位置（location）资源文件
 * 创建日期：2013-01-15
 * 创建者：高继扬
 * 修改历史：who，when，why
 */


package com.imhere.restapi;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.dom4j.*;
import java.sql.*;

/**LocationResource
 * @描述：根路径/locations，定义位置节点资源API
 *@author 高继扬，2013-01-15
 *@修改历史：高继扬，2013-02-06，修改连接池，使用c3p0，为hibernate做准备
 */
@Path("/locations")
public class LocationResource {
	/**
	 * @描述 将输入位置节点的相关信息返回
	 * @param loc_path
	 * @author 高继扬，2013-01-15
	 * @修改历史：高继扬，2013-02-06，修改连接池，使用c3p0，为hibernate做准备
	 */
	@Path("{loc_path}")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String getLocationInfo(@PathParam("loc_path") String loc_path)
	{
		/*
		 关于连接池：tomcat7中自带的jdbc connection pool与c3p0获取connection的
		 方式不尽相同：
		 共同点：
		 initCtx = new InitialContext();
		DataSource datasource;
		datasource = (DataSource)initCtx.lookup("java:comp/env/jdbc/heredb");
		这三行代码是相同的，但是其中的DataSource不是一个，tomcat7使用的
		是org.apache.tomcat.jdbc.pool，而c3p0使用javax.sql.DataSource
		不同点：在获取connection上不同
		tomcat 7：
		Future<Connection> future = datasource.getConnectionAsync();
		 while (!future.isDone()) {
		      System.out.println("Connection is not yet available. Do some background work");
		      try {
		        Thread.sleep(100); //simulate work
		      }catch (InterruptedException x) {
		        Thread.currentThread().interrupt();
		      }
		      con=future.get()	      
	      c3p0:
	       con = datasource.getConnection();
	       
	       使用两个连接池的主要不同在于
	       1.context.xml中Resource节点的属性上，属性名不尽相同；
	       2.在程序中获取connection的方式不同。
	       
		 */
		Context initCtx;
		Connection con = null;
		try {
			initCtx = new InitialContext();
			DataSource datasource;
			datasource = (DataSource)initCtx.lookup("java:comp/env/jdbc/heredb"); 
			  con = datasource.getConnection(); //should return instantly

			  Statement st = con.createStatement();
			  
			 // String locpathnew=loc_path.replaceAll("-", "/");
			  StringBuilder locpathbl=new StringBuilder("\'"+loc_path+"\'");
			  String locpathnew=locpathbl.toString().replaceAll("-","/");
			  
			   ResultSet rs = st.executeQuery("select * from LocationNodeChart where path_string like"+locpathnew);
			 //建立XML的document
			   if(rs.next())
			   {
					Document doc=DocumentHelper.createDocument();
					//添加相关节点及信息
					
					Element root =doc.addElement("LocationInfo");
					Element loc_node_idsl=root.addElement("loc_node_id");
					loc_node_idsl.setText(rs.getString(1));
					Element path_string=root.addElement("path_string");
					path_string.setText(rs.getString(2));
					Element loc_name=root.addElement("loc_name");
					loc_name.setText(rs.getString(3));
					Element loc_address=root.addElement("loc_address");
					loc_address.setText(rs.getString(4));
					Element longitude=root.addElement("longitude");
					longitude.setText(rs.getString(5));
					Element latitude=root.addElement("latitude");
					latitude.setText(rs.getString(6));
					Element description=root.addElement("description");
					description.setText(rs.getString(7));
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
		
		Document doce=DocumentHelper.createDocument();
		Element roote =doce.addElement("error");
		roote.addAttribute("type", "loc_node_id not found");
		return doce.asXML();
	}
	
	/**
	 * @描述 将输入位置节点的字节点的相关信息返回
	 * @param loc_path
	 * @author 高继扬，2013-01-15
	 * @修改历史：高继扬，2013-02-06，修改连接池，使用c3p0，为hibernate做准备
	 */
	@Path("{loc_path}/childnodes")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String getChildNodes(@PathParam("loc_path") String loc_path)
	{
		Context initCtx;
		Connection con = null;
		try {
			initCtx = new InitialContext();
			DataSource datasource;
			datasource = (DataSource)initCtx.lookup("java:comp/env/jdbc/heredb"); 
			  con = datasource.getConnection(); //should return instantly
			  System.out.println("get connection");
			  Statement st = con.createStatement();
			  
			 // String locpathnew=loc_path.replaceAll("-", "/");
			  StringBuilder locpathbl=new StringBuilder("\'"+loc_path+"%\'");
			  String locpathnew=locpathbl.toString().replaceAll("-","/");
			  
			   ResultSet rs = st.executeQuery("select * from LocationNodeChart where path_string like"+locpathnew);
			 //建立XML的document
			   if(rs.next())
			   {
				   rs.last();
				   Integer size=rs.getRow();
				   Document doc=DocumentHelper.createDocument();
				   Element root =doc.addElement("LocationInfoList");
				   System.out.println(size);
				   root.addAttribute("ListSize",size.toString() );
				   
				   rs.first();
				   for(int i=0;i<size;i++)
				   {
						
						//添加相关节点及信息
						Element subroot =root.addElement("LocationInfo");
						Element loc_node_idsl=subroot.addElement("loc_node_id");
						loc_node_idsl.setText(rs.getString(1));
						Element path_string=subroot.addElement("path_string");
						path_string.setText(rs.getString(2));
						Element loc_name=subroot.addElement("loc_name");
						loc_name.setText(rs.getString(3));
						Element loc_address=subroot.addElement("loc_address");
						loc_address.setText(rs.getString(4));
						Element longitude=subroot.addElement("longitude");
						longitude.setText(rs.getString(5));
						Element latitude=subroot.addElement("latitude");
						latitude.setText(rs.getString(6));
						Element description=subroot.addElement("description");
						description.setText(rs.getString(7));
						rs.next();
						
				   }
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
		
		Document doce=DocumentHelper.createDocument();
		Element roote =doce.addElement("error");
		roote.addAttribute("type", "loc_node_id not found");
		return doce.asXML();
	}
	
	/**
	 * @描述 将输入位置节点的子场景相关信息返回
	 * @param loc_path
	 * @author 高继扬，2013-01-15
	 * @修改历史：高继扬，2013-02-06，修改连接池，使用c3p0，为hibernate做准备
	 */
	@Path("{loc_path}/childcontexts")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String getChildContexts(@PathParam("loc_path") String loc_path)
	{
		
		Context initCtx;
		Connection con = null;
		try {
		initCtx = new InitialContext();
		DataSource datasource;
		datasource = (DataSource)initCtx.lookup("java:comp/env/jdbc/heredb"); 
		  con = datasource.getConnection(); //should return instantly
			  
		  Statement st = con.createStatement();
		  
		 // String locpathnew=loc_path.replaceAll("-", "/");
		  StringBuilder locpathbl=new StringBuilder("\'"+loc_path+"\'");
		  String locpathnew=locpathbl.toString().replaceAll("-","/");
		  System.out.println(locpathnew);
		  ResultSet rs = st.executeQuery("select loc_node_id from LocationNodeChart where path_string like"+locpathnew);
		  if(rs.next())
		  {
			 // System.out.println("select * from ContextChart where loc_node_id ="+rs.getString(1));
			  StringBuilder locnodeid=new StringBuilder("\'"+rs.getString(1)+"\'");
			  rs = st.executeQuery("select * from ContextChart where loc_node_id ="+locnodeid.toString());
			  Document doc=DocumentHelper.createDocument();
		 //建立XML的document
			   if(rs.next())
			   {
				   rs.last();
				   Integer size=rs.getRow();
				   Element root =doc.addElement("LocationContextList");
				   System.out.println(size);
				   root.addAttribute("ListSize",size.toString() );
				   
				   rs.first();
				   for(int i=0;i<size;i++)
				   {
						
						//添加相关节点及信息
						Element subroot =root.addElement("ContextInfo");
						Element context_id=subroot.addElement("context_id");
						context_id.setText(rs.getString(1));
						Element loc_node_idsl=subroot.addElement("loc_node_id");
						loc_node_idsl.setText(rs.getString(2));
						Element context_name=subroot.addElement("context_name");
						context_name.setText(rs.getString(3));
						
						rs.next();
						
				   }
				   return doc.asXML();
			   }
			   Element root =doc.addElement("LocationContextList");
			   root.addAttribute("ListSize","0" );
			   return doc.asXML();
		    }
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally {
            if (con!=null) try {con.close();}catch (Exception ignore) {}
          } 
		
		Document doce=DocumentHelper.createDocument();
		Element roote =doce.addElement("error");
		roote.addAttribute("type", "loc_node_id not found");
		return doce.asXML();
	}

}

/**
 * 文件名：UserResource.java
 * 描述：用户（user）资源文件
 * 创建日期：2013-01-14
 * 创建者：高继扬
 * 修改历史：who，when，why
 */


package com.imhere.restapi;

//import javax.management.timer.Timer;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
//import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.dom4j.*;

import com.imhere.persistence.Session;
import com.imhere.persistence.User;

import java.sql.*;


/**UserResource
 * @描述：根路径/users，定义用户资源API
 *@author 高继扬，2013-01-15
 *
 */
@Path("/users")
public class UserResource {
	
	/**
	 * @描述 将输入userid的对应的用户信息返回
	 * @param userid
	 * @return 用户信息的xml表示
	 * @author 高继扬，2013-01-14
	 * @修改历史：高继扬，2013-02-06，修改连接池，使用c3p0，为hibernate做准备
	 */
	@GET
	@Produces({MediaType.APPLICATION_XML})
	@Path("{id}")
	public String getUserInfo(@PathParam("id") int userid)
	{
		
	
		// long begintime = System.nanoTime();
		// long endtime;
		Context initCtx;
		Connection con = null;
		try {
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
			initCtx = new InitialContext();
			DataSource datasource;
			datasource = (DataSource)initCtx.lookup("java:comp/env/jdbc/heredb"); 
		    con = datasource.getConnection(); //should return instantly
		    User user=Session.loadUser(userid, con);
		    if(user!=null) 
			    return user.exportXML();
			  
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
		roote.addAttribute("type","user_id not found");
		return doce.asXML();
		
	}
	
	/**
	 * @描述 创建新用户
	 * @param representation，用户信息的xml表示
	 * @author 高继扬，2013-01-14
	 * @修改历史：高继扬，2013-02-06，修改连接池，使用c3p0，为hibernate做准备
	 */
	@POST
	@Consumes({MediaType.APPLICATION_XML})
	@Produces({"application/xml"})
	public String createUserInfo(String representation)
	{	
		Context initCtx;
		Connection con = null;
		try {
			
			User user=new User();
			if(user.importXML(representation))
			{
				initCtx = new InitialContext();
				DataSource datasource;
				datasource = (DataSource)initCtx.lookup("java:comp/env/jdbc/heredb"); 
				con = datasource.getConnection(); //should return instantly
				
				int result=Session.saveUser(user, con);
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

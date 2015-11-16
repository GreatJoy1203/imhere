/**
 * 文件名：ContextResource.java
 * 描述：场景（context）资源文件
 * 创建日期：2013-01-16
 * 创建者：高继扬
 * 修改历史：who，when，why
 */



package com.imhere.restapi;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.dom4j.*;

import com.imhere.persistence.ContextMsg;
import com.imhere.persistence.Session;
import com.imhere.persistence.User;

import java.sql.*;


/**ContextResource
 * @描述：根路径/contexts，定义场景资源API
 *@author 高继扬，2013-01-16
 *@修改历史：高继扬，2013-02-06，修改连接池，使用c3p0，为hibernate做准备
 */
@Path("/contexts")
public class ContextResource {
	/**
	 * @描述 将输入id对应的场景信息返回
	 * @param ctxid
	 * @author 高继扬，2013-01-16
	 * @修改历史：高继扬，2013-02-06，修改连接池，使用c3p0，为hibernate做准备
	 */
	@Path("{ctxid}")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String getContextInfo(@PathParam("ctxid") int ctxid)
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
			  ResultSet rs = st.executeQuery("select * from ContextChart where context_id="+ctxid);
			  if(rs.next())
			   {
					Document doc=DocumentHelper.createDocument();
					//添加相关节点及信息
					Element root =doc.addElement("ContextInfo");
					Element context_id=root.addElement("context_id");
					context_id.setText(rs.getString(1));
					Element loc_node_id=root.addElement("loc_node_id");
					loc_node_id.setText(rs.getString(2));
					Element context_name=root.addElement("context_name");
					context_name.setText(rs.getString(3));
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
		roote.addAttribute("type","context_id not found");
		return doce.asXML();
	}
	
	@Path("{ctxid}/visitors")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String getVisitors(@PathParam("ctxid") int ctxid,@QueryParam("s") int start)
	{
		Context initCtx;
		Connection con = null;
		try {
			initCtx = new InitialContext();
			DataSource datasource;
			datasource = (DataSource)initCtx.lookup("java:comp/env/jdbc/heredb"); 
			con = datasource.getConnection(); //should return instantly
			return Session.getCtxVisitors(ctxid, start, con);
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		finally {
            if (con!=null) try {con.close();}catch (Exception ignore) {}
          } 
		Document doce=DocumentHelper.createDocument();
		Element roote =doce.addElement("error");
		roote.addAttribute("type","context_id/ user_id not found");
		return doce.asXML();
	}
	
	@Path("{ctxid}/visit")
	@POST
	@Produces(MediaType.APPLICATION_XML)
	public String visitContext(@PathParam("ctxid") int ctxid,@QueryParam("u") int user_id)
	{
		Context initCtx;
		Connection con = null;
		try {
			initCtx = new InitialContext();
			DataSource datasource;
			datasource = (DataSource)initCtx.lookup("java:comp/env/jdbc/heredb"); 
			con = datasource.getConnection(); //should return instantly
			Session.visitContext(ctxid, user_id, con);
			com.imhere.persistence.Context context=Session.loadContext(ctxid, con);
			User user=Session.loadUser(user_id, con);
			if(context!=null)
				return context.exportXML(user);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		finally {
            if (con!=null) try {con.close();}catch (Exception ignore) {}
          } 
		Document doce=DocumentHelper.createDocument();
		Element roote =doce.addElement("error");
		roote.addAttribute("type","context_id/ user_id not found");
		return doce.asXML();
	}
	
	@Path("{ctxid}/uploadmsg")
	@POST
	@Consumes({"application/xml"})
	@Produces(MediaType.APPLICATION_XML)
	public String uploadMessage(@PathParam("ctxid") int ctxid,
			                                            @QueryParam("u") int user_id,
			                                            String msg )
	{
		Context initCtx;
		Connection con = null;
		try {
			initCtx = new InitialContext();
			DataSource datasource;
			datasource = (DataSource)initCtx.lookup("java:comp/env/jdbc/heredb"); 
			con = datasource.getConnection(); //should return instantly
			ContextMsg message=new ContextMsg();
			message.setContextID(ctxid);
			java.util.Date now=new java.util.Date();
			long time=now.getTime()/1000;
			message.setMsgPath(ContextMsg.rootpath+ctxid+"_"+user_id+"_"+time+".xml");
			message.setMsg(msg);
			int id=Session.saveContextMsg(message, con);
			if(id!=-1)
			{
				Document doce=DocumentHelper.createDocument();
				Element roote =doce.addElement("result");
				roote.addAttribute("status","success");
				roote.setText(""+id);
				return doce.asXML();
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
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

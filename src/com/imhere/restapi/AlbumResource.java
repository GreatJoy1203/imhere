package com.imhere.restapi;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.dom4j.*;

import com.imhere.persistence.Album;
import com.imhere.persistence.Session;

import java.sql.*;


@Path("/albums")
public class AlbumResource {
	@Produces({"application/xml"})
	@POST
	public String createAlbum(@QueryParam("n") String name,
									@QueryParam("d") String des)
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
		
		//修改数据库，添加新行
		Context initCtx;
		Connection con = null;
		//int count=-1;
		try {
			initCtx = new InitialContext();
			DataSource datasource;
			datasource = (DataSource)initCtx.lookup("java:comp/env/jdbc/heredb"); 
			 con = datasource.getConnection(); //should return instantly
			  Album album=new Album(name,des);
			  int id=Session.saveAlbum(album, con);
			  if(id!=-1)
			  {
				    Document doce=DocumentHelper.createDocument();
					Element roote =doce.addElement("result");
					roote.addAttribute("status","success");
					roote.setText(""+id);
					return doce.asXML();
			  }
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
		Element roote =doce.addElement("result");
		roote.addAttribute("status","fail");
		return doce.asXML();
	}

	@Path("{aid}")
	@GET
	@Produces({"application/xml"})
	public String getAlbumInfo(@PathParam("aid") int aid)
	{
		
		Context initCtx;
		Connection con = null;
		//Document doc=DocumentHelper.createDocument();
		try {
			initCtx = new InitialContext();
			DataSource datasource;
			datasource = (DataSource)initCtx.lookup("java:comp/env/jdbc/heredb"); 
			  con = datasource.getConnection(); //should return instantly
			  Album album=Session.loadAlbum(aid, con);
			  if(album!=null)
				  return album.exportXML();
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
		roote.addAttribute("type","album_id not found");
		return doce.asXML();
	}
}

package com.imhere.restapi;

import javax.imageio.ImageIO;
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
import org.dom4j.*;

import com.imhere.persistence.Photo;
import com.imhere.persistence.Session;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;



@Path("/images")
public class PhotoResource {
	
	static final String rootpath="/home/greatjoy/ImageDB";
	static final String tinypath="/tiny/";
	static final String midpath="/mid/";
	static final String normalpath="/normal/";
	
	
	@Produces({"application/xml"})
	@Consumes({"image/png"})
	@POST
	public String uploadImagePNG(@QueryParam("n") String name,
									@QueryParam("d") String des,
									@QueryParam("a") int album_id,
									File file)
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
		
	//格式转换
	createNormalImage(file,rootpath+normalpath+name+".jpg");	
	//创建缩略图,jpeg格式
	createTinyImage(file,rootpath+tinypath+name+".jpg");
	createMidImage(file,rootpath+midpath+name+".jpg");
	
	//修改数据库，添加新行
	Context initCtx;
	Connection con = null;
	int count=-1;
	try {
		initCtx = new InitialContext();
		DataSource datasource;
		datasource = (DataSource)initCtx.lookup("java:comp/env/jdbc/heredb"); 
		  con = datasource.getConnection(); //should return instantly
		  Statement st = con.createStatement();
		  st.executeUpdate("INSERT INTO  `HereDatabase`.`PhotoChart` ("+
			//	  "`photo_id` ,"+
				  "`image_name` ,"+
				  "`description` ,"+
		//		  "`creattime` ,"+
				  "`album_id` ,"+
				  "`image_tiny` ,"+
				  "`image_mid` ,"+
				  "`image_normal`)"+
				  "VALUES ("+
		//		  "'NULL' ,"+  
				  "'"+name+"',"+
				  "'"+des+"',"+
		//		  "'CURRENT_TIMESTAMP'," +
				  "'"+album_id+"',"+
				  "'"+rootpath+tinypath+name+".jpg"+"',"+
				  "'"+rootpath+midpath+name+".jpg"+"',"+
				  "'"+rootpath+normalpath+name+".jpg"+"'"+
				  ")");
		  ResultSet rs=st.executeQuery("SELECT LAST_INSERT_ID();");  
		  if(rs.next())
			  count=rs.getInt(1);
		  if(count!=-1)
		  {
			  Document doce=DocumentHelper.createDocument();
				Element roote =doce.addElement("result");
				roote.addAttribute("status","success");
				roote.setText(""+count);
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
	
	
	@Produces({"application/xml"})
	@Consumes({"image/jpeg"})
	@POST
	public String uploadImageJPEG(@QueryParam("n") String name,
									@QueryParam("d") String des,
									@QueryParam("a") int album_id,
									byte[] image)
	{
	//保存正常大小的图片，jpeg格式
	File file=new File(rootpath+normalpath+name+".jpg");
	try {
		FileOutputStream fout=new FileOutputStream(file);
		fout.write(image);
		fout.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//创建缩略图,jpeg格式
	createTinyImage(file,rootpath+tinypath+name+".jpg");
	createMidImage(file,rootpath+midpath+name+".jpg");
	
	//修改数据库，添加新行
	Context initCtx;
	Connection con = null;
	int count=-1;
	try {
		initCtx = new InitialContext();
		DataSource datasource;
		datasource = (DataSource)initCtx.lookup("java:comp/env/jdbc/heredb"); 
		  con = datasource.getConnection(); //should return instantly
		  
		  Statement st = con.createStatement();
		  st.executeUpdate("INSERT INTO  `HereDatabase`.`PhotoChart` ("+
			//	  "`photo_id` ,"+
				  "`image_name` ,"+
				  "`description` ,"+
		//		  "`creattime` ,"+
				  "`album_id` ,"+
				  "`image_tiny` ,"+
				  "`image_mid` ,"+
				  "`image_normal`)"+
				  "VALUES ("+
		//		  "'NULL' ,"+  
				  "'"+name+"',"+
				  "'"+des+"',"+
		//		  "'CURRENT_TIMESTAMP'," +
				  "'"+album_id+"',"+
				  "'"+rootpath+tinypath+name+".jpg"+"',"+
				  "'"+rootpath+midpath+name+".jpg"+"',"+
				  "'"+rootpath+normalpath+name+".jpg"+"'"+
				  ")");
		  
		  ResultSet rs=st.executeQuery("SELECT LAST_INSERT_ID();");  
		  if(rs.next())
			  count=rs.getInt(1);
		  if(count!=-1)
		  {
			  Document doce=DocumentHelper.createDocument();
				Element roote =doce.addElement("result");
				roote.addAttribute("status","success");
				roote.setText(""+count);
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
	void createNormalImage(File file,String filepath)
	{
		try {
			Image image=ImageIO.read(file);
			//原始大小
			int width=image.getWidth(null);
			int height=image.getHeight(null);
			BufferedImage tag=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(image, 0, 0, width, height, null);
			FileOutputStream fout=new FileOutputStream(filepath);
			JPEGImageEncoder encoder=JPEGCodec.createJPEGEncoder(fout);
			encoder.encode(tag);
			fout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void createTinyImage(File file,String filepath)
	{
		try {
			Image image=ImageIO.read(file);
			//32*32
			int width=32;
			int height=32;
			BufferedImage tag=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(image, 0, 0, width, height, null);
			FileOutputStream fout=new FileOutputStream(filepath);
			JPEGImageEncoder encoder=JPEGCodec.createJPEGEncoder(fout);
			encoder.encode(tag);
			fout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void createMidImage(File file,String filepath)
	{
		try {
			Image image=ImageIO.read(file);
			//128*128
			int width=128;
			int height=128;
			BufferedImage tag=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(image, 0, 0, width, height, null);
			FileOutputStream fout=new FileOutputStream(filepath);
			JPEGImageEncoder encoder=JPEGCodec.createJPEGEncoder(fout);
			encoder.encode(tag);
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Path("{imageid}/tiny")
	@GET
	@Produces({"image/jpeg"})
	public File getTinyImage(@PathParam("imageid") int imageid)
	{

		Context initCtx;
		Connection con = null;
		File file=null;
		try {
			initCtx = new InitialContext();
			DataSource datasource;
			datasource = (DataSource)initCtx.lookup("java:comp/env/jdbc/heredb"); 
			  con = datasource.getConnection(); //should return instantly
			  Statement st = con.createStatement();
			  ResultSet rs=st.executeQuery(
					 " SELECT image_tiny FROM  `PhotoChart`  WHERE image_id ="+imageid);
			  if(rs.next())
			  {
				  file=new File(rs.getString("image_tiny"));
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
		return file;
	}
	@Path("{imageid}/normal")
	@GET
	@Produces({"image/jpeg"})
	public File getNormalImage(@PathParam("imageid") int imageid)
	{

		Context initCtx;
		Connection con = null;
		File file=null;
		try {
			initCtx = new InitialContext();
			DataSource datasource;
			datasource = (DataSource)initCtx.lookup("java:comp/env/jdbc/heredb"); 
			  con = datasource.getConnection(); //should return instantly
			  Statement st = con.createStatement();
			  ResultSet rs=st.executeQuery(
					 " SELECT image_normal FROM  `PhotoChart`  WHERE image_id ="+imageid);
			  if(rs.next())
			  {
				  file=new File(rs.getString("image_normal"));
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
		return file;
	}
	
	@Path("{imageid}/mid")
	@GET
	@Produces({"image/jpeg"})
	public File getMidImage(@PathParam("imageid") int imageid)
	{
		
		Context initCtx;
		Connection con = null;
		File file=null;
		try {
			initCtx = new InitialContext();
			DataSource datasource;
			datasource = (DataSource)initCtx.lookup("java:comp/env/jdbc/heredb"); 
			  con = datasource.getConnection(); //should return instantly
			  
			  Statement st = con.createStatement();
			  ResultSet rs=st.executeQuery(
					 " SELECT image_mid FROM  `PhotoChart`  WHERE image_id ="+imageid);
			  if(rs.next())
			  {
				  file=new File(rs.getString("image_mid"));
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
		return file;
	}
	@Path("{imageid}/info")
	@GET
	@Produces({"application/xml"})
	public String getImageInfo(@PathParam("imageid") int imageid)
	{
		
		Context initCtx;
		Connection con = null;
		try {
			initCtx = new InitialContext();
			DataSource datasource;
			datasource = (DataSource)initCtx.lookup("java:comp/env/jdbc/heredb"); 
			  con = datasource.getConnection(); //should return instantly
			  Photo image=Session.loadImage(imageid, con);
			  if(image!=null)
				  return image.exportXML();
			  
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
		roote.addAttribute("type","image_id not found");
		return doce.asXML();
	}

}

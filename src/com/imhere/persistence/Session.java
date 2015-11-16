package com.imhere.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;




public class Session {
	public Session(){}
	public static Topic loadTopic(int topic_id,Connection con) throws SQLException
	{
		if (con==null) return null;
		 Statement st = con.createStatement();
		 ResultSet rs=st.executeQuery("SELECT * FROM  `TopicChart` WHERE  `topic_id` ="+topic_id);
		 if(rs.next())
		 {
			 Topic topic=new Topic();
			 topic.setContent(rs.getString("content"));
			 topic.setCreateTime(rs.getTimestamp("createtime"));
			 topic.setForumID(rs.getInt("forum_id"));
			 topic.setTopicID(topic_id);
			 topic.setUserID(rs.getInt("user_id"));
			 rs=st.executeQuery("SELECT response_id FROM  `ResponseChart` WHERE  `topic_id` ="+topic_id);
			 if(rs.next())
			 {
				 rs.last();
				 int size=rs.getRow();		 
				 rs.first();
				for(int i=0;i<size;i++)
				{
					topic.addResponse(rs.getInt("response_id"));
					rs.next();
				}
			 }
			 return topic;
		 }
		 return null;
	}
	public static int saveTopic(Topic topic,Connection con)
	{
		if(con==null) return -1;
		try {
			Statement st = con.createStatement();	
			st.executeUpdate("INSERT INTO  `HereDatabase`.`TopicChart` ("+
					"`content` ,"+
					"`user_id` ,"+
					"`forum_id` )"+
					"VALUES ("+
					 "'"+topic.getContent()+"',"+
					 "'"+topic.getUserID()+"',"+
					 "'"+topic.getForumID()+"'"+
					")");
			ResultSet rs=st.executeQuery("SELECT LAST_INSERT_ID();");	
			int count=-1;
			 if(rs.next())
				  count=rs.getInt(1);
			return count;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return -1;
	}
	
	public static Forum loadForum(int forum_id,Connection con) throws SQLException
	{
		if (con==null) return null;
		 Statement st = con.createStatement();
		 ResultSet rs=st.executeQuery("SELECT * FROM  `ForumChart` WHERE  `forum_id` ="+forum_id);
		 if(rs.next())
		 {
			 Forum forum=new Forum();
			 forum.setCreateTime(rs.getTimestamp("createtime"));
			 forum.setForumID(rs.getInt("forum_id"));
			 forum.setName(rs.getString("forum_name"));
			 forum.setUserID(rs.getInt("user_id"));
			 rs=st.executeQuery("SELECT topic_id FROM  `TopicChart` WHERE  `forum_id` ="+forum_id);
			 if(rs.next())
			 {
				 rs.last();
				 int size=rs.getRow();		 
				 rs.first();
				for(int i=0;i<size;i++)
				{
					forum.addTopic(rs.getInt("topic_id"));
					rs.next();
				}
			 }
			 return forum;
		 }
		 return null;
	}
	
	public static int saveForum(Forum forum,Connection con)
	{
		if(con==null) return -1;
		try {
			Statement st = con.createStatement();	
			st.executeUpdate("INSERT INTO  `HereDatabase`.`ForumChart` ("+
					"`user_id` ,"+
					" `forum_name` )"+
					"VALUES ("+
					 "'"+forum.getUserID()+"',"+
					"'"+forum.getName()+"'"+
					")");
			ResultSet rs=st.executeQuery("SELECT LAST_INSERT_ID();");	
			int count=-1;
			 if(rs.next())
				  count=rs.getInt(1);
			return count;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return -1;
	}
	
	public static Photo loadImage(int imageid,Connection con) throws SQLException
	{
		if (con==null) return null;
		Statement st = con.createStatement();
		 ResultSet rs=st.executeQuery( " SELECT * FROM  `PhotoChart`  WHERE image_id ="+imageid);
		if(rs.next())
		{
			Photo image=new Photo();
			image.setImageID(imageid);
			image.setAlbumID(rs.getInt("album_id"));
			image.setCreateTime(rs.getTimestamp("creattime"));
			image.setDescription(rs.getString("description"));
			image.setName(rs.getString("image_name"));
			return image;
		}
		return null;
	}
	
	public static Album loadAlbum(int aid,Connection con) throws SQLException
	{
		if (con==null) return null;
		Statement st = con.createStatement();
		 ResultSet rs=st.executeQuery( " SELECT * FROM  `AlbumChart`  WHERE album_id ="+aid);
		if(rs.next())
		{
			Album album=new Album();
			album.setAlbumID(rs.getInt("album_id"));
			album.setCreateTime(rs.getTimestamp("creattime"));
			album.setDescription(rs.getString("description"));
			album.setName(rs.getString("album_name"));
			rs=st.executeQuery(" SELECT image_id FROM  `PhotoChart`  WHERE album_id ="+aid);
		    if(rs.next())
		    {
			    rs.last();
			    int size=rs.getRow();
			    rs.first();
			    for(int i=0;i<size;i++)
			    {
			 		//添加相关节点及信息
					 album.addImage(rs.getInt(1));
					 rs.next();
			    }
		    }
		  return album;
		}
		return null;
	}
	
	public static  int saveAlbum(Album album,Connection con)
	{
		if(con==null) return -1;
		Statement st;
		try {
			 st = con.createStatement();
			 st.executeUpdate("INSERT INTO  `HereDatabase`.`AlbumChart` ("+
					  "`album_name` ,"+
					  "`description` )"+
					  "VALUES ("+
					 "'"+album.getName()+"',"+
					 "'"+album.getDescription()+"'"+
					  ")");
			  ResultSet rs=st.executeQuery("SELECT LAST_INSERT_ID();");
			  int id=-1;
			  if(rs.next())
				  id=rs.getInt(1);
			  return id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return -1;
	}
	
	public static User loadUser(int userid,Connection con) throws SQLException
	{
		if(con==null) return null;
		 Statement st = con.createStatement();
		   ResultSet rs = st.executeQuery("select * from UserChart where user_id="+userid);
		 //建立XML的document
		   if(rs.next())
		   {
				User user=new User();
				//user_id
				user.setUserID(userid);
				//user_name
				user.setUserName(rs.getString("user_name"));
				//password
				user.setPassword(rs.getString("password"));
				//real_name
				user.setRealName(rs.getString("real_name"));
				//birthday
				user.setBirthday(rs.getDate("birthday"));
				//sex
				user.setSex(rs.getInt("sex"));
				//home town
				user.setCountry(rs.getInt("ht_country"));
				user.setProvince(rs.getInt("ht_province"));
				user.setCity(rs.getString("ht_city"));				
				//university
				user.setUniversity(rs.getInt("uv_name"));
				user.setEnrollYear(rs.getInt("uv_enrollyear"));
				user.setDepartment(rs.getInt("uv_department"));
				user.setImageID(rs.getInt("image_id"));
				
				return user;
		   }
		return null;
	}
	
	public static int saveUser(User user,Connection con) throws UnsupportedEncodingException
	{
		if(con==null) return -1;


		String user_name=user.getUserName();
		user_name="\'"+user_name+"\'";
		String password=user.getPassword();
		password="\'"+password+"\'";
		String real_name=new String(user.getRealName().getBytes("UTF-8"),"UTF-8");
		real_name="\'"+real_name+"\'";

		int sexi=user.getSex();
		String sex="\'"+sexi+"\'";
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		String birthday=format.format(user.getBirthday());
		birthday="\'"+birthday+"\'";
		
		int ht_countryi=user.getCountry();
		String ht_country="\'"+ht_countryi+"\'";	
		int ht_provincei=user.getProvince();
		String ht_province="\'"+ht_provincei+"\'";
		String ht_city=new String(user.getCity().getBytes("UTF-8"),"UTF-8");
		ht_city="\'"+ht_city+"\'";
	
		int uv_namei=user.getUniversity();
		String uv_name="\'"+uv_namei+"\'";
		int uv_enrollyeari=user.getEnrollYear();
		String uv_enrollyear="\'"+uv_enrollyeari+"\'";
		int uv_departmenti=user.getDepartment();
		String uv_department="\'"+uv_departmenti+"\'";
		
		int image_idi=user.getImageID();
		String image_id="\'"+image_idi+"\'";
		
		Statement st;
		try {
			st = con.createStatement();
			 st.executeUpdate("INSERT INTO  `HereDatabase`.`UserChart` (" +
						//	  		"`user_id` ," +
							  		"`user_name` ," +
							  		"`password` ," +
							  		"`real_name` ," +
							  		"`birthday` ," +
							  		"`sex` ," +
							  		"`ht_country` ," +
							  		"`ht_province` ," +
							  		"`ht_city` ," +
							  		"`uv_name` ," +
							  		"`uv_enrollyear` ," +
							  		"`uv_department`,"+
							  		"`image_id`)"+
							  		"VALUES (" +
							//  		user_id+", " +
							  		user_name+" , " +
							  		password+" ,  " +
							  		real_name+", " +
							  		birthday+" ,  " +
							  		sex+", " +
							  		ht_country+", " +
							  		ht_province+" , " +
							  		ht_city+"," +
							  		uv_name+"  , " +
							  		uv_enrollyear+", " +
							  		uv_department+","+
							  		image_id+
							  		")");
							  
		  ResultSet rs=st.executeQuery("SELECT LAST_INSERT_ID();");
		  int id=-1;
		  if(rs.next())
			  id=rs.getInt(1);
		  return id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -1;
		 
	}
	public static ContextMsg loadContextMsg(int context_id,Connection con)
	{
		if(con==null)
			return null;

		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from ContextMsgChart where context_id="+context_id);
			if(rs.next())
			{
				ContextMsg msg=new ContextMsg();
				msg.setContextID(context_id);
				msg.setCreateTime(rs.getTimestamp("createtime"));
				msg.setMsgID(rs.getInt("msg_id"));
				msg.setMsgPath(rs.getString("msg_path"));
				File file=new File(msg.getMsgPath());
				SAXReader reader = new SAXReader();
		        Document doc;
				try {
					doc = reader.read(file);// 读取XML文件
					msg.setMsg(doc.asXML());
				} catch (DocumentException e) {				
					e.printStackTrace();
					return null;//如果读取失败，返回null
				}
		         return msg;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return null;
	}
	
	public static int  saveContextMsg(ContextMsg message,Connection con)
	{
		if(con==null)
			return -1;
		byte[] msg=message.getMsg().getBytes();
		File file=new File(message.getMsgPath());
		try {
			FileOutputStream fout=new FileOutputStream(file);
			fout.write(msg);
			fout.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} 
		Statement st;
		try {
			 st = con.createStatement();
			 st.executeUpdate("INSERT INTO  `HereDatabase`.`ContextMsgChart` ("+
					 						"`context_id` ,"+
					 						"`msg_path`)"+
					 						"VALUES ("+
					 						"'"+message.getContextID()+"'," +
					 						"'"+message.getMsgPath()+"');"
					 						);


			  ResultSet rs=st.executeQuery("SELECT LAST_INSERT_ID();");
			  int id=-1;
			  if(rs.next())
				  id=rs.getInt(1);
			  return id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	public static Context loadContext(int context_id,Connection con)
	{
		if(con==null)
			return null;
		
		try {
			Statement st = con.createStatement();
			Statement stuser = con.createStatement();
			ResultSet rs = st.executeQuery("select * from ContextChart where context_id="+context_id);
			 if(rs.next())
		     {
				 
		    	Context context=new Context();
		    	//加载基本信息
				context.setContextID(rs.getInt("context_id"));
				context.setLocNodeID(rs.getString("loc_node_id"));
				context.setName(rs.getString("context_name"));
				//加载访问历史信息
				rs = st.executeQuery("SELECT * FROM  `UserVisitCtxChart` WHERE  `context_id` ="+rs.getString("context_id")+" ORDER BY  `time` DESC LIMIT 0 , 10");
				 if(rs.next())
				    {
					    rs.last();
					    int size=rs.getRow();
					    rs.first();
					    for(int i=0;i<size;i++)
					    {
							ContextVisit visit=new ContextVisit();
							visit.ContextID=context_id;
							visit.UserID=rs.getInt("user_id");
							visit.VisitTime=rs.getTimestamp("time");
							ResultSet userrs=stuser.executeQuery("select real_name, image_id from `UserChart` where user_id="+visit.UserID);
							if(userrs.next())
							{
								visit.UserImageID=userrs.getInt("image_id");
								visit.UserName=userrs.getString("real_name");
								context.addContextHistory(visit);
							}
							rs.next();
					    }
				    }
				 //加载场景留言信息
				 ContextMsg msg=Session.loadContextMsg(context_id, con);
				 if(msg!=null)	
					 context.setMessage(msg);
				 
				 return context;
		     }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
		 return null;
	}
	public static String getCtxVisitors(int contextid,int start,Connection con)
	{
		Statement st;
		try {
			st = con.createStatement();
			Statement stuser = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM  `UserVisitCtxChart` WHERE  `context_id` ="+contextid+" ORDER BY  `time` DESC LIMIT "+ start+" ,"+ (start+10));
			List<ContextVisit> history=new ArrayList<ContextVisit>();
			if(rs.next())
		    {
			    rs.last();
			    int size=rs.getRow();
			    rs.first();
			    for(int i=0;i<size;i++)
			    {
					ContextVisit visit=new ContextVisit();
					visit.ContextID=contextid;
					visit.UserID=rs.getInt("user_id");
					visit.VisitTime=rs.getTimestamp("time");
					ResultSet userrs=stuser.executeQuery("select real_name, image_id from `UserChart` where user_id="+visit.UserID);
					if(userrs.next())
					{
						visit.UserImageID=userrs.getInt("image_id");
						visit.UserName=userrs.getString("real_name");
						history.add(visit);
					}
					rs.next();
			    }
		    }
			Document doc=DocumentHelper.createDocument();
			Element root =doc.addElement("ContextInfo");
			Element visitors=root.addElement("Visitors");
			visitors.addAttribute("size",""+history.size());
			for(ContextVisit record:history)
			{
				Element visitor=visitors.addElement("visitor");
				Element userid=visitor.addElement("user_id");
				userid.setText(""+record.UserID);
				Element username=visitor.addElement("name");
				username.setText(record.UserName);
				Element time=visitor.addElement("time");
				time.setText(record.VisitTime.toString());
				Element image=visitor.addElement("image_id");
				image.setText(""+record.UserImageID);
			}
			return doc.asXML();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document doce=DocumentHelper.createDocument();
		Element roote =doce.addElement("error");
		roote.addAttribute("type","context_id not found");
		return doce.asXML();
	}
	
	public static boolean visitContext(int contextid,int userid,Connection con)
	{
		if(con==null) 
			return false;
		try {
			Statement st = con.createStatement();
			st.executeUpdate("INSERT INTO  `HereDatabase`.`UserVisitCtxChart` ("+
											"`user_id` ,"+
											"`context_id`)"+
											"VALUES ("+
											"'"+userid+"',"+
											"'"+contextid+"');" );
			return true;
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static TextSet loadTextSet(int sid,Connection con) throws SQLException
	{
		if (con==null) return null;
		Statement st = con.createStatement();
		 ResultSet rs=st.executeQuery( " SELECT * FROM  `TextSetChart`  WHERE textset_id ="+sid);
		if(rs.next())
		{
			TextSet textset=new TextSet();
			textset.setTextSetID(sid);
			textset.setCreateTime(rs.getTimestamp("createtime"));
			textset.setName(rs.getString("name"));
			rs=st.executeQuery(" SELECT text_id, text_name FROM  `TextChart`  WHERE textset_id ="+sid);
		    if(rs.next())
		    {
			    rs.last();
			    int size=rs.getRow();
			    rs.first();
			    for(int i=0;i<size;i++)
			    {
			 		//添加相关节点及信息
			    	TextAtom text=new TextAtom();
			    	text.TextID=rs.getInt("text_id");
			    	text.Name=rs.getString("text_name");
					 textset.addText(text);
					 rs.next();
			    }
		    }
		  return textset;
		}
		return null;
	}
	
	public static  int saveTextSet(TextSet textset,Connection con)
	{
		if(con==null) return -1;
		Statement st;
		try {
			st = con.createStatement();
			 st.executeUpdate("INSERT INTO  `HereDatabase`.`TextSetChart` ("+
					  "`name` )"+
					  "VALUES ("+
					 "'"+textset.getName()+"'"+
					  ")");
			  ResultSet rs=st.executeQuery("SELECT LAST_INSERT_ID();");
			  int id=-1;
			  if(rs.next())
				  id=rs.getInt(1);
			  return id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return -1;
	}
	
	public static HotPoint loadHotPoint(int hid,Connection con) throws SQLException
	{
		if (con==null) return null;
		Statement st = con.createStatement();
		 ResultSet rs=st.executeQuery( " SELECT * FROM  `HotPointChart`  WHERE hotpoint_id ="+hid);
		if(rs.next())
		{
			HotPoint hotpoint=new HotPoint();
			hotpoint.setHotPointID(hid);
			hotpoint.setCreateTime(rs.getTimestamp("createtime"));
			hotpoint.setName(rs.getString("name"));
			hotpoint.setHotPointType(rs.getInt("type"));
			Album album=loadAlbum(rs.getInt("album_id"),con);
			hotpoint.setAlbum(album);
			TextSet set=loadTextSet(rs.getInt("textset_id"),con);
			hotpoint.setTextSet(set);
			Forum forum=loadForum(rs.getInt("forum_id"),con);
			hotpoint.setForum(forum);
			
		     return hotpoint;
		}
		return null;
	}
	
	public static  int saveHotPoint(HotPoint hotpoint,Connection con)
	{
		if(con==null) return -1;
		Statement st;
		try {
			st = con.createStatement();
			 st.executeUpdate("INSERT INTO  `HereDatabase`.`HotPointChart` ("+
					 						"`name` ,"+
					 						"`type` ,"+
					 						"`textset_id` ,"+
					 						"`album_id` ,"+
					 						"`forum_id` )"+
					 						"VALUES ("+
					 						"'"+hotpoint.getName()+"',"+ 
					 						"'"+hotpoint.getHotPointType()+"',"+ 
					 						"'"+hotpoint.getTextSet().getTextSetID()+"',"+
					 						"'"+hotpoint.getAlbum().getAlbumID()+"'," +
					 						"'"+hotpoint.getForum().getForumID()+"');" );
			  ResultSet rs=st.executeQuery("SELECT LAST_INSERT_ID();");
			  int id=-1;
			  if(rs.next())
				  id=rs.getInt(1);
			  return id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return -1;
	}
	
}

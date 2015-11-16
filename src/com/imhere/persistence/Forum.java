package com.imhere.persistence;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Forum {
	private int ForumID;
	private int UserID;
	private String Name;
	private Set<Integer> Topics=new HashSet<Integer>();
	private Timestamp CreateTime;
	public Forum(){};
	public Forum(int userid)
	{
		this.UserID=userid;
	}
	public void setUserID(int id)
	{
		this.UserID=id;
	}
	public int getUserID()
	{
		return this.UserID;
	}
	public void setForumID(int id)
	{
		this.ForumID=id;
	}
	public int getForumID()
	{
		return this.ForumID;
	}
	public Set<Integer> getTopics()
	{
		return this.Topics;
	}
	public void setTopics(Set<Integer> resps)
	{
		this.Topics=resps;
	}
	public void addTopic(int res_id)
	{
		Topics.add(res_id);
	}
	public void setCreateTime(Timestamp time)
	{
		this.CreateTime=time;
	}
	public Timestamp getCreateTime()
	{
		return this.CreateTime;
	}
	public String getName()
	{
		return this.Name;
	}
	public void setName(String name)
	{
		this.Name=name;
	}
	
	public String exportXML()
	{
		Document doc=DocumentHelper.createDocument();
		Element root=doc.addElement("Forum");
		Element id=root.addElement("id");
		id.setText(""+ForumID);
		Element name=root.addElement("name");
		name.setText(this.Name);
		Element user_id=root.addElement("user_id");
		user_id.setText(""+UserID);
		Element createtime=root.addElement("createtime");
		createtime.setText(this.CreateTime.toString());
		Element resps=root.addElement("set");
		resps.addAttribute("size",""+Topics.size());
		for(int i:Topics)
		{
			Element resp=resps.addElement("topic_id");
			resp.setText(""+i);
		}
	
		return doc.asXML();
	}
	public boolean importXML(String representation)
	{
		try {
			Document doc = DocumentHelper.parseText(representation);
			Element forumele=doc.getRootElement();
			this.UserID=Integer.parseInt(forumele.element("user_id").getText());
			this.Name=forumele.element("name").getText();
			return true;
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public Element exportElement()
	{
		Document doc=DocumentHelper.createDocument();
		Element root=doc.addElement("Forum");
		Element id=root.addElement("id");
		id.setText(""+ForumID);
		Element name=root.addElement("name");
		name.setText(this.Name);
		Element user_id=root.addElement("user_id");
		user_id.setText(""+UserID);
		Element createtime=root.addElement("createtime");
		createtime.setText(this.CreateTime.toString());
		Element resps=root.addElement("set");
		resps.addAttribute("size",""+Topics.size());
		for(int i:Topics)
		{
			Element resp=resps.addElement("topic_id");
			resp.setText(""+i);
		}
		return root;
	}
}

package com.imhere.persistence;

import java.sql.Timestamp;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Response {
	private int ResponseID;
	private int UserID;
	private int TopicID;
	private String Content;
	private Timestamp CreateTime;
	
	public Response(){}
	public Response(String content,int userid,int topicid)
	{
		this.Content=content;
		this.UserID=userid;
		this.TopicID=topicid;
	}
	public void setContent(String content)
	{
		this.Content=content;
	}
	public String getContent()
	{
		return Content;
	}
	public void setUserID(int userid)
	{
		this.UserID=userid;
	}
	public int getUserID()
	{
		return this.UserID;
	}
	public void setTopicID(int userid)
	{
		this.TopicID=userid;
	}
	public int getTopicID()
	{
		return this.TopicID;
	}
	public void setResponseID(int userid)
	{
		this.ResponseID=userid;
	}
	public int getResponseID()
	{
		return this.ResponseID;
	}
	public void setCreateTime(Timestamp time)
	{
		this.CreateTime=time;
	}
	public Timestamp getCreateTime()
	{
		return this.CreateTime;
	}
	
	public String exportXML()
	{
		Document doc=DocumentHelper.createDocument();
		Element root=doc.addElement("Response");
		Element id=root.addElement("id");
		id.setText(""+ResponseID);
		Element content=root.addElement("content");
		content.setText(this.Content);
		Element user_id=root.addElement("user_id");
		user_id.setText(""+UserID);
		Element topic_id=root.addElement("topic_id");
		topic_id.setText(""+TopicID);
		Element createtime=root.addElement("createtime");
		createtime.setText(this.CreateTime.toString());
	
		return doc.asXML();
	}

}

package com.imhere.persistence;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Topic {
	private int TopicID;
	private int UserID;
	private int ForumID;
	private String Content;
	private Set<Integer> responses=new HashSet<Integer>();
	private Timestamp CreateTime;
	public Topic(){};
	public Topic(String content,int userid,int forumid)
	{
		this.Content=content;
		this.UserID=userid;
		this.ForumID=forumid;
	}
	public void setTopicID(int id)
	{
		this.TopicID=id;
	}
	public int getTopicID()
	{
		return this.TopicID;
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
	public void setContent(String content)
	{
		this.Content=content;
	}
	public String getContent()
	{
		return this.Content;
	}
	public Set<Integer> getResponses()
	{
		return this.responses;
	}
	public void setResponses(Set<Integer> resps)
	{
		this.responses=resps;
	}
	public void addResponse(int res_id)
	{
		responses.add(res_id);
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
		Element root=doc.addElement("Topic");
		Element id=root.addElement("id");
		id.setText(""+TopicID);
		Element content=root.addElement("content");
		content.setText(this.Content);
		Element user_id=root.addElement("user_id");
		user_id.setText(""+UserID);
		Element forum_id=root.addElement("forum_id");
		forum_id.setText(""+ForumID);
		Element createtime=root.addElement("createtime");
		createtime.setText(this.CreateTime.toString());
		Element resps=root.addElement("set");
		resps.addAttribute("size",""+responses.size());
		for(int i:responses)
		{
			Element resp=resps.addElement("response_id");
			resp.setText(""+i);
		}
	
		return doc.asXML();
	}
	public boolean importXML(String representation)
	{
		try {
			Document doc = DocumentHelper.parseText(representation);
			Element topicele=doc.getRootElement();
			this.UserID=Integer.parseInt(topicele.element("user_id").getText());
			this.ForumID=Integer.parseInt(topicele.element("forum_id").getText());
			this.Content=topicele.element("content").getText();
			return true;
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}

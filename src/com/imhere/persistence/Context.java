package com.imhere.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

class ContextVisit{
	public int UserID;
	public int UserImageID;
	public String UserName;
	public int ContextID;
	public Timestamp VisitTime;
}
public class Context {
	private int ContextID;
	private String LocNodeID;
	private String Name;
	private List<ContextVisit> ContextHistroy=new ArrayList<ContextVisit>();
	private ContextMsg Message=null;
	public int getContextID() {
		return ContextID;
	}
	public void setContextID(int contextID) {
		ContextID = contextID;
	}
	public String getLocNodeID() {
		return LocNodeID;
	}
	public void setLocNodeID(String locNodeID) {
		LocNodeID = locNodeID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public List<ContextVisit> getContextHistroy() {
		return ContextHistroy;
	}
	public void setContextHistroy(List<ContextVisit> contextHistroy) {
		ContextHistroy = contextHistroy;
	}
	public void addContextHistory(ContextVisit visit)
	{
		ContextHistroy.add(visit);
	}
	public ContextMsg getMessage() {
		return Message;
	}
	public void setMessage(ContextMsg message) {
		Message = message;
	}
	
	public String exportXML(User user)
	{
		Document doc=DocumentHelper.createDocument();
		Element root =doc.addElement("ContextInfo");
		//基本信息
		Element context_id=root.addElement("context_id");
		context_id.setText(""+this.ContextID);
		Element loc_node_id=root.addElement("loc_node_id");
		loc_node_id.setText(this.LocNodeID);
		Element context_name=root.addElement("context_name");
		context_name.setText(this.Name);
		//访问历史信息
		Element visitors=root.addElement("Visitors");
		visitors.addAttribute("size",""+this.ContextHistroy.size());
		for(ContextVisit record:ContextHistroy)
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
		//场景留言信息
		if(this.Message!=null)
		{
			if(Message.certifyVisit(user))
			{
				try {
					Document msg=DocumentHelper.parseText(Message.getMsg());
					Element message=msg.getRootElement();
					root.add(message);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return doc.asXML();
	}

}

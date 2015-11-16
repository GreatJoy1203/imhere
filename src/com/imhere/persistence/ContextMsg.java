package com.imhere.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class ContextMsg {
	public static final String rootpath="/home/greatjoy/MessageDB/"; 
	private int MsgID;
	private int ContextID;
	private String MsgPath;
	private Timestamp CreateTime;
	private String Msg;
	public ContextMsg(){}
	public ContextMsg(int contextid,String msgpath,Timestamp time,String msg)
	{
		this.ContextID=contextid;
		this.MsgPath=msgpath;
		this.CreateTime=time;
		this.Msg=msg;
	}
	public int getMsgID() {
		return MsgID;
	}
	public void setMsgID(int msgID) {
		MsgID = msgID;
	}
	public int getContextID() {
		return ContextID;
	}
	public void setContextID(int contextID) {
		ContextID = contextID;
	}
	public String getMsgPath() {
		return MsgPath;
	}
	public void setMsgPath(String msgPath) {
		MsgPath = msgPath;
	}
	public Timestamp getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(Timestamp createtime) {
		this.CreateTime = createtime;
	}
	public String getMsg() {
		return Msg;
	}
	public void setMsg(String message) {
		Msg = message;
	}
	
	public String exportXML()
	{
		return Msg;
	}
	public boolean certifyVisit(User user)
	{
		if(user==null) return false;
		try {
			Document doc=DocumentHelper.parseText(Msg);
			Element root=doc.getRootElement();
			Element condition=root.element("conditions");
			//性别
			Element sex=condition.element("sex");
			int sexnum=Integer.parseInt(sex.getText());
			if(sexnum!=2&&user.getSex()!=sexnum)
				return false;
			//年级
			Element grade=condition.element("grade");
			int from=Integer.parseInt(grade.element("from").getText());
			int to=Integer.parseInt(grade.element("to").getText());
			if((2013-user.getEnrollYear())<from||(2013-user.getEnrollYear())>to)
				return false;
			//投放时间
			Element ts=condition.element("ts");
			int start=Integer.parseInt(ts.element("start").getText());
			int end=Integer.parseInt(ts.element("end").getText());
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date create = df.parse(this.CreateTime.toString());
			Date now=new Date();
			long span=(now.getTime()-create.getTime())/1000;
			if(span<start||span>end) return false;
			//系别
			//尚未添加
			
			return true;
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return false;
	}
	

}

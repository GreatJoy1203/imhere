package com.imhere.persistence;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

class TextAtom
{
	public int TextID;
	public String Name;
}

public class TextSet {

	private Timestamp CreateTime;
	private int TextSetID;
	private String Name;
	private Set<TextAtom> Texts=new HashSet<TextAtom>();
	public int getTextSetID() {
		return TextSetID;
	}
	public void setTextSetID(int textSetID) {
		TextSetID = textSetID;
	}
	public Timestamp getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(Timestamp createTime) {
		CreateTime = createTime;
	}
	public Set<TextAtom> getTexts() {
		return Texts;
	}
	public void setTexts(Set<TextAtom> texts) {
		Texts = texts;
	}
	public void addText(TextAtom text)
	{
		Texts.add(text);
	}
	
	public String exportXML()
	{
		Document doc=DocumentHelper.createDocument();
	    Element root=doc.addElement("TextSetInfo");
	    Element id=root.addElement("id");
	    id.setText(""+this.TextSetID);
	    Element name=root.addElement("name");
	    name.setText(this.Name);
	    Element list =root.addElement("TextList");
	    list.addAttribute("size",""+Texts.size());
		   
	    for(TextAtom text:Texts)
	    {
		 	//添加相关节点及信息
			 Element textid =list.addElement("text_id");
			 textid.setText(""+text.TextID);
			 Element tname =list.addElement("name");
			 tname.setText(text.Name);
	    }
	     return doc.asXML();
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
	
	public Element exportElement()
	{
		Document doc=DocumentHelper.createDocument();
	    Element root=doc.addElement("TextSetInfo");
	    Element id=root.addElement("id");
	    id.setText(""+this.TextSetID);
	    Element name=root.addElement("name");
	    name.setText(this.Name);
	    Element list =root.addElement("TextList");
	    list.addAttribute("size",""+Texts.size());
		   
	    for(TextAtom text:Texts)
	    {
		 	//添加相关节点及信息
			 Element textid =list.addElement("text_id");
			 textid.setText(""+text.TextID);
			 Element tname =list.addElement("name");
			 tname.setText(text.Name);
	    }
	     return root;
	}
	
}

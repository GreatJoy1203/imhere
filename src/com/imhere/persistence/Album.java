package com.imhere.persistence;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Album {

	private String Name;
	private String Description;
	private Timestamp CreateTime;
	private int AlbumID;
	private Set<Integer> Images=new HashSet<Integer>();
	public Album(){}
	public Album(String name,String des)
	{
		this.Name=name;
		this.Description=des;
	}
	
	public void setAlbumID(int id)
	{
		this.AlbumID=id;
	}
	public int getAlbumID()
	{
		return this.AlbumID;
	}
	public String getName()
	{
		return this.Name;
	}
	public void setName(String name)
	{
		this.Name=name;
	}
	public String getDescription()
	{
		return this.Description;
	}
	public void setDescription(String des)
	{
		this.Description=des;
	}
	public void setCreateTime(Timestamp time)
	{
		this.CreateTime=time;
	}
	public Timestamp getCreateTime()
	{
		return this.CreateTime;
	}
	public Set<Integer> getImages()
	{
		return this.Images;
	}
	public void addImage(int id)
	{
		Images.add(id);
	}
	
	
	public String exportXML()
	{
		Document doc=DocumentHelper.createDocument();
	    Element root=doc.addElement("AlbumInfo");
	    Element id=root.addElement("id");
	    id.setText(""+this.AlbumID);
	    Element name=root.addElement("name");
	    name.setText(this.Name);
	    Element des=root.addElement("description");
	    des.setText(this.Description);
	    Element list =root.addElement("ImageList");
	    list.addAttribute("size",""+Images.size());
		   
	    for(int i:Images)
	    {
		 	//添加相关节点及信息
			 Element imageid =list.addElement("image_id");
			 imageid.setText(""+i);
	    }
	     return doc.asXML();
		   
	}
	
	public Element exportElement()
	{
		Document doc=DocumentHelper.createDocument();
	    Element root=doc.addElement("AlbumInfo");
	    Element id=root.addElement("id");
	    id.setText(""+this.AlbumID);
	    Element name=root.addElement("name");
	    name.setText(this.Name);
	    Element des=root.addElement("description");
	    des.setText(this.Description);
	    Element list =root.addElement("ImageList");
	    list.addAttribute("size",""+Images.size());
		   
	    for(int i:Images)
	    {
		 	//添加相关节点及信息
			 Element imageid =list.addElement("image_id");
			 imageid.setText(""+i);
	    }
	    return root;
		   
	}
	
}

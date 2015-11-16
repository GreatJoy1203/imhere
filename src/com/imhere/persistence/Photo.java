package com.imhere.persistence;

import java.sql.Timestamp;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


public class Photo {
	public static final String rootpath="/home/greatjoy/ImageDB";
	public static final String tinypath="/tiny/";
	public static final String midpath="/mid/";
	public static final String normalpath="/normal/";
	
	private int ImageID;
	private String Name;
	private String Description;
	private Timestamp CreateTime;
	private int AlbumID;
	
	public void setImageID(int id)
	{
		this.ImageID=id;
	}
	public int getImageID()
	{
		return this.ImageID;
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
	
	public String exportXML()
	{
		  Document doc=DocumentHelper.createDocument();
		  Element root=doc.addElement("ImageInfo");
		  Element id=root.addElement("id");
		  id.setText(""+this.ImageID);
		  Element name=root.addElement("name");
		  name.setText(this.Name);
		  Element des=root.addElement("description");
		  des.setText(this.Description);
		  Element aid=root.addElement("aid");
		  aid.setText(""+this.AlbumID);
		  return doc.asXML();
	}
}

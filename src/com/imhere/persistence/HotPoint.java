package com.imhere.persistence;

import java.sql.Timestamp;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class HotPoint {
	
	private int HotPointID;
	private int HotPointType;
	private String Name;
	private TextSet m_TextSet;
	private Album m_Album;
	private Forum m_Forum;
	private Timestamp CreateTime;
	public int getHotPointID() {
		return HotPointID;
	}
	public void setHotPointID(int hotPointID) {
		HotPointID = hotPointID;
	}
	public int getHotPointType() {
		return HotPointType;
	}
	public void setHotPointType(int hotPointType) {
		HotPointType = hotPointType;
	}
	public TextSet getTextSet() {
		return m_TextSet;
	}
	public void setTextSet(TextSet m_TextSet) {
		this.m_TextSet = m_TextSet;
	}
	public Album getAlbum() {
		return m_Album;
	}
	public void setAlbum(Album m_Album) {
		this.m_Album = m_Album;
	}
	public Forum getForum() {
		return m_Forum;
	}
	public void setForum(Forum m_Forum) {
		this.m_Forum = m_Forum;
	}
	public Timestamp getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(Timestamp createTime) {
		CreateTime = createTime;
	}
	
	public String exportXML()
	{
		Document doc=DocumentHelper.createDocument();
	    Element root=doc.addElement("HotPoint");
	    Element id=root.addElement("id");
	    id.setText(""+this.HotPointID);
	    Element type=root.addElement("type");
	    type.setText(""+this.HotPointType);
	    Element name=root.addElement("name");
	    name.setText(this.Name);
	    Element time=root.addElement("createtime");
	    time.setText(this.getCreateTime().toString());
	    root.add(this.m_Album.exportElement());
	    root.add(this.m_TextSet.exportElement());
	    root.add(this.m_Forum.exportElement());
	        
	    return doc.asXML();
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}

}

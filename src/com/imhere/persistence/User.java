package com.imhere.persistence;


import java.io.UnsupportedEncodingException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class User {
	private int UserID;
	private String Password;
	private String UserName;
	private String RealName;
	private Date Birthday;
	private int Sex;
	private int Country;
	private int Province;
	private String City;
	private int University;
	private int Department;
	private int EnrollYear;
	private int ImageID;
	
	public int getUserID() {
		return UserID;
	}
	public void setUserID(int userID) {
		UserID = userID;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getRealName() {
		return RealName;
	}
	public void setRealName(String realName) {
		RealName = realName;
	}
	public Date getBirthday() {
		return Birthday;
	}
	public void setBirthday(Date birthday) {
		Birthday = birthday;
	}
	public int getSex() {
		return Sex;
	}
	public void setSex(int sex) {
		Sex = sex;
	}
	public int getCountry() {
		return Country;
	}
	public void setCountry(int country) {
		Country = country;
	}
	public int getProvince() {
		return Province;
	}
	public void setProvince(int province) {
		Province = province;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public int getUniversity() {
		return University;
	}
	public void setUniversity(int university) {
		University = university;
	}
	public int getDepartment() {
		return Department;
	}
	public void setDepartment(int department) {
		Department = department;
	}
	public int getEnrollYear() {
		return EnrollYear;
	}
	public void setEnrollYear(int enrollYear) {
		EnrollYear = enrollYear;
	}
	
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public int getImageID() {
		return ImageID;
	}
	public void setImageID(int imageID) {
		ImageID = imageID;
	}
	public String exportXML()
	{
		Document doc=DocumentHelper.createDocument();
		//添加相关节点及信息
		Element root =doc.addElement("UserInfo");
		//user_id
		Element user_id=root.addElement("user_id");
		user_id.setText(""+this.UserID);
		//user_name
		Element user_name=root.addElement("user_name");
		user_name.setText(this.UserName);
		//password
		Element password=root.addElement("password");
		password.setText(this.Password);
		//real_name
		Element realname=root.addElement("real_name");
		realname.setText(this.RealName);
		//birthday
		Element birthday=root.addElement("birthday");
		birthday.setText(this.Birthday.toString());
		//sex
		Element sex=root.addElement("sex");
		sex.setText(""+this.Sex);
		//home town
		Element hometown=root.addElement("hometown");
		Element ht_country=hometown.addElement("country");
		ht_country.setText(""+this.Country);
		Element ht_province=hometown.addElement("province");
		ht_province.setText(""+this.Province);
		Element ht_city=hometown.addElement("city");
		ht_city.setText(this.City);				
		//university
		Element university=root.addElement("university");
		Element uv_name=university.addElement("name");
		uv_name.setText(""+this.University);
		Element uv_enrollyear=university.addElement("enrollyear");
		uv_enrollyear.setText(""+this.EnrollYear);
		Element uv_department=university.addElement("department");
		uv_department.setText(""+this.Department);		
		//image
		Element image=root.addElement("image_id");
		image.setText(""+this.ImageID);
		
		
		return doc.asXML();
	}
	
	@SuppressWarnings("deprecation")
	public boolean importXML(String representation)
	{
		Document doc;
		try {
			doc = DocumentHelper.parseText(representation);
			Element userinfo = doc.getRootElement();
			//this.UserID=Integer.parseInt(userinfo.element("user_id").getText());
			this.UserName=userinfo.element("user_name").getText();
			this.Password=userinfo.element("password").getText();	
			this.RealName=new String(userinfo.element("real_name").getText().getBytes("UTF-8"),"UTF-8");
			this.Sex=Integer.parseInt(userinfo.element("sex").getText());
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
			try {
				this.Birthday=format.parse(userinfo.element("birthday").getText());
			} catch (ParseException e) {
				this.Birthday=new Date(2013-1900, 1-1, 1);
				e.printStackTrace();
			}
			
			Element hometown=userinfo.element("hometown");
			this.Country=Integer.parseInt(hometown.element("country").getText());
			this.Province=Integer.parseInt(hometown.element("province").getText());
			this.City=new String(hometown.element("city").getText().getBytes("UTF-8"),"UTF-8");
			
			Element university=userinfo.element("university");
			this.University=Integer.parseInt(university.element("name").getText());
			this.EnrollYear=Integer.parseInt(university.element("enrollyear").getText());
			this.Department=Integer.parseInt(university.element("department").getText());
			
			this.ImageID=Integer.parseInt(userinfo.element("image_id").getText());
			
			return true;
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return false;
	}
	
	

}

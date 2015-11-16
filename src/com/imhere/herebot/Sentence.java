package com.imhere.herebot;

public class Sentence {
	
	public Sentence(String origin)
	{
		this.m_Origin=origin;
		//如果origin是空的，则将数组长度置为0
		if(origin.equals(""))
			this.m_Splits=new String[0];
		else
			this.m_Splits=origin.split(" ");
	}
	public Sentence()
	{
		
	}
	public String m_Origin;
	public String[ ] m_Splits;

}

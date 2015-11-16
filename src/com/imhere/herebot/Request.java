package com.imhere.herebot;

import java.util.ArrayList;
import java.util.List;

public class Request {
	public Request(String origin,List<Sentence> sentences)
	{
		this.m_Origin=origin;
		this.m_Sentences=sentences;
	}
	public Request()
	{
		m_Sentences=new ArrayList<Sentence>();
		m_Origin=new String();
	}
	
	public String m_Origin;
	public List<Sentence> m_Sentences;
	public int m_ObjectID;

}

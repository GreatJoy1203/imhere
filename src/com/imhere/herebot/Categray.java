package com.imhere.herebot;

import java.util.ArrayList;
import java.util.List;

public class Categray {
	public Categray(String pattern,String api)
	{
		m_API=api;
		m_Pattern=pattern;
		m_Template=new ArrayList<String>();
	}
	public String m_API;
	public String m_Pattern;
	public List<String> m_Template;
	
	public void addTemplate(String template)
	{
		m_Template.add(template);
	}
}

package com.imhere.herebot;

import java.util.Map;

public class BasicIntepreter {
	private String m_Name;

	public String getName(Map<String,String> param) {
		return "编号"+param.get("id"); 
		//return m_Name;
	}

	public void setName(String name) {
		this.m_Name = name;
	}
	
}

package com.imhere.herebot;


import java.util.HashMap;
import java.util.Map;

public class RuleNode {
	
	public RuleNode(String pattern,Categray cat)
	{
		this.m_Categray=cat;
		this.m_Pattern=pattern;
		m_ChildNodes=new HashMap<String,RuleNode>();
	}
	Map<String ,RuleNode> m_ChildNodes;
	Categray m_Categray;
	public String m_Pattern;

}

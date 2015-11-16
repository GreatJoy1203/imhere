package com.imhere.herebot;

import edu.fudan.nlp.cn.tag.CWSTagger;

public class NLPModule {
	public NLPModule() throws Exception
	{
		m_Tag= new CWSTagger("/home/greatjoy/workspace/imhere/seg.m");
	}
	public CWSTagger m_Tag;
	protected String[] splitSentence(String origin)
	{
		String split=origin.replace("，", " ").replace("！", " ").replace("。", " ").replace("；", " ").replace("、", "").replace("？", " ");
		return split.trim().split(" ");
	}
	public Request processSentence(String origin,int objectid)
	{
		//替换标点，分句
		String[] splitsentence=splitSentence(origin);
		Request request=new Request();
		for(String str:splitsentence)
		{
			//分词
			String s=m_Tag.tag(str);
			request.m_Sentences.add(new Sentence(s));
		}
		request.m_ObjectID=objectid;
		return request;
	}
}

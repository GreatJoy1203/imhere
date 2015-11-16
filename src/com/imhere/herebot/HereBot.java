package com.imhere.herebot;

import java.util.Scanner;



public class HereBot {
	
	//private final String BasicFile;
	//private final String ContextFile;
	//private final String
	
	public static void main(String[] args) throws Exception 
	{
		/*
		GraphMaster master=new GraphMaster();
		master.importXML("herebot.xml");
		
		CWSTagger tag = new CWSTagger("./models/seg.m");
		//String str="北京今天的天气真好";
		
		java.util.Scanner sc  = new Scanner(System.in);
		
		while(true)
		{
			String input  = sc.next();
			String s= tag.tag(input);
			Sentence sentence=new Sentence(s);
			Response res=master.response(sentence);
			if(res!=null)
				System.out.println(res.m_Response);
			else
				System.out.println("这句话真心听不懂了......");
		}*/
		//BasicIntepreter test;
		/*
		Map<String,String> param=new HashMap<String,String>();
		param.put("test", "that's it");
		String code="BasicIntepreter::getName";
		String[] decode=code.split("::");
		Class<?> c=Class.forName("com.imhere.herebot."+decode[0]);
		Class[] parameterTypes = new Class[1];
		Object object=c.newInstance();
		parameterTypes[0]=Map.class;
		Method getNamemethod=c.getMethod(decode[1], parameterTypes);
		String str=(String)getNamemethod.invoke(object, param);
		System.out.println(str);*/
		//ObjectBot bot=new ObjectBot();
		//Response resp=bot.intepreteResponse(new Response("我是[BasicIntepreter::getName],你是谁啊？","NULL"));
		//System.out.println(resp.m_Response);
		IntelBot bot=new IntelBot();
		java.util.Scanner sc  = new Scanner(System.in);
		while(true)
		{
			String input  = sc.next();
			Response res=bot.response(input,1001234123);
			
			if(!res.m_Response.equals(""))
				System.out.println(res.m_Response);
			else
				System.out.println("这句话真心听不懂了......");
		}
	}
}

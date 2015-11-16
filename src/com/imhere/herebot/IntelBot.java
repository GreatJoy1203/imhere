package com.imhere.herebot;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import edu.fudan.nlp.cn.tag.CWSTagger;

public class IntelBot {
	protected Map<Integer,GraphMaster> m_Masters;
	protected NLPModule m_NLP;
	public IntelBot() throws Exception
	{
		m_Masters=new HashMap<Integer,GraphMaster>();
		m_NLP = new NLPModule();
		
		GraphMaster master=new GraphMaster();
		master.importXML("/home/greatjoy/workspace/imhere/herebot.xml", m_NLP.m_Tag);
		
		m_Masters.put(0,master);
	}
	
	
	protected Response intepreteResponse(Response response) throws  Exception
	{
		StringBuilder result=new StringBuilder();
		char c;
		for(int i=0;i<response.m_Response.length();i++)
		{
			c=response.m_Response.charAt(i);
			if(c!='[')
				result.append(c);
			else
			{
				i++;
				StringBuilder code=new StringBuilder();
				for(;i<response.m_Response.length();i++)
				{
					c=response.m_Response.charAt(i);
					if(c!=']')
						code.append(c);
					else
					{
						Map<String,String> param=new HashMap<String,String>();
						String id=new Integer(response.m_ObjectID).toString();
						param.put("id",id );
						//分割，前半部分是类名，后半部分是方法
						String[] decode=code.toString().split("::");
						//创建类
						Class<?> cc=Class.forName("com.imhere.herebot."+decode[0]);
						Class[] parameterTypes = new Class[1];
						//创建实例
						Object object=cc.newInstance();
						//统一使用映射作为参数
						parameterTypes[0]=Map.class;
						//建立方法
						Method getNamemethod=cc.getMethod(decode[1], parameterTypes);
						//取得结果
						String str=(String)getNamemethod.invoke(object, param);
						//添加
						result.append(str);
						break;
					}
				}
			}
		}
		Response newresp=new Response(result.toString(),response.m_URI);
		newresp.m_ObjectID=response.m_ObjectID;
		return newresp;
	}
	protected Response matchResponse(Request request)
	{
		Response response=new Response("","NULL");
		response.m_ObjectID=request.m_ObjectID;
		//取该整数的2、3位
		int typeid=request.m_ObjectID/10000000-100;
		//对每一句话匹配
		for(Sentence sentence:request.m_Sentences)
		{
			
			Response temp=m_Masters.get(typeid).response(sentence);
			if(temp!=null)
			{
				if(!temp.m_URI.equals("NULL"))
				{
					response.m_URI=temp.m_URI;
				}
				//将结果相加
				response.m_Response+=temp.m_Response;
			}
		}
		return response;
	}
	
	
	public Response response(String origin,int objectid)
	{
		Request request=m_NLP.processSentence(origin,objectid);
		Response response=matchResponse(request);
		try {
			Response result=intepreteResponse(response);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
}

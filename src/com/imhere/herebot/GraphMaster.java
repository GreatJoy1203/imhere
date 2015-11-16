package com.imhere.herebot;

import java.io.File;
import java.util.Iterator;

import edu.fudan.nlp.cn.tag.CWSTagger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class GraphMaster {
	private RuleNode m_Root;
	
	public GraphMaster()
	{
		m_Root=new RuleNode("ROOT",null);
	}
	public Response response(Sentence sentence)
	{
		RuleNode node=matchPattern(sentence,m_Root);
		if(node!=null)
		{
			Response resp=new Response(node.m_Categray.m_Template.get(0),node.m_Categray.m_API);
			return resp;
		}
		return null;
	}
	protected RuleNode matchPattern(Sentence sentence,RuleNode node)
	{
		//如果目前sentence的长度为0
		if(sentence.m_Splits.length==0)
		{
			//首先检查当前节点是否包含叶子节点
			if(node.m_ChildNodes.containsKey("#LEAF#"))
			{
				//直接返回
				return node.m_ChildNodes.get("#LEAF#");
			}
			//检查当前节点是否包含通配符节点
			if(node.m_ChildNodes.containsKey("*"))
			{
				//递归至下一级通配符
				return matchWildCard(sentence,node.m_ChildNodes.get("*"),null);
			}
		}
		
		//sentence的长度大于0，取得首词
		String first=sentence.m_Splits[0];
		//如果当前节点的字节点中包含首词节点
		if(node.m_ChildNodes.containsKey(first))
		{
			//新建node
			RuleNode nextnode=node.m_ChildNodes.get(first);
			//新建sentence
			StringBuilder str=new StringBuilder();
			for(int i=1;i<sentence.m_Splits.length;i++)
			{
				str.append(sentence.m_Splits[i]+" ");
			}
			Sentence nextsen=new Sentence(str.toString());
			
			//递归至下一级
			return matchPattern(nextsen,nextnode);
		}
		if(node.m_ChildNodes.containsKey("*"))
		{
			//新建node
			RuleNode nextnode=node.m_ChildNodes.get("*");
			//新建sentence
			StringBuilder str=new StringBuilder();
			for(int i=1;i<sentence.m_Splits.length;i++)
			{
				str.append(sentence.m_Splits[i]+" ");
			}
			Sentence nextsen=new Sentence(str.toString());
			
			//递归至下一级
			return matchWildCard(nextsen,nextnode,first);
		}
		return null;
	}
	
	protected RuleNode matchWildCard(Sentence sentence, RuleNode node,String lastfirst)
	{
		//lastfirst为null则sentence长度比为0，即前者是后者的充分条件，如果
		//lastfirst为null则说明输入类似“今天天气X”，X为好则返回结果，X为“差”
		//则无匹配项，返回null。lastfirst不是null，则需要继续检查lastfirst在该节点的
		//子节点中的情况，即不返回。
		if(sentence.m_Splits.length==0)
		{
			if(node.m_ChildNodes.containsKey("#LEAF#"))
				return node.m_ChildNodes.get("#LEAF#");
			else
			{
				if(lastfirst==null)
					return null;
			}
		}
		
		//首先检查lastfirst，这种情况是把*当作空的情况
		if(node.m_ChildNodes.containsKey(lastfirst))
		{
			//新建node
			RuleNode nextnode=node.m_ChildNodes.get(lastfirst);
			//新建sentence（复制）
			Sentence nextsen=new Sentence(sentence.m_Origin);
			
			//递归至下一级
			return matchPattern(nextsen,nextnode);
		}
		
		if(sentence.m_Splits.length>0)
		{
			//这种情况是把*当作一个单词的情况，*替代了一个词
			//sentence的长度大于0，取得首词
			String first=sentence.m_Splits[0];
			//如果当前节点的字节点中包含首词节点
			if(node.m_ChildNodes.containsKey(first))
			{
				//新建node
				RuleNode nextnode=node.m_ChildNodes.get(first);
				//新建sentence
				StringBuilder str=new StringBuilder();
				for(int i=1;i<sentence.m_Splits.length;i++)
				{
					str.append(sentence.m_Splits[i]+" ");
				}
				Sentence nextsen=new Sentence(str.toString());
				
				//递归至下一级
				return matchPattern(nextsen,nextnode);
			}
			if(node.m_ChildNodes.containsKey("*"))
			{
				//新建node
				RuleNode nextnode=node.m_ChildNodes.get("*");
				//新建sentence
				StringBuilder str=new StringBuilder();
				for(int i=1;i<sentence.m_Splits.length;i++)
				{
					str.append(sentence.m_Splits[i]+" ");
				}
				Sentence nextsen=new Sentence(str.toString());
				
				//递归至下一级
				return matchWildCard(nextsen,nextnode,first);
			}
		}
		return null;
	}
	
	protected void addCategray(RuleNode node,Sentence sentence,Categray categray)
	{
		//如果是叶子节点
		if(sentence.m_Splits.length==0)
		{
			RuleNode leafnode=new RuleNode("#LEAF#",categray);
			node.m_ChildNodes.put("#LEAF#",leafnode);
			return;
		}
		String first=sentence.m_Splits[0];
		
		//如果该节点存在
		if(node.m_ChildNodes.containsKey(first))
		{
			//找到该节点
			RuleNode childnode=node.m_ChildNodes.get(first);
			//新建一个sentence
			StringBuilder str=new StringBuilder();
			for(int i=1;i<sentence.m_Splits.length;i++)
			{
				str.append(sentence.m_Splits[i]+" ");
			}
			Sentence nextsen=new Sentence(str.toString());		
			//递归
			addCategray(childnode,nextsen,categray);
			return;
		}
		else
		{
			//若该节点不存在，则创建一个
			RuleNode childnode=new RuleNode(first,null);
			node.m_ChildNodes.put(first,childnode);
			//新建一个sentence，去掉第0个
			StringBuilder str=new StringBuilder();
			for(int i=1;i<sentence.m_Splits.length;i++)
			{
				str.append(sentence.m_Splits[i]+" ");
			}
			Sentence nextsen=new Sentence(str.toString());		
			//递归
			addCategray(childnode,nextsen,categray);
			return;
		}
	}
	
	public void importXML(String filepath,CWSTagger tag)
	{
		 if(tag==null) 
			 return;
		 
	     try {
	    	 
	    	 SAXReader reader = new SAXReader();
	    	 Document document = reader.read(new File(filepath));
	    	 Element root=document.getRootElement();
	    	 for( Iterator i = root.elementIterator("Categray"); i.hasNext();)
	    	 {
	    		 Element catele= (Element) i.next();
	    		 Element pat=catele.element("Pattern");
	    		 Element api=catele.element("API");
	    		 Categray categray=new Categray(pat.getText(),api.getText());
	    		 for( Iterator it = catele.elementIterator("Template"); it.hasNext();)
	    		 {
	    			 Element tem=(Element) it.next();
	    			 categray.addTemplate(tem.getText());
	    		 }
	    		 
	    		 String str=catele.element("Pattern").getText();
	    		//分词
	    		 String s = tag.tag(str);
	    		 Sentence sentence=new Sentence(s);
	    		 //添加
	    		 addCategray(m_Root,sentence,categray);
	    	 }
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

package com.imhere.restapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;



@Path("/intelbot")
public class IntelBotResource {
	@Path("/response")
	@POST
	@Produces({"application/xml"})
	@Consumes({"application/xml"})
	public String getResponse(String message)
	{
		
		try {
			
			//解析收到的XML
			Document doc = DocumentHelper.parseText(message);
			Element root=doc.getRootElement();
			Element input=root.element("input");
			Element id=root.element("id");
			
			//调用intelbot，得到答复
			com.imhere.herebot.Response res=
					IntelBotUtil.getBot().response(input.getText(), Integer.parseInt(id.getText()));   
			
			//建立返回的XML
			Document docre=DocumentHelper.createDocument();
			Element rootre=docre.addElement("Response");
			//str
			Element str=rootre.addElement("str");
			str.setText(res.m_Response);
			//api
			Element api=rootre.addElement("api");
			api.setText(res.m_URI);
			//id
			Element idre=rootre.addElement("id");
			Integer iid=res.m_ObjectID;
			idre.setText(iid.toString());
			
			if(!res.m_Response.equals(""))
				System.out.println(res.m_Response);
			else
				System.out.println("这句话真心听不懂了......");
			
			return docre.asXML();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "<end>ere</end>";
	}
}

package com.imhere.restapi;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.imhere.persistence.Response;


@Path("/bbs/responses")
public class ResponseResource {
	@Path("{rid}")
	@Produces({"application/xml"})
	@GET
	public String getResponse(@PathParam("rid") int rid)
	{
		Session session=HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		Response res=(Response)session.get(Response.class, rid);	
		tx.commit();
		session.close();
		if(res!=null)
			return res.exportXML();	
		Document doce=DocumentHelper.createDocument();
		Element roote =doce.addElement("error");
		roote.addAttribute("type","response_id not found");
		return doce.asXML();
	}
	
	@Produces({"text/plain"})
	@Consumes({"application/xml"})
	@POST
	public String getResponse(String representation)
	{
		Session session=HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		Document doc;
		try {
			doc = DocumentHelper.parseText(representation);
			Element response = doc.getRootElement();
			int user_id=Integer.parseInt(response.element("user_id").getText());
			int topic_id=Integer.parseInt(response.element("topic_id").getText());
			Response res=new Response(response.element("content").getText(),user_id,topic_id);
			session.save(res);
			tx.commit();
			session.close();
			return "OK";	
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error";
		
	}

}

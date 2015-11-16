package com.imhere.restapi;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class HibernateListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
		 HibernateUtil.getSessionFactory().close();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		HibernateUtil.getSessionFactory(); 
	}

}

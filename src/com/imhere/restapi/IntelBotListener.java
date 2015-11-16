package com.imhere.restapi;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class IntelBotListener  implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
		 //HibernateUtil.getSessionFactory().close();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		IntelBotUtil.getBot();
	}
}

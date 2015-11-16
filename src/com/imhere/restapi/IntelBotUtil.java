package com.imhere.restapi;

import com.imhere.herebot.IntelBot;



public class IntelBotUtil {
	private static final IntelBot m_IntelBot;
	 
    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
        	m_IntelBot = new IntelBot();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial IntelBot creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
 
    public static IntelBot getBot() {
        return m_IntelBot;
    }

}

package com.practice;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {
	
	private static int sessionCount = 0;

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		synchronized (this)
		{
			sessionCount ++;
		}
		System.out.println("Session Created: " + arg0.getSession().getId());
		System.out.println("Total Sessions: " + sessionCount);
	}

	public static int getSessionCount() {
		return sessionCount;
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		synchronized(this)
		{
			sessionCount --;
		}
		System.out.println("Session Destroyed: " + arg0.getSession().getId());
		System.out.println("Total Sessions: " + sessionCount);
	}
	
	
}

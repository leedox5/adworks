package com.practice;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import mobis.common.base.LangMap;

public class MyContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("Listener 종료");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ServletContext ctx = arg0.getServletContext();
		
		Book mybook = new Book("BOOK1", "김태용", "2010");
		McamsUtil util = new McamsUtil();
		LangMap L = null;
		try {
			L = util.setLangMapToApplication1("LABL_KO");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ctx.setAttribute("book", mybook);
		ctx.setAttribute("L", L);
		System.out.println("Listener 초기화");
	}
}

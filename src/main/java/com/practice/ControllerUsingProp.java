package com.practice;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ControllerUsingProp
 */
@WebServlet(
		urlPatterns = { "/ControllerUsingProp" }, 
		initParams = { 
				@WebInitParam(name = "configProp", value = "/WEB-INF/Ex.properties")
		})

public class ControllerUsingProp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Map<String, Object> commandHandlerMap = new HashMap<String, Object>();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControllerUsingProp() {
        super();
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		String conf = config.getInitParameter("configProp");
		
		System.out.println(conf);
		
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = config.getServletContext().getResourceAsStream(conf);
			prop.load(input);
		} catch (IOException e) {
			throw new ServletException(e);
		} finally {
			if(input!=null)
			{
				try {
					input.close();
				} catch (IOException ex) {
					
				}
				
			}
		}
		
		Iterator<?> keyIter = prop.keySet().iterator();
		
		while (keyIter.hasNext()) {
			String command = (String) keyIter.next();
			String handlerClassName = prop.getProperty(command);
			try {
				Class<?> handlerClass = Class.forName(handlerClassName);
				Object handlerInstance = handlerClass.newInstance();
				commandHandlerMap.put(command, handlerInstance);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	private void process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String command = request.getParameter("cmd");
		CommandHandler handler = (CommandHandler) commandHandlerMap.get(command);
		
		System.out.println(command);
		
		if(handler == null) {
			handler = new NullHandler();
		}
		String view = null;
		try {
			view = handler.process(request, response);
		} catch (Throwable e) {
			throw new ServletException(e);
		}
		
		RequestDispatcher dispather = request.getRequestDispatcher(view);
		dispather.forward(request, response);
	}
}

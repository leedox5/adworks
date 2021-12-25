package com.practice;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloHandler implements CommandHandler {

	@Override
	public String process(HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		Cookie cookie1 = new Cookie("XYZ", "ZYX");
		response.addCookie(cookie1);
		
		request.setAttribute("hello", "This is hello handler");
		return "hello.jsp";
	}

}

package com.practice;

import org.apache.log4j.Logger;

public class DemoLog4j {

	private static Logger logger = Logger.getLogger(DemoLog4j.class);

	public static void main(String[] args) {
		logger.trace("BasicBinder:83 - binding parameter [1] as [VARCHAR] - 20130425");
		logger.info("parameter BasicExtractor:72 - Found [1957] as column [LTEM1_0_]");
	}
}

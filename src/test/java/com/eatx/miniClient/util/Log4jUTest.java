package com.eatx.miniClient.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

public class Log4jUTest {

	private static final Logger logger = LogManager.getLogger(Log4jUTest.class.getName());
	
	/**
	 * debug < info < warn < error < fatal
	 * 
	 * the right hand side is highest level
	 */
	@Test
	public void test(){
		
		logger.trace("trace");
		logger.debug("debug");
//		logger.debug("debug", new NullPointerException());
		
		System.out.println("LOG.isDebugEnabled():"+logger.isDebugEnabled());
		
//		logger.info("info", new NullPointerException());
		logger.warn("warn");
		logger.error("error");
//		System.out.println("logger.ROOT_LOGGER_NAME: "+logger.ROOT_LOGGER_NAME);
	}
}

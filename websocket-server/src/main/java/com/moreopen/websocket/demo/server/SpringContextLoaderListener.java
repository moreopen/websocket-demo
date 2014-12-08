/*
 * Copyright 2011 y.sdo.com, Inc. All rights reserved.
 * y.sdo.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.moreopen.websocket.demo.server;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringContextLoaderListener extends ContextLoaderListener {
	
	private static final Logger logger = LoggerFactory.getLogger(SpringContextLoaderListener.class);
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		com.moreopen.websocket.demo.BeanLocator.setApplicationContext(applicationContext);
		logger.info("============ Initialize application context, done. " + applicationContext);
	}
}

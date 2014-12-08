package com.moreopen.websocket.demo.server.spi.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.moreopen.websocket.demo.server.packet.Message;
import com.moreopen.websocket.demo.server.spi.OfflineService;

@Component("LazyOfflineService")
public class LazyOfflineService implements OfflineService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void save(Message message) {
		if (logger.isInfoEnabled()) {
			logger.info("I am lazy, I don't like to save offline message, hahaha...");
		}
	}

}

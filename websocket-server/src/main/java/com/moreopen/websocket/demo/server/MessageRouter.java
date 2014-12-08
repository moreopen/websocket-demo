package com.moreopen.websocket.demo.server;

import java.util.Collection;

import javax.annotation.Resource;

import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.moreopen.websocket.demo.server.packet.Message;
import com.moreopen.websocket.demo.server.spi.OfflineService;


@Component("MessageRouter")
public class MessageRouter {
	
	private Logger logger = LoggerFactory.getLogger(MessageRouter.class);
	
	@Resource
	private SessionManager sessionManager;
	
	@Resource
	private OfflineService offlineService;

	/**
	 * broadcast message to all other sessions (exclude message sender)
	 * @param session : sender
	 * @param message : message content
	 */
	public void broadcast(Session session, String message) {
		try {
			Collection<WebClientSession> sessions = sessionManager.all();
			for (WebClientSession each : sessions) {
				if (each != session) {
					each.deliverString(message + " (From : " + session.getRemoteAddress() + ")");
				}
			}
		} catch (Exception e) {
			logger.error("broadcast message failed", e);
		}
	}

	public void route(Message message) {
		String to = message.getTo();
		WebClientSession clientSession = null;
		try {
			clientSession = sessionManager.getClientSession(to);
		} catch (IllegalJidException e) {
			logger.error(String.format("illegal jid, to [%s]", to), e);
			return;
		}
		if (clientSession == null) {
			logger.warn(String.format("can't find client session, jid [%s]", to));
			offlineService.save(message);
			return;
		}
		if (clientSession.isOffine()) {
			logger.warn(String.format("[%s] is offline", to));
			offlineService.save(message);
			return;
		}
		clientSession.deliver(PacketProtocol.message, message);
	}

}

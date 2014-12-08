package com.moreopen.websocket.demo.server.handler;

import java.io.IOException;

import javax.annotation.Resource;

import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.moreopen.websocket.demo.server.MessageRouter;
import com.moreopen.websocket.demo.server.PacketProtocol;
import com.moreopen.websocket.demo.server.SessionManager;
import com.moreopen.websocket.demo.server.WebClientSession;
import com.moreopen.websocket.demo.server.packet.BaseResponse;
import com.moreopen.websocket.demo.server.packet.Message;
import com.moreopen.websocket.demo.server.reply.IncomingMessageReplyProcessor;
import com.moreopen.websocket.demo.util.JsonUtils;

@Component("MessageHandler")
public class MessageHandler implements PacketHandler {
	
	@Resource
	private SessionManager sessionManager;
	
	@Resource
	private MessageRouter messageRouter;
	
	@Resource
	private IncomingMessageReplyProcessor replyProcessor;
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void handle(Session session, String body) throws IOException {
		WebClientSession clientSession = sessionManager.getClientSession(session);
		if (clientSession == null) {
			logger.error("current session is not binded, close it");
			session.close();
			return;
		}
		Message message = null;
		try {
			message = JsonUtils.json2Bean(body, Message.class);
		} catch (Exception e) {
			logger.error("unformatted json", e);
			BaseResponse response = BaseResponse.UNFORMATTED;
			clientSession.deliver(PacketProtocol.message, response);
			return;
		}
		beforeProcess(clientSession, message);
		replyProcessor.process(clientSession, message);
		messageRouter.route(message);
	}

	//reset message from avoid be faked
	private void beforeProcess(WebClientSession clientSession, Message message) {
		String from = clientSession.getJid();
		message.setFrom(from);
	}

}

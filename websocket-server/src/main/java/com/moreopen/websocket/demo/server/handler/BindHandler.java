package com.moreopen.websocket.demo.server.handler;

import java.io.IOException;

import javax.annotation.Resource;

import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.moreopen.websocket.demo.server.PacketProtocol;
import com.moreopen.websocket.demo.server.SessionManager;
import com.moreopen.websocket.demo.server.WebClientSession;
import com.moreopen.websocket.demo.server.packet.BaseResponse;
import com.moreopen.websocket.demo.server.packet.BindRequest;
import com.moreopen.websocket.demo.server.packet.BindResponse;
import com.moreopen.websocket.demo.server.packet.Packet;
import com.moreopen.websocket.demo.util.JsonUtils;

@Component("BindHandler")
public class BindHandler implements PacketHandler {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private SessionManager sessionManager;

	@Override
	public void handle(Session session, String message) throws IOException {
		BindRequest bindRequest = null;
		try {
			bindRequest = JsonUtils.json2Bean(message, BindRequest.class);
		} catch (Exception e) {
			logger.error("unformatted json", e);
			BaseResponse response = BaseResponse.UNFORMATTED;
			try {
				Packet packet = new Packet(PacketProtocol.bind.getCode(), JsonUtils.bean2Json(response));
				session.getRemote().sendString(JsonUtils.bean2Json(packet));
			} catch (Exception e1) {
				logger.error("deliver failed", e1);
			}
			return;
		}
		WebClientSession clientSession = sessionManager.getClientSession(session);
		//already binded, please update
		if (clientSession != null) {
			if (logger.isInfoEnabled()) {
				logger.info(String.format("session already binded to node [%s], nick [%s], update to node [%s], nick [%s]", 
						clientSession.getNode(), clientSession.getNickName(),
						bindRequest.getNickName(), bindRequest.getNickName())
				);
			}
			update(clientSession, bindRequest);
		} else {
			clientSession = doBind(session, bindRequest);
			if (logger.isInfoEnabled()) {
				logger.info(String.format("session bind to node [%s], nick [%s], remote [%s]", 
								clientSession.getNode(), clientSession.getNickName(), session.getRemoteAddress()
					)
				);
			}
		}
		BindResponse response = new BindResponse(clientSession.getNickName());
		clientSession.deliver(PacketProtocol.bind, response);
	}

	private void update(WebClientSession clientSession, BindRequest bindRequest) {
		clientSession.setPlatform(bindRequest.getPlatform());
		clientSession.setVersion(bindRequest.getVersion());
		clientSession.setNickName(bindRequest.getNickName());
		//TODO XXX just use nick name as node, in fact this node should be set when authed before bind
		clientSession.setNode(bindRequest.getNickName());
	}

	private WebClientSession doBind(Session session, BindRequest bindRequest) {
		WebClientSession clientSession = new WebClientSession();
		update(clientSession, bindRequest);
		clientSession.init(session);
		sessionManager.add(clientSession);
		return clientSession;
	}

}

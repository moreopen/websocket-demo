package com.moreopen.websocket.demo.server.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.moreopen.websocket.demo.server.PacketProtocol;
import com.moreopen.websocket.demo.server.packet.Packet;

@Component("PacketHandlers")
public class PacketHandlers implements InitializingBean {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private Map<PacketProtocol, PacketHandler> handlers = new HashMap<PacketProtocol, PacketHandler>();
	
	@Resource
	private MessageHandler messageHandler;
	
	@Resource
	private BindHandler bindHandler;

	public void handle(Session session, Packet packet) throws IOException {
		PacketProtocol protocol = PacketProtocol.resolve(packet);
		PacketHandler packetHandler = handlers.get(protocol);
		if (packetHandler == null) {
			logger.error(String.format("can't find PacketHandler for packet [%s, %s]", packet.getType(), protocol.getSymbol()));
			return;
		}
		packetHandler.handle(session, packet.getPayload());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		handlers.put(PacketProtocol.message, messageHandler);
		handlers.put(PacketProtocol.bind, bindHandler);
	}

}

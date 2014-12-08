package com.moreopen.websocket.demo.server.handler;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;

public interface PacketHandler {

	void handle(Session session, String body) throws IOException;

}

package com.moreopen.websocket.demo.server;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.moreopen.websocket.demo.BeanLocator;
import com.moreopen.websocket.demo.server.handler.PacketHandlers;
import com.moreopen.websocket.demo.server.packet.Packet;
import com.moreopen.websocket.demo.util.JsonUtils;


@WebSocket
public class MyWebSocketHandler {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private SessionManager sessionManager;
	
	private PacketHandlers packetHandlers;
	
	public MyWebSocketHandler() {
		sessionManager = BeanLocator.getBean("SessionManager");
		packetHandlers = BeanLocator.getBean("PacketHandlers");
		Assert.notNull(sessionManager);
		Assert.notNull(packetHandlers);
	}

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        System.out.println("========= Close: statusCode=" + statusCode + ", reason=" + reason);
        sessionManager.remove(session);
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        System.out.println("========== Error: " + t.getMessage());
    }

    /*
     * XXX can auth ticket here, if not valid, close session, otherwise waiting binding (ticket can get from a common http server)
     */
    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("========== Connect: " + session.getRemoteAddress().getAddress());
        System.out.println("========== query string: " + session.getUpgradeRequest().getQueryString());
        try {
            session.getRemote().sendString("You connected to me. What can I do for you?");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * all packet format : 
     * {
     *   "type":"xxx",
     *   "payload":{ //different type has different payload
     *   	...
     *   }
     * }
     */
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.println("received : " + message);
//        session.getRemote().sendString("Hello " + packet);
        Packet packet;
		try {
			packet = JsonUtils.json2Bean(message, Packet.class);
		} catch (Exception e) {
			logger.error(String.format("unformatted json message [%s]", message));
			throw new IOException(e);
		}
        packetHandlers.handle(session, packet);
    }
    
}

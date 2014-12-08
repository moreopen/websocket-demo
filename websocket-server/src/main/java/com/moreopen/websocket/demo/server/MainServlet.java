package com.moreopen.websocket.demo.server;

import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 * @deprecated
 * use another way to open web socket service, {@see WebSocketServerLauncher}
 */
public class MainServlet extends org.eclipse.jetty.websocket.servlet.WebSocketServlet {

	private static final long serialVersionUID = 4075855152610327740L;

	@Override
	public void configure(WebSocketServletFactory arg0) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not yet implemented");
	}

	

}

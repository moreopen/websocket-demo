package com.moreopen.websocket.demo.client;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class WebSocketClientLauncher {
	
	private static  Logger logger = Logger.getLogger(WebSocketClientLauncher.class);

	private static Server server = null;
	private int port;

	public WebSocketClientLauncher(int port) {
		this.port = port;
	}

	public void run() throws Exception {
		if (server != null) {
			return;
		}
		server = new Server(this.port);
		server.setStopAtShutdown(true);
		server.setHandler(getWebAppContext());
		server.start();
		logger.info("Start icollector server, done.port: "+ this.port);
	}

	public void stop() throws Exception {
		if (server != null) {
			server.stop();
			server = null;
		}
	}

	public void setPort(int port) {
		this.port = port;
	}

	private WebAppContext getWebAppContext() {

		String path = WebSocketClientLauncher.class.getResource("/").getFile().replaceAll("/target/(.*)", "")
				+ "/src/main/webapp";

		return new WebAppContext(path, "/");
	}

	public static void main(String[] args) throws Exception {
		// launch the monitor console server
		new WebSocketClientLauncher(8081).run();
		server.join();
	}


}

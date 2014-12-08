package com.moreopen.websocket.demo.server;

import java.util.TimeZone;

import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WebSocketServerLauncher {
	
	private static Logger logger = LoggerFactory.getLogger(WebSocketServerLauncher.class);
	
//	private static ClassPathXmlApplicationContext context;
	
	//5 minutes
	private static final int IDLE_TIMEOUT = 5*60*1000;
	
	public static void main(String[] args) throws Exception {
//		initApplicationContext();
		final Server wsServer = launchWebSocketServer();
		final Server server = launchHttpServer();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					if (server != null) {
						server.stop();
						logger.info("stopped web server");
					}
					if (wsServer != null) {
						wsServer.stop();
						logger.info("stopped web socket server");
					}
				} catch (Exception e) {
				}
			}		
		});
	}

	private static Server launchHttpServer() throws Exception {
		int webPort = 8084;
		Server server = new Server(webPort);
		server.setStopAtShutdown(true);
		HandlerCollection handlers = new HandlerCollection();
		handlers.addHandler(getWebAppContext());
		handlers.addHandler(getRequestLogHandler());
		server.setHandler(handlers);
		server.start();
		logger.info("Start web server, done.port: "+ webPort);
		return server;
	}
	
	protected static RequestLogHandler getRequestLogHandler() {
		RequestLogHandler logHandler = new RequestLogHandler();
		NCSARequestLog requestLog = new NCSARequestLog("jetty-yyyy_MM_dd.access.log");
		requestLog.setAppend(true);
		requestLog.setLogServer(true);
		requestLog.setExtended(false);
		requestLog.setLogTimeZone(TimeZone.getDefault().getID());
		requestLog.setLogLatency(true);
		requestLog.setLogDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
		logHandler.setRequestLog(requestLog);
		return logHandler;
	}

	private static WebAppContext getWebAppContext() {
		String path = WebSocketServerLauncher.class.getResource("/").getFile().replaceAll("/target/(.*)", "")	+ "/src/main/webapp";
		return new WebAppContext(path, "/");
	}

	private static Server launchWebSocketServer() throws Exception {
		int wsPort = 8083;
		Server server = new Server(wsPort);
		
//		HandlerCollection handlers = new HandlerCollection();
//		handlers.addHandler(getWebAppContext());
//		handlers.addHandler(getRequestLogHandler());
		
        WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory factory) {
				factory.getPolicy().setIdleTimeout(IDLE_TIMEOUT);
                factory.register(MyWebSocketHandler.class);
            }
        };
//        handlers.addHandler(wsHandler);
//        server.setHandler(handlers);
        server.setHandler(wsHandler);
        server.start();
        logger.info("Start Web Socket server, done.port: " + wsPort);
        return server;
	}

//	private static void initApplicationContext() {
//		context = new ClassPathXmlApplicationContext("/allinone.context.xml");
//		context.registerShutdownHook();		
//		context.start();
//		BeanLocator.setApplicationContext(context);
//	}

}

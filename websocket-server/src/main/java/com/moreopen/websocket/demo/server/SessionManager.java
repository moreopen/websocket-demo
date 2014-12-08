package com.moreopen.websocket.demo.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.math.RandomUtils;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.moreopen.websocket.demo.server.core.JidObject;

@Component("SessionManager")
public class SessionManager implements InitializingBean, ApplicationContextAware {
	
	private static final int MAX_RANDOM_SIZE = 100;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private Map<Session, WebClientSession> sessionMap = new HashMap<Session, WebClientSession>();
	
	/**
	 * key : node
	 * value : list of current session
	 */
	private Map<String, List<WebClientSession>> node2Session = new HashMap<String, List<WebClientSession>>(); 
	
	/**
	 * all client session list, refresh period
	 */
	private List<WebClientSession> sessions = new ArrayList<WebClientSession>();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("========= Initializing SessionManager: " + this);
		} 
		Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				sessions = new ArrayList<WebClientSession>(sessionMap.values());
			}
		}, 1, 10, TimeUnit.SECONDS);
	}
	
	public Collection<WebClientSession> all() {
		return sessionMap.values();
	}

	public void add(final WebClientSession clientSession) {
		
		sessionMap.put(clientSession.getSession(), clientSession);
		String node = clientSession.getNode();
		List<WebClientSession> sessions = node2Session.get(node);
		if (sessions == null) {
			synchronized (node.intern()) {
				if ((sessions = node2Session.get(node)) == null) { 
					sessions = new ArrayList<WebClientSession>();
					node2Session.put(node, sessions);
				}
			}
		} 
		//check if exit the session with same platform or not
		WebClientSession _session = (WebClientSession) CollectionUtils.find(sessions, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				return ((BaseClientSession) object).getPlatform().equals(clientSession.getPlatform());
			}
		});
		if (_session != null) {
			kickSession(sessions, _session);
		}
		sessions.add(clientSession);
	}
	
	private void kickSession(List<WebClientSession> sessions, WebClientSession clientSession) {
		//XXX TODO send kick packet to client session
		logger.warn(String.format("kicked pre client session [%s] for multi logon with same node and platform", clientSession.toString()));
		//XXX close session
		if (clientSession.getSession() != null) {
			clientSession.getSession().close(); //close will trigger remove
		} else {
			removeClientSession(clientSession);
		}
	}

	public WebClientSession getClientSession(Session session) {
		return sessionMap.get(session);
	}

	//XXX now just support one. If support multi clients for one node, should be processed by Router 
	public WebClientSession getClientSession(String jid) throws IllegalJidException {
		JidObject jidObject = JidObject.newInstance(jid);
		List<WebClientSession> sessions = node2Session.get(jidObject.getNode());
		return CollectionUtils.isEmpty(sessions) ? null : sessions.get(0);
	}

	public void remove(Session session) {
		WebClientSession removedClientSession = sessionMap.remove(session);
		if (removedClientSession != null) {
			removeClientSession(removedClientSession); 
		}
	}

	private void removeClientSession(WebClientSession removedClientSession) {
		List<WebClientSession> sessions = node2Session.get(removedClientSession.getNode());
		if (CollectionUtils.isNotEmpty(sessions)) {
			synchronized (removedClientSession.getNode().intern()) {
				sessions.remove(removedClientSession);
				if (logger.isInfoEnabled()) {
					logger.info(String.format("removed client session, jid [%s]", removedClientSession.getJid()));
				}
				if (sessions.isEmpty()) {
					node2Session.remove(removedClientSession.getNode());
				}
			}
		}
	}
	
	/**
	 * random get some sessions
	 * @param size : required random size
	 * XXX attention : sessions is not thread safe, so the size of sessions will changed in execution of randomGetSessions
	 */
	public List<WebClientSession> randomGetSessions(int size) {
		
		if (size > MAX_RANDOM_SIZE) {
			size = MAX_RANDOM_SIZE;
		}
		
		int allSize = sessions.size();
		if (allSize <= size) {
			return sessions;
		}
		int offset = allSize - size;
		int start = RandomUtils.nextInt(offset + 1);
		List<WebClientSession> result = new ArrayList<WebClientSession>(size);
		for (int index = start; index < start + size; index++) {
			WebClientSession session = null;
			try {
				session = sessions.get(index);
			} catch (ArrayIndexOutOfBoundsException e) {
				logger.error("index out of bounds of sessions, just break");
				break;
			}
			if (session != null) {
				result.add(session);
			}
		}
		return result;
		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		logger.info("========= current application context : " + applicationContext);
	}

}

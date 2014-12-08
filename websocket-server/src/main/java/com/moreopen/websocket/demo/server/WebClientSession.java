package com.moreopen.websocket.demo.server;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moreopen.websocket.demo.server.packet.Packet;
import com.moreopen.websocket.demo.server.packet.Payload;
import com.moreopen.websocket.demo.util.JsonUtils;

public class WebClientSession extends BaseClientSession {
	
	private static final long TRANSMITTED_TIME_OUT = 5;

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private String version;

	private Session session;
	
	public WebClientSession() {
		super();
	}
	
	public WebClientSession(String node, String nickName, String platform) {
		super(node, nickName, platform);
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void init(Session session) {
		this.session = session;
	}
	
	public Session getSession() {
		return session;
	}

	public String getJid() {
		return node + "@" + Constants.DOMAIN + "/" + platform;
	}

	public boolean isOffine() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void deliver(PacketProtocol packetProtocol, Payload payload) {
		if (payload == null) {
			logger.warn("payload can't be null");
			return;
		}
		String json = null;
		try {
			json = payload.toJson();
		} catch (Exception e) {
			logger.error(String.format("convert payload to json failed, payload [%s]", payload.toString()));
			return;
		}
		Packet packet = new Packet(packetProtocol.getCode(), json);
		String packetJson;
		try {
			packetJson = JsonUtils.bean2Json(packet);
		} catch (Exception e) {
			logger.error(String.format("convert packet to json failed, packet [%s]", packet.toString()));
			return;
		}
		deliverString(packetJson);
	}

	/**
	 *  use Future avoid blocking : java.lang.IllegalStateException: Blocking message pending 10000 for BLOCKING
	 *  maybe use lock or queue to avoid ...
	 */
	public void deliverString(String body) {
		try {
//			session.getRemote().sendString(body);
			Future<Void> future = session.getRemote().sendStringByFuture(body);
			future.get(TRANSMITTED_TIME_OUT, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			logger.error(String.format("deliver body [%s] failed", body), e);
		} finally {
		}
	}
	
	@Override
	public String toString() {
		return String.format("WebClientSession[node:%s, platform:%s, nickName:%s, version:%s, remote:%s]",
						node, platform, nickName, version,
						session != null ? session.getRemoteAddress() : null);
	}

}

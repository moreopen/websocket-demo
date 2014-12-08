package com.moreopen.websocket.demo.server;

public class BaseClientSession {

	protected String node;
	
	protected String platform;
	
	protected String nickName;
	
	public BaseClientSession() {}

	public BaseClientSession(String node, String nickName, String platform) {
		this.node = node;
		this.nickName = nickName;
		this.platform = platform;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

}

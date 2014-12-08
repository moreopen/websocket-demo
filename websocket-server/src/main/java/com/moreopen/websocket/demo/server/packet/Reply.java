package com.moreopen.websocket.demo.server.packet;

public class Reply extends Payload {
	
	private String id;
	
	private String code;
	
	private String to;
	
	private String from;

	public void setId(String id) {
		this.id = id;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getTo() {
		return to;
	}

	public String getFrom() {
		return from;
	}

}

package com.moreopen.websocket.demo.server.packet;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.moreopen.websocket.demo.util.JsonUtils;

public class Message extends Payload {
	
	private String id;
	
	private String from;
	
	private String to;
	
	private String body;

	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getFrom() {
		return this.from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return this.to;
	}
	
	public void setTo(String to) {
		this.to = to;
	}
	
	public static void main(String[] args) throws Exception {
		String json = "{\"id\":\"333\", \"from\":\"xxx\", \"to\":\"toxxx\"}";
		Message message = JsonUtils.json2Bean(json, Message.class);
		System.out.println(ToStringBuilder.reflectionToString(message));
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}

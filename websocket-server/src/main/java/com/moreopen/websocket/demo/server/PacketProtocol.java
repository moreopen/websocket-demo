package com.moreopen.websocket.demo.server;

import com.moreopen.websocket.demo.server.packet.Packet;

public enum PacketProtocol {
	
	auth("0", "auth"),
	bind("1", "bind"),
	message("2", "message"), 
	reply("3", "reply");
	
	private String code;
	
	private String symbol;
	
	private PacketProtocol(String code, String symbol) {
		this.code = code;
		this.symbol = symbol;
	}

	public String getCode() {
		return code;
	}

	public String getSymbol() {
		return symbol;
	}
	
	public static PacketProtocol resolve(Packet packet) {
		for (PacketProtocol protocol : values()) {
			if (protocol.getCode().equals(packet.getType())) {
				return protocol;
			}
		}
		throw new IllegalArgumentException("unsupported or invalid packet, type : " + packet.getType());
	}
}

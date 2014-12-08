package com.moreopen.websocket.demo.server.packet;

public class BindResponse extends BaseResponse {
	
	private String nickName;

	public BindResponse(String nickName) {
		this.nickName = nickName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

}

package com.moreopen.websocket.demo.server.packet;

public class BaseResponse extends Payload {
	
	public static final String OK = "200";
	
	public static final BaseResponse UNFORMATTED = new BaseResponse("401", "un-formatted input"); 
	
	//返回码, 200 表示成功
	private String code = OK;
	
	//返回信息
	private String message;
	
	public BaseResponse() {
	}
	
	public BaseResponse(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}

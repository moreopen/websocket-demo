package com.moreopen.websocket.demo.server;

/**
 * 非法 Jid 异常
 */
public class IllegalJidException extends Exception {
	
	private static final long serialVersionUID = 2307998794119926083L;

	public IllegalJidException() {
		super();
	}
	
	public IllegalJidException(String msg) {
		super(msg);
	}
	
	public IllegalJidException(String msg, Throwable t) {
		super(msg, t);
	}

}

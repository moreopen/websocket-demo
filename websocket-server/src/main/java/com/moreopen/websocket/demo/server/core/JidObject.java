package com.moreopen.websocket.demo.server.core;

import com.moreopen.websocket.demo.server.IllegalJidException;

public class JidObject {
	
	public static final String AT = "@";
	
	public static final String SLASH = "/";
	
	private String node;
	
	private String domain;
	
	private String resource;
	
	public JidObject() {}
	
	public JidObject(String node, String domain, String resource) {
		this.node = node;
		this.domain = domain;
		this.resource = resource;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public static JidObject newInstance(String jid) throws IllegalJidException {
		int pos1 = jid.indexOf(AT);
		if (pos1 == -1) {
			throw new IllegalJidException(AT + " is required");
		}
		String node = jid.substring(0, pos1);
		String domain = null;
		String resource = null;
		String left = jid.substring(pos1 + 1);
		int pos2 = left.indexOf(SLASH);
		if (pos2 == -1) {
			domain = left;
		} else {
			domain = left.substring(0, pos2);
			resource = left.substring(pos2+1);
		}
		return new JidObject(node, domain, resource);
	}

}

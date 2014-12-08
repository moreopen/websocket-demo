package com.moreopen.websocket.demo.server.packet;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.moreopen.websocket.demo.util.JsonUtils;

public class Payload {
	
	public String toJson() throws Exception {
		return JsonUtils.bean2Json(this);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
	}

}

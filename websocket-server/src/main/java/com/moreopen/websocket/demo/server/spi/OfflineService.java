package com.moreopen.websocket.demo.server.spi;

import com.moreopen.websocket.demo.server.packet.Message;

public interface OfflineService {

	void save(Message message);

}

package com.moreopen.websocket.demo.server.reply;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import com.moreopen.websocket.demo.server.BaseClientSession;
import com.moreopen.websocket.demo.server.Constants;
import com.moreopen.websocket.demo.server.PacketProtocol;
import com.moreopen.websocket.demo.server.WebClientSession;
import com.moreopen.websocket.demo.server.packet.Message;
import com.moreopen.websocket.demo.server.packet.Reply;

/**
 * 服务端返回给客户端（发送方）的消息回执处理器
 */
@Component
public class IncomingMessageReplyProcessor {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private ExecutorService executorService;
	
	@PostConstruct
	public void init() {
		executorService = Executors.newFixedThreadPool(
			Runtime.getRuntime().availableProcessors() + 1, 
			new CustomizableThreadFactory("T-Reply-Processor-")
		);
	}

	public void process(final WebClientSession clientSession, final Message message) {
		
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Reply reply = buildReply(message, clientSession);					
					clientSession.deliver(PacketProtocol.reply, reply);
				} catch (Exception e) {
					logger.warn("process reply failed", e);
				}
			}
		});
		
	}

	private Reply buildReply(Message message, BaseClientSession clientSession) {
		Reply reply = new Reply();
		reply.setId(message.getId());
		reply.setCode(Constants.ReplyCodes.SERVER_RECEIVED);
		reply.setFrom(Constants.DOMAIN);
		reply.setTo(message.getFrom());
		return reply;
	}

}

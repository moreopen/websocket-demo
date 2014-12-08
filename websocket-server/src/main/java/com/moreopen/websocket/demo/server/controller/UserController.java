package com.moreopen.websocket.demo.server.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.moreopen.websocket.demo.server.BaseClientSession;
import com.moreopen.websocket.demo.server.SessionManager;
import com.moreopen.websocket.demo.server.WebClientSession;
import com.moreopen.websocket.demo.util.JsonUtils;

@Controller
public class UserController {
	
	@Resource
	private SessionManager sessionManager;
	
	@RequestMapping("/user/list")
	public void randomList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		List<WebClientSession> randomSessions = sessionManager.randomGetSessions(100);
		List<BaseClientSession> list = new ArrayList<BaseClientSession>(randomSessions.size());
		for (WebClientSession session : randomSessions) {
			list.add(new BaseClientSession(session.getNode(), session.getNickName(), session.getPlatform()));
		}
		
		try {
			String bean2Json = JsonUtils.bean2Json(list);
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.getWriter().write(bean2Json);
		} catch (Exception e) {
		}
	}

}

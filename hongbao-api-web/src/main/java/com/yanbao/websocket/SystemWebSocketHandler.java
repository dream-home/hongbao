/* 
 * 文件名：SystemWebSocketHandler.java  
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：朱仲威  
 * 创建时间：2017年2月14日
 * 版本号：v1.0
 */
package com.yanbao.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.yanbao.constant.RedisKey;
import com.yanbao.core.model.Token;
import com.yanbao.redis.Hash;
import com.yanbao.redis.Sets;
import com.yanbao.redis.Strings;
import com.yanbao.service.MessageService;
import com.yanbao.util.TokenUtil;

/**
 * TODO
 * @version v1.0
 * @date 2017年2月14日
 */
@Component
public class SystemWebSocketHandler implements WebSocketHandler { 
	private static  Logger logger = (Logger) LoggerFactory.getLogger(SystemWebSocketHandler.class);
	@Autowired
	private MessageService messageService;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.debug("connect to the websocket success......");
		logger.debug("********************afterConnectionEstablished sessionid *********************" + session.getId() +"  ******************************************");
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		String tokens = (String) message.getPayload();
		int r=0;
		if (StringUtils.isEmpty(tokens) || tokens.indexOf("token=") < 0) {
			session.close();
		} else {
			
			String token = tokens.split("=")[1];
			logger.debug("*********************token********************" +  token+"  *****************************************");
			if (!Sets.sismember(RedisKey.ALL_TOKENS.getKey(), token)) {
				session.close();
			}
			Token t = (Token) TokenUtil.getTokenObject(token);
			messageService.countAllUnread(t);
			if (t==null) { 
				session.close();  
				return;
			}
			Hash.hset(RedisKey.SOCKET_SESSION_TOKENS.getKey(), session.getId(), t.getId());
		}
		System.out.println(message.getPayload());
		String userId = Hash.hget(RedisKey.SOCKET_SESSION_TOKENS.getKey(), session.getId());
		logger.debug("******************* handleMessage **********************" + session.getId() + "  &&&&&&&&   " + userId + "******************************************");
		if (StringUtils.isEmpty(userId)) {
			return;
		}
	    r = (String)  Hash.hget(RedisKey.ALL_IDS.getKey(), userId) == null ? 0 : Integer.valueOf(Hash.hget(RedisKey.ALL_IDS.getKey(), userId));
		
		session.sendMessage(new TextMessage(r + ""));
		/*
		 * if (Strings.get(RedisKey.START_SOCKET.getKey())) {
		 * 
		 * }
		 */
		while (true) {
			if (Sets.scard(RedisKey.WAITING_IDS.getKey()) > 0) {
				if (Sets.sismember(RedisKey.WAITING_IDS.getKey(), userId)) {
					int unread   = (String)  Hash.hget(RedisKey.ALL_IDS.getKey(), userId) == null ? 0 : Integer.valueOf(Hash.hget(RedisKey.ALL_IDS.getKey(), userId));
					session.sendMessage(new TextMessage(unread + ""));
					Sets.srem(RedisKey.WAITING_IDS.getKey(), userId);
					logger.debug("******************* unread **********************" + unread + "  &&&&&&&&  ****************************************** userId =" + userId + "******************************************");
				}

				/*
				 * Set<String> idSet =
				 * Sets.smembers(RedisKey.WAITING_IDS.getKey()); for (Iterator
				 * iterator = idSet.iterator(); iterator.hasNext();) {
				 * logger.debug(Sets.scard(RedisKey.WAITING_IDS.getKey())+"");
				 * String key = (String) iterator.next(); int unread = (String)
				 * Strings.get(key) == null ? 0 : Integer.valueOf((String)
				 * Strings.get(key)); session.sendMessage(new TextMessage(unread
				 * + "")); Sets.srem(RedisKey.WAITING_IDS.getKey(), key); }
				 */
			}
		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		logger.debug("handleTransportError......");
		session.close();
		logger.debug("*********************handleTransportError sessionid ********************" +  session.getId()+"  *****************************************");
		Hash.hdel(RedisKey.SOCKET_SESSION_TOKENS.getKey(), session.getId());
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		logger.debug("afterConnectionClosed...... " + closeStatus);
		session.close();
		logger.debug("*********************afterConnectionClosed sessionid ********************" +  session.getId()+"  *****************************************");
		Hash.hdel(RedisKey.SOCKET_SESSION_TOKENS.getKey(), session.getId());
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
	


}

package com.yanbao.websocket;

/* 
 * 文件名：HandShakeInterceptor.java  
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：朱仲威  
 * 创建时间：2017年2月13日
 * 版本号：v1.0
 */

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * TODO
 * 
 * @author zzwei
 * @version v1.0
 * @date 2017年2月13日
 */
public class WebSocketHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
		if (request instanceof ServletServerHttpRequest) {
//			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
		}
        /** 在拦截器内强行修改websocket协议，将部分浏览器不支持的 x-webkit-deflate-frame 扩展修改成 permessage-deflate */  
        if(request.getHeaders().containsKey("Sec-WebSocket-Extensions")){  
            request.getHeaders().set("Sec-WebSocket-Extensions", "permessage-deflate");  
        }  
        System.out.println("Before Handshake");    
        return super.beforeHandshake(request, response, wsHandler, attributes);   
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
	}
}

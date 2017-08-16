/* 
 * 文件名：WebSocketConfig.java  
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：朱仲威  
 * 创建时间：2017年2月14日
 * 版本号：v1.0
 */
package com.yanbao.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * TODO
 * 
 * @author zzwei
 * @version v1.0
 * @date 2017年2月14日
 */

/*@Component
@Configuration
@EnableWebMvc
@EnableWebSocket*/
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(systemWebSocketHandler(), "/webSocketServer").addInterceptors(new WebSocketHandshakeInterceptor());
		registry.addHandler(systemWebSocketHandler(), "/sockjs/webSocketServer").addInterceptors(new WebSocketHandshakeInterceptor()).withSockJS();
	}

	@Bean
	public SystemWebSocketHandler systemWebSocketHandler() {
		return new SystemWebSocketHandler();
	}

}

package com.mobiloitte.notification.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.mobiloitte.notification.handler.CustomWebSocketChatHandler;
import com.mobiloitte.notification.handler.CustomWebSocketChatHandlerNew;
import com.mobiloitte.notification.handler.CustomWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	@Autowired
	private CustomWebSocketHandler webSocketHandler;
	@Autowired
	private CustomWebSocketChatHandler customWebSocketChatHandler;
	@Autowired
	private CustomWebSocketChatHandlerNew customWebSocketChatHandlerNew;

	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

		registry.addHandler(webSocketHandler, "/notification").setAllowedOrigins("*")
				.addInterceptors(new HttpSessionHandshakeInterceptor());
		registry.addHandler(customWebSocketChatHandler, "/chat").setAllowedOrigins("*")
				.addInterceptors(new HttpSessionHandshakeInterceptor());
		registry.addHandler(customWebSocketChatHandlerNew, "/support").setAllowedOrigins("*")
				.addInterceptors(new HttpSessionHandshakeInterceptor());
	}

	@Bean
	public ServletServerContainerFactoryBean createWebSocketContainer() {
		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
		container.setMaxTextMessageBufferSize(8192);
		container.setMaxBinaryMessageBufferSize(8192);
		container.setAsyncSendTimeout((long) 600000);
		container.setMaxSessionIdleTimeout((long) 600000);
		return container;
	}
//	@Bean
//    public DefaultHandshakeHandler handshakeHandler() {
//
//        WebSocketPolicy policy = new WebSocketPolicy(WebSocketBehavior.SERVER);
//        policy.setInputBufferSize(8192);
//        policy.setIdleTimeout(600000);
//
//        return new DefaultHandshakeHandler(
//                new JettyRequestUpgradeStrategy(new WebSocketServerFactory(policy)));
//    }

}

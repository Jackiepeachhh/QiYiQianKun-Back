package com.atlcd.chinesechessback.config;

import com.atlcd.chinesechessback.common.properties.CorsProperties;
import com.atlcd.chinesechessback.interceptor.JwtWebSocketHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final CorsProperties corsProperties;

    private final StompConfig stompConfig;

    private final JwtWebSocketHandshakeInterceptor jwtWebSocketHandshakeInterceptor;
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins(corsProperties.getAllowOrigins().toArray(new String[0]))
                .withSockJS()
                .setInterceptors(jwtWebSocketHandshakeInterceptor)
                .setStreamBytesLimit(stompConfig.getStreamBytesLimit())
                .setHttpMessageCacheSize(stompConfig.getHttpMessageCacheSize())
                .setDisconnectDelay(stompConfig.getDisconnectDelay());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/room");
        registry.setApplicationDestinationPrefixes("/app");

    }

}

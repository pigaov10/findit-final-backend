package com.imogo.imogo_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // para mensagens de saída
        config.setApplicationDestinationPrefixes("/app"); // mensagens de entrada
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // O frontend conecta aqui via SockJS
        registry.addEndpoint("/ws-chat")
                .setAllowedOrigins("http://localhost:3000", "http://localhost:3001")
                .withSockJS();
    }
}

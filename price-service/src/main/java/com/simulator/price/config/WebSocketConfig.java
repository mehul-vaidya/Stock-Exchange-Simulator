package com.simulator.price.config;
//It enables WebSocket + STOMP messaging so your backend can push live stock prices
// to the Angular UI without polling.
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        /** Enables an in-memory message broker
        Used for broadcasting
        Clients subscribe to it
        **/
        registry.enableSimpleBroker("/topic"); //Server → Client (broadcast)
        registry.setApplicationDestinationPrefixes("/app"); ///app — Client → Server
    }

    //This defines where the WebSocket connection starts.
    //Frontend connects to: ws://localhost:808X/ws
    //* Allows any frontend (Angular running on another port) to connect.
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*");
    }
}

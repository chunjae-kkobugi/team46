package com.memomo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/stomp-send");
        registry.enableSimpleBroker("/stomp-receive");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp")
                .withSockJS();
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit((4*8)*1024);
        // 메시지 최대 바이트 제한(초과 시 연결 종료)
        // 32*1024 Byte = 32 KByte
        // MariaDB 한 글자는 4바이트이므로, 대략 8000글자 정도
        // 비트 < 바이트(x8) < 킬로바이트(x1024) < 메가바이트(x1024)
        registry.setTimeToFirstMessage(30000);
        // 최초 메시지 수신 최대 시간(밀리초) 제한(초과 시 연결 종료)
        // 30,000 ms = 30 s
        // 초기 통신이나 핑 테스트 등을 위해 시간 설정
    }
}
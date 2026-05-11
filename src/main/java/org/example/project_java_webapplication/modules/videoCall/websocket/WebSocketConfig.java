package org.example.project_java_webapplication.modules.videoCall.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.context.annotation.Bean;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig
        implements WebSocketConfigurer {

    private final SignalWebSocketHandler signalWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(
            WebSocketHandlerRegistry registry
    ) {

        registry.addHandler(
                signalWebSocketHandler,
                "/signal"
        ).setAllowedOrigins("*");
    }

    /**
     * FIX: Increase WebSocket message size limits.
     * Default is 8192 bytes — too small for screen-share SDP offers (20-50KB).
     * Also increase session idle timeout to prevent drops during long sessions.
     */
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        // 512 KB — enough for any SDP offer including screen share codecs
        container.setMaxTextMessageBufferSize(524288);
        container.setMaxBinaryMessageBufferSize(524288);
        // 30 minutes idle timeout (in milliseconds)
        container.setMaxSessionIdleTimeout(1800000L);
        return container;
    }
}
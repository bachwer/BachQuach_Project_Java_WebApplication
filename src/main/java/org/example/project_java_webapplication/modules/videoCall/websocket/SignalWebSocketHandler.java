package org.example.project_java_webapplication.modules.videoCall.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class SignalWebSocketHandler
        extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;

    // =====================================
    // CONNECTION ESTABLISHED
    // =====================================

    @Override
    public void afterConnectionEstablished(
            WebSocketSession session
    ) throws Exception {

        System.out.println(
                "Socket connected: "
                        + session.getId()
        );
    }

    // =====================================
    // HANDLE MESSAGE
    // =====================================

    @Override
    protected void handleTextMessage(

            WebSocketSession session,

            TextMessage message

    ) throws Exception {

        JsonNode json =
                objectMapper.readTree(
                        message.getPayload()
                );

        String action =
                json.has("action")
                        ? json.get("action").asText()
                        : "";

        // =================================
        // CONNECT
        // =================================

        if ("connect".equals(action)) {

            String username =
                    json.get("username")
                            .asText();

            // =============================
            // CLOSE OLD SESSION
            // =============================

            WebSocketSession oldSession =

                    UserSocketSessionManager
                            .getUser(username);

            if (

                    oldSession != null &&

                            oldSession.isOpen() &&

                            !oldSession.getId()
                                    .equals(session.getId())

            ) {

                try {

                    oldSession.close();

                    System.out.println(
                            "Closed old session for: "
                                    + username
                    );

                } catch (Exception e) {

                    System.out.println(
                            "Failed closing old session: "
                                    + e.getMessage()
                    );
                }
            }

            // =============================
            // SAVE SESSION
            // =============================

            UserSocketSessionManager
                    .addUser(
                            username,
                            session
                    );

            session.getAttributes().put(
                    "username",
                    username
            );

            System.out.println(
                    username
                            + " connected via WebSocket"
            );

            return;
        }

        // =================================
        // HEARTBEAT
        // =================================

        if ("ping".equals(action)) {

            session.sendMessage(

                    new TextMessage(
                            "{\"action\":\"pong\"}"
                    )
            );

            return;
        }

        // =================================
        // FORWARD SIGNAL
        // =================================

        if (json.has("target")) {

            String target =
                    json.get("target")
                            .asText();

            WebSocketSession targetSession =

                    UserSocketSessionManager
                            .getUser(target);

            // =============================
            // TARGET NOT FOUND
            // =============================

            if (targetSession == null) {

                System.out.println(
                        "Target "
                                + target
                                + " offline"
                );

                return;
            }

            // =============================
            // TARGET CLOSED
            // =============================

            if (!targetSession.isOpen()) {

                System.out.println(
                        "Target "
                                + target
                                + " session closed"
                );

                UserSocketSessionManager
                        .removeUser(target);

                return;
            }

            // =============================
            // SEND SIGNAL
            // =============================

            try {

                targetSession.sendMessage(
                        new TextMessage(
                                message.getPayload()
                        )
                );

                System.out.println(

                        "Forwarded "

                                + action

                                + " to "

                                + target
                );

            } catch (Exception e) {

                System.out.println(

                        "Failed forwarding to "

                                + target

                                + ": "

                                + e.getMessage()
                );

                UserSocketSessionManager
                        .removeUser(target);

                try {

                    targetSession.close();

                } catch (Exception ignored) {
                }
            }
        }
    }

    // =====================================
    // CONNECTION CLOSED
    // =====================================

    @Override
    public void afterConnectionClosed(

            WebSocketSession session,

            CloseStatus status

    ) throws Exception {

        String username =

                (String) session
                        .getAttributes()
                        .get("username");

        if (username == null) {
            return;
        }

        // =================================
        // SAFE REMOVE
        // =================================

        WebSocketSession currentSession =

                UserSocketSessionManager
                        .getUser(username);

        if (

                currentSession != null &&

                        currentSession.getId()
                                .equals(session.getId())

        ) {

            UserSocketSessionManager
                    .removeUser(username);

            System.out.println(
                    username
                            + " disconnected"
            );
        }
    }

    // =====================================
    // TRANSPORT ERROR
    // =====================================

    @Override
    public void handleTransportError(

            WebSocketSession session,

            Throwable exception

    ) throws Exception {

        System.out.println(

                "WebSocket transport error: "

                        + exception.getMessage()
        );

        try {

            if (session.isOpen()) {
                session.close();
            }

        } catch (Exception ignored) {
        }
    }
}
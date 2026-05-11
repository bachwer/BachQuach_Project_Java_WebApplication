package org.example.project_java_webapplication.modules.videoCall.controller;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.videoCall.dto.SignalMessage;
import org.example.project_java_webapplication.modules.videoCall.websocket.UserSocketSessionManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/api/signal")
@RequiredArgsConstructor
public class SignalController {

    private final ObjectMapper
            objectMapper;

    // =====================================
    // SEND SIGNAL
    // =====================================

    @PostMapping("/send")
    public ResponseEntity<?> sendSignal(

            @RequestBody
            SignalMessage signalMessage

    ) throws Exception {

        WebSocketSession targetSession =

                UserSocketSessionManager.getUser(
                        signalMessage.getTarget()
                );

        if (targetSession == null) {

            return ResponseEntity.badRequest()
                    .body("TARGET USER OFFLINE");
        }

        if (!targetSession.isOpen()) {

            return ResponseEntity.badRequest()
                    .body("TARGET SESSION CLOSED");
        }

        targetSession.sendMessage(

                new TextMessage(

                        objectMapper.writeValueAsString(
                                signalMessage
                        )
                )
        );

        return ResponseEntity.ok(
                "SIGNAL SENT"
        );
    }

    // =====================================
    // CHECK ONLINE
    // =====================================

    @GetMapping("/online/{username}")
    public ResponseEntity<?> isOnline(

            @PathVariable
            String username

    ) {

        boolean online =

                UserSocketSessionManager
                        .isOnline(username);

        return ResponseEntity.ok(
                online
        );
    }
}
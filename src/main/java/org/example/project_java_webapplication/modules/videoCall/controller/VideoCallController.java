package org.example.project_java_webapplication.modules.videoCall.controller;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.videoCall.entity.VideoCall;
import org.example.project_java_webapplication.modules.videoCall.service.VideoCallService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/video-call")
@RequiredArgsConstructor
public class VideoCallController {

    private final VideoCallService
            videoCallService;

    // =====================================
    // CREATE CALL
    // =====================================

    @PostMapping("/start/{mentoringSessionId}")
    public ResponseEntity<?> startCall(

            @PathVariable
            Long mentoringSessionId

    ) {

        VideoCall videoCall =
                videoCallService
                        .createCall(
                                mentoringSessionId
                        );

        return ResponseEntity.ok(
                videoCall
        );
    }

    // =====================================
    // ACCEPT CALL
    // =====================================

    @PostMapping("/accept/{roomId}")
    public ResponseEntity<?> acceptCall(

            @PathVariable
            String roomId

    ) {

        VideoCall videoCall =
                videoCallService
                        .acceptCall(
                                roomId
                        );

        return ResponseEntity.ok(
                videoCall
        );
    }

    // =====================================
    // REJECT CALL
    // =====================================

    @PostMapping("/reject/{roomId}")
    public ResponseEntity<?> rejectCall(

            @PathVariable
            String roomId

    ) {

        VideoCall videoCall =
                videoCallService
                        .rejectCall(
                                roomId
                        );

        return ResponseEntity.ok(
                videoCall
        );
    }

    // =====================================
    // END CALL
    // =====================================

    @PostMapping("/end/{roomId}")
    public ResponseEntity<?> endCall(

            @PathVariable
            String roomId

    ) {

        videoCallService
                .endCall(roomId);

        return ResponseEntity.ok(
                "CALL ENDED"
        );
    }

    // =====================================
    // FIND CALL
    // =====================================

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getCall(

            @PathVariable
            String roomId

    ) {

        VideoCall videoCall =
                videoCallService
                        .findByRoomId(
                                roomId
                        );

        return ResponseEntity.ok(
                videoCall
        );
    }
}
package org.example.project_java_webapplication.modules.videoCall.service;


import org.example.project_java_webapplication.modules.videoCall.entity.VideoCall;

public interface VideoCallService {

    VideoCall createCall(Long sessionId);

    VideoCall acceptCall(
            String roomId
    );

    VideoCall rejectCall(
            String roomId
    );

    void endCall(String roomId);

    VideoCall findByRoomId(
            String roomId
    );

    void markMissedCall(
            String roomId
    );
}
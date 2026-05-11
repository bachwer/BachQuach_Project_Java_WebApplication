package org.example.project_java_webapplication.modules.videoCall.repository;

import org.example.project_java_webapplication.modules.videoCall.entity.VideoCall;
import org.example.project_java_webapplication.modules.videoCall.entity.VideoCallStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoCallRepository extends JpaRepository<VideoCall, Long> {

    Optional<VideoCall> findByRoomId(String roomId);

    Optional<VideoCall> findTopByMentoringSessionIdOrderByCreatedAtDesc(Long sessionId);

    Optional<VideoCall> findTopByCallerIdAndReceiverIdAndStatus(
            Long callerId,
            Long receiverId,
            VideoCallStatus status
    );
}
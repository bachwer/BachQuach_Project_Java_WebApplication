package org.example.project_java_webapplication.modules.videoCall.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.auth.repository.UserRepository;
import org.example.project_java_webapplication.modules.mentoring.entity.MentoringSession;
import org.example.project_java_webapplication.modules.mentoring.repository.MentoringSessionRepository;
import org.example.project_java_webapplication.modules.videoCall.entity.VideoCall;
import org.example.project_java_webapplication.modules.videoCall.entity.VideoCallStatus;
import org.example.project_java_webapplication.modules.videoCall.repository.VideoCallRepository;
import org.example.project_java_webapplication.modules.videoCall.service.VideoCallService;
import org.example.project_java_webapplication.modules.videoCall.util.RoomIdGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class VideoCallServiceImpl
        implements VideoCallService {

    private final VideoCallRepository
            videoCallRepository;

    private final MentoringSessionRepository
            mentoringSessionRepository;

    private final UserRepository
            userRepository;

    // =====================================
    // CREATE VIDEO CALL
    // =====================================

    @Override
    public VideoCall createCall(
            Long mentoringSessionId
    ) {

        // =================================
        // GET MENTORING SESSION
        // =================================

        MentoringSession session =
                mentoringSessionRepository
                        .findById(mentoringSessionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Mentoring session not found"
                                )
                        );

        // =================================
        // GET USERS
        // =================================

        User lecturer =
                session.getLecturer();

        User student =
                session.getStudent();

        if (lecturer == null) {

            throw new RuntimeException(
                    "Lecturer not found"
            );
        }

        if (student == null) {

            throw new RuntimeException(
                    "Student not found"
            );
        }

        // =================================
        // CHECK EXISTING ACTIVE CALL
        // =================================
        VideoCall existingCall = videoCallRepository
                .findTopByMentoringSessionIdOrderByCreatedAtDesc(mentoringSessionId)
                .orElse(null);

        if (existingCall != null && existingCall.getStatus() != VideoCallStatus.ENDED) {
            return existingCall;
        }

        // =================================
        // CREATE ROOM ID
        // =================================

        String roomId =
                RoomIdGenerator
                        .generateSessionRoom(

                                session.getId(),

                                lecturer.getId(),

                                student.getId()
                        );

        // =================================
        // CREATE VIDEO CALL
        // =================================

        VideoCall videoCall =
                new VideoCall();

        videoCall.setMentoringSession(
                session
        );

        videoCall.setCaller(
                lecturer
        );

        videoCall.setReceiver(
                student
        );

        videoCall.setRoomId(
                roomId
        );

        videoCall.setStatus(
                VideoCallStatus.RINGING
        );

        videoCall.setCreatedAt(
                LocalDateTime.now()
        );

        // =================================
        // SAVE
        // =================================

        return videoCallRepository.save(
                videoCall
        );
    }

    // =====================================
    // ACCEPT CALL
    // =====================================

    @Override
    public VideoCall acceptCall(
            String roomId
    ) {

        VideoCall videoCall =
                videoCallRepository
                        .findByRoomId(roomId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Call not found"
                                )
                        );

        videoCall.setStatus(
                VideoCallStatus.ACCEPTED
        );

        videoCall.setStartedAt(
                LocalDateTime.now()
        );

        return videoCallRepository.save(
                videoCall
        );
    }

    // =====================================
    // REJECT CALL
    // =====================================

    @Override
    public VideoCall rejectCall(
            String roomId
    ) {

        VideoCall videoCall =
                videoCallRepository
                        .findByRoomId(roomId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Call not found"
                                )
                        );

        videoCall.setStatus(
                VideoCallStatus.REJECTED
        );

        videoCall.setEndedAt(
                LocalDateTime.now()
        );

        return videoCallRepository.save(
                videoCall
        );
    }

    // =====================================
    // END CALL
    // =====================================

    @Override
    public void endCall(
            String roomId
    ) {

        VideoCall videoCall =
                videoCallRepository
                        .findByRoomId(roomId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Call not found"
                                )
                        );

        videoCall.setStatus(
                VideoCallStatus.ENDED
        );

        videoCall.setEndedAt(
                LocalDateTime.now()
        );

        videoCallRepository.save(
                videoCall
        );
    }

    // =====================================
    // FIND BY ROOM ID
    // =====================================

    @Override
    public VideoCall findByRoomId(
            String roomId
    ) {

        return videoCallRepository
                .findByRoomId(roomId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Call not found"
                        )
                );
    }

    // =====================================
    // MISSED CALL
    // =====================================

    @Override
    public void markMissedCall(
            String roomId
    ) {

        VideoCall videoCall =
                videoCallRepository
                        .findByRoomId(roomId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Call not found"
                                )
                        );

        videoCall.setStatus(
                VideoCallStatus.MISSED
        );

        videoCall.setEndedAt(
                LocalDateTime.now()
        );

        videoCallRepository.save(
                videoCall
        );
    }
}
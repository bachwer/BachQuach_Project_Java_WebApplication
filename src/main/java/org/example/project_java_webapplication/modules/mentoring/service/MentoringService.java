package org.example.project_java_webapplication.modules.mentoring.service;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.mentoring.entity.MentoringSession;
import org.example.project_java_webapplication.modules.mentoring.entity.MentoringStatus;
import org.example.project_java_webapplication.modules.mentoring.repository.MentoringSessionRepository;
import org.example.project_java_webapplication.modules.videoCall.repository.VideoCallRepository;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MentoringService {

    private final MentoringSessionRepository mentoringSessionRepository;
    private final VideoCallRepository videoCallRepository;

    @Transactional
    public MentoringSession createSession(MentoringSession session) {
        if (!isSlotAvailable(session.getLecturer(), session.getSessionDate(), session.getStartTime())) {
            throw new RuntimeException("Lecturer is already booked for this slot!");
        }
        return mentoringSessionRepository.save(session);
    }

    public boolean isSlotAvailable(User lecturer, LocalDate date, LocalTime startTime) {
        return !mentoringSessionRepository.isSlotOccupied(lecturer, date, startTime);
    }

    public List<MentoringSession> getPendingRequests(User lecturer) {
        return mentoringSessionRepository.findByLecturerAndStatus(lecturer, MentoringStatus.PENDING);
    }

    public List<MentoringSession> getConfirmedRequest(User lecturer) {
        return mentoringSessionRepository.findByLecturerAndStatus(lecturer, MentoringStatus.CONFIRMED);
    }

    public List<MentoringSession> getHistoryRequests(User lecturer) {
        return mentoringSessionRepository.findByLecturerAndStatusInOrderBySessionDateDescStartTimeDesc(lecturer, 
                List.of(MentoringStatus.COMPLETED, MentoringStatus.CANCELLED));
    }

    public long getPendingCount(User lecturer) {
        return mentoringSessionRepository.countByLecturerAndStatus(lecturer, MentoringStatus.PENDING);
    }

    public long getTotalMentored(User lecturer) {
        return mentoringSessionRepository.countByLecturerAndStatus(lecturer, MentoringStatus.COMPLETED);
    }

    @Transactional
    public void updateStatus(Long sessionId, MentoringStatus status) {
        MentoringSession session = mentoringSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        session.setStatus(status);
        
        if (status == MentoringStatus.CANCELLED) {
            session.setCancelledAt(LocalDateTime.now());
        }
        
        if (status == MentoringStatus.COMPLETED || status == MentoringStatus.CANCELLED) {
            videoCallRepository.findTopByMentoringSessionIdOrderByCreatedAtDesc(sessionId)
                .ifPresent(call -> {
                    if (call.getEndedAt() == null) {
                        call.setEndedAt(LocalDateTime.now());
                        call.setStatus(org.example.project_java_webapplication.modules.videoCall.entity.VideoCallStatus.ENDED);
                        videoCallRepository.save(call);
                    }
                });
        }
        
        mentoringSessionRepository.save(session);
    }

    public List<MentoringSession> getStudentSessions(User student) {
        return mentoringSessionRepository.findByStudent(student);
    }

    public MentoringSession getSessionById(Long id) {
        return mentoringSessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    public MentoringSession getSessionWithDetails(Long id) {
        return mentoringSessionRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }
}

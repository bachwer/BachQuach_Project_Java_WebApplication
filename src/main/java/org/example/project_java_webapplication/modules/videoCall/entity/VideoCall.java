package org.example.project_java_webapplication.modules.videoCall.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.mentoring.entity.MentoringSession;

import java.time.LocalDateTime;

@Entity
@Table(name = "video_call_sessions")
@Getter
@Setter
public class VideoCall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // MENTORING SESSION
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentoring_session_id", nullable = false)
    private MentoringSession mentoringSession;

    // =========================
    // ROOM
    // =========================

    @Column(name = "room_id", nullable = false, unique = true)
    private String roomId;

    // =========================
    // CALLER
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caller_id", nullable = false)
    private User caller;

    // =========================
    // RECEIVER
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    // =========================
    // STATUS
    // =========================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VideoCallStatus status;

    // =========================
    // TIME
    // =========================

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // =========================
    // AUTO CREATE
    // =========================

    @PrePersist
    public void prePersist() {

        this.createdAt = LocalDateTime.now();

    }

    public String getDurationFormatted() {
        if (startedAt == null || endedAt == null) return "N/A";
        java.time.Duration duration = java.time.Duration.between(startedAt, endedAt);
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        return String.format(
            "%02d:%02d:%02d",
            absSeconds / 3600,
            (absSeconds % 3600) / 60,
            absSeconds % 60);
    }
}

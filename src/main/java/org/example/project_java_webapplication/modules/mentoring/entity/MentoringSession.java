package org.example.project_java_webapplication.modules.mentoring.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.project_java_webapplication.modules.user.entity.Department;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "mentoring_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentoringSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private org.example.project_java_webapplication.modules.auth.entity.User student;

    @ManyToOne
    @JoinColumn(name = "lecturer_id", nullable = false)
    private org.example.project_java_webapplication.modules.auth.entity.User lecturer;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "session_date", nullable = false)
    private LocalDate sessionDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private MentoringStatus status = MentoringStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "mentoringSession", cascade = CascadeType.ALL)
    @Builder.Default
    private java.util.List<org.example.project_java_webapplication.modules.videoCall.entity.VideoCall> videoCalls = new java.util.ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}

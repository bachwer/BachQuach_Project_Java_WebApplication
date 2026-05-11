package org.example.project_java_webapplication.modules.evaluations.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.mentoring.entity.MentoringSession;
import org.example.project_java_webapplication.modules.user.entity.Lecturer;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_evaluations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "mentoring_session_id", nullable = false, unique = true)
    private MentoringSession mentoringSession;

    @ManyToOne
    @JoinColumn(name = "lecturer_id", nullable = false)
    private org.example.project_java_webapplication.modules.auth.entity.User lecturer;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Enumerated(EnumType.STRING)
    @Column(name = "performance_level")
    private PerformanceLevel performanceLevel;

    @Column(name = "evaluation_comment", columnDefinition = "TEXT")
    private String evaluationComment;

    @Column(columnDefinition = "TEXT")
    private String recommendation;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
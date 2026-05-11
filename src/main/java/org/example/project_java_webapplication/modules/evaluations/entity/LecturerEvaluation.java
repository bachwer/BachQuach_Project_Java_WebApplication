package org.example.project_java_webapplication.modules.evaluations.entity;


import jakarta.persistence.*;
import lombok.*;
import org.example.project_java_webapplication.modules.mentoring.entity.MentoringSession;
import org.example.project_java_webapplication.modules.user.entity.Lecturer;

import java.time.LocalDateTime;

@Entity
@Table(name="lecturer_evaluations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LecturerEvaluation {

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
    private org.example.project_java_webapplication.modules.auth.entity.User student;
    @Column(nullable = false)

    private Integer rating;

    @Column(columnDefinition = "TEXT")

    private String feedback;

    @Column(name = "created_at", updatable = false)

    private LocalDateTime createdAt;

    @PrePersist

    public void prePersist() {

        this.createdAt = LocalDateTime.now();

    }


}

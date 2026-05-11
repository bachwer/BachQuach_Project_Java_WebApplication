package org.example.project_java_webapplication.modules.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.project_java_webapplication.modules.auth.entity.User;
import java.time.LocalDateTime;

@Entity
@Table(name = "lecturers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lecturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "academic_rank")
    private String academicRank;

    @Column(columnDefinition = "TEXT")
    private String specialization;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public String getFullName() {
        return user != null ? user.getFullName() : null;
    }
}

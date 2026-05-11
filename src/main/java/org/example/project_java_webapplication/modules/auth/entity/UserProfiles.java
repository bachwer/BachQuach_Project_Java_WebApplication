package org.example.project_java_webapplication.modules.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name ="user_profiles")
@Getter
@Setter
public class UserProfiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;


    @Column(name = "full_name")
    private String fullName;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Temporal(TemporalType.DATE)
    private Date dob;
    @Column(name = "avatar_url")

    private String avatarUrl;

    // ADDRESS

    @Column(columnDefinition = "TEXT")

    private String address;

    // CREATED AT

    @Column(name = "created_at")

    private LocalDateTime createdAt;

    @PrePersist

    public void prePersist() {

        createdAt = LocalDateTime.now();

    }


}

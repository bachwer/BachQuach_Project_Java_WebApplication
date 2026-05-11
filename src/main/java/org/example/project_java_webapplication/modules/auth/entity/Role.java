package org.example.project_java_webapplication.modules.auth.entity;

import jakarta.persistence.*;

import lombok.*;

@Entity

@Table(name = "roles")

@Getter

@Setter

@NoArgsConstructor

@AllArgsConstructor

@Builder

public class Role {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String name;

}
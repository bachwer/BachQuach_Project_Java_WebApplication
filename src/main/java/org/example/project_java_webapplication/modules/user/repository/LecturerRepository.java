package org.example.project_java_webapplication.modules.user.repository;

import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.user.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LecturerRepository extends JpaRepository<Lecturer, Long> {
    Optional<Lecturer> findByUser(User user);
    java.util.List<Lecturer> findByUserProfileFullNameContainingIgnoreCase(String name);
}

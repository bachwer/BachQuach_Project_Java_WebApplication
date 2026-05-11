package org.example.project_java_webapplication.modules.user.repository;

import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.user.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUser(User user);
}

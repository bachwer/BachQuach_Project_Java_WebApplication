package org.example.project_java_webapplication.modules.auth.repository;

import org.example.project_java_webapplication.modules.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByRolesName(String roleName);
    List<User> findByRolesNameAndProfileFullNameContainingIgnoreCase(String roleName, String name);
}
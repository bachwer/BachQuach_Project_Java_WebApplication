package org.example.project_java_webapplication.modules.auth.repository;
import org.example.project_java_webapplication.modules.auth.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}

package org.example.project_java_webapplication.modules.auth.repository;

import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.auth.entity.UserProfiles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfilesRepository
        extends JpaRepository<UserProfiles, Long> {

    Optional<UserProfiles> findByUser(User user);
}
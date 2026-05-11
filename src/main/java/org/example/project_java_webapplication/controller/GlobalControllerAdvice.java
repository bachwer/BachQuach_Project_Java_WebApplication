package org.example.project_java_webapplication.controller;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.auth.entity.UserProfiles;
import org.example.project_java_webapplication.modules.auth.repository.UserProfilesRepository;
import org.example.project_java_webapplication.modules.auth.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final UserRepository userRepository;
    private final UserProfilesRepository userProfilesRepository;

    @ModelAttribute("currentUser")
    public UserProfiles getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return userProfilesRepository.findByUser(user).orElse(null);
        }
        return null;
    }
}

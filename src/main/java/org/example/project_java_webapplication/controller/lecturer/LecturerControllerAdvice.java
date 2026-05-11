package org.example.project_java_webapplication.controller.lecturer;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.auth.repository.UserRepository;
import org.example.project_java_webapplication.modules.user.entity.Lecturer;
import org.example.project_java_webapplication.modules.user.repository.LecturerRepository;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice(basePackages = "org.example.project_java_webapplication.controller.lecturer")
@RequiredArgsConstructor
public class LecturerControllerAdvice {

    private final UserRepository userRepository;
    private final LecturerRepository lecturerRepository;

    @ModelAttribute("user")
    public User currentUser(Principal principal) {
        if (principal == null) return null;
        return userRepository.findByEmail(principal.getName()).orElse(null);
    }

    @ModelAttribute("lecturerProfile")
    public Lecturer currentLecturer(@ModelAttribute("user") User user) {
        if (user == null) return null;
        return lecturerRepository.findByUser(user).orElse(null);
    }
}

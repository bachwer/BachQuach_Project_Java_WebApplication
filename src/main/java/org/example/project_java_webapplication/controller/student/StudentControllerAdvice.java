package org.example.project_java_webapplication.controller.student;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.auth.repository.UserRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice(basePackages = "org.example.project_java_webapplication.controller.student")
@RequiredArgsConstructor
public class StudentControllerAdvice {

    private final UserRepository userRepository;

    @ModelAttribute
    public void addUserToModel(Model model, Principal principal) {
        if (principal != null) {
            userRepository.findByEmail(principal.getName()).ifPresent(user -> {
                model.addAttribute("user", user);
                model.addAttribute("currentUser", user.getProfile());
            });
        }
    }
}

package org.example.project_java_webapplication.modules.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.auth.dto.RegisterRequest;
import org.example.project_java_webapplication.modules.auth.entity.Gender;
import org.example.project_java_webapplication.modules.auth.entity.Role;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.auth.entity.UserProfiles;
import org.example.project_java_webapplication.modules.auth.repository.RoleRepository;
import org.example.project_java_webapplication.modules.auth.repository.UserProfilesRepository;
import org.example.project_java_webapplication.modules.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserProfilesRepository userProfilesRepository;

    // REGISTER
    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute RegisterRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        // CHECK EMAIL
        if (userRepository.existsByEmail(request.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email already exists");
            return "redirect:/register";
        }

        if(result.hasErrors()){
            return "redirect:/register";
        }

        // ROLE
        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new RuntimeException("Role STUDENT not found"));

        // CREATE USER
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isActive(true)
                .roles(Set.of(studentRole))
                .build();

        // SAVE USER
        User savedUser = userRepository.save(user);

        // CREATE PROFILE
        UserProfiles profile = new UserProfiles();
        profile.setUser(savedUser);
        profile.setFullName(request.getFullName());
        profile.setPhone(request.getPhone());
        profile.setGender(Gender.valueOf(request.getGender()));
        profile.setAddress(request.getAddress());
        profile.setDob(java.sql.Date.valueOf(request.getDob()));

        // SAVE PROFILE
        userProfilesRepository.save(profile);

        redirectAttributes.addFlashAttribute("success", "Register success! Please login.");
        return "redirect:/login";
    }
}
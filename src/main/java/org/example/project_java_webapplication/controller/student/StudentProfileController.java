package org.example.project_java_webapplication.controller.student;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.auth.entity.UserProfiles;
import org.example.project_java_webapplication.modules.auth.repository.UserProfilesRepository;
import org.example.project_java_webapplication.modules.auth.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentProfileController {

    private final UserRepository userRepository;
    private final UserProfilesRepository userProfilesRepository;

    @GetMapping("/profile")
    public String profile() {
        return "student/profile/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute UserProfiles profileData, 
                                Principal principal, RedirectAttributes ra) {
        try {
            User user = userRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            UserProfiles profile = user.getProfile();
            if (profile == null) {
                profile = new UserProfiles();
                profile.setUser(user);
            }
            
            profile.setFullName(profileData.getFullName());
            profile.setPhone(profileData.getPhone());
            profile.setGender(profileData.getGender());
            profile.setDob(profileData.getDob());
            profile.setAddress(profileData.getAddress());
            profile.setAvatarUrl(profileData.getAvatarUrl());
            
            userProfilesRepository.save(profile);
            ra.addFlashAttribute("successMessage", "Profile updated successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/student/profile";
    }

    @GetMapping("/profile/avatar")
    public String avatarSettings(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("currentUser", user.getProfile());
        return "student/profile/avatar";
    }

    @PostMapping("/profile/avatar/update")
    public String updateAvatar(@org.springframework.web.bind.annotation.RequestParam String avatarUrl, 
                               Principal principal, RedirectAttributes ra) {
        try {
            User user = userRepository.findByEmail(principal.getName()).orElseThrow();
            UserProfiles profile = user.getProfile();
            if (profile == null) {
                profile = new UserProfiles();
                profile.setUser(user);
            }
            profile.setAvatarUrl(avatarUrl);
            userProfilesRepository.save(profile);
            ra.addFlashAttribute("successMessage", "Avatar updated successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/student/profile/avatar";
    }
}

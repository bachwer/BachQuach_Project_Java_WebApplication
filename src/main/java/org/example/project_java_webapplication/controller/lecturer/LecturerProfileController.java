package org.example.project_java_webapplication.controller.lecturer;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.auth.repository.UserRepository;
import org.example.project_java_webapplication.modules.auth.repository.UserProfilesRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/lecturer/profile")
@RequiredArgsConstructor
public class LecturerProfileController {

    private final UserRepository userRepository;
    private final UserProfilesRepository userProfilesRepository;

    @GetMapping("/settings")
    public String profileSettings(Model model, @ModelAttribute("user") User user) {
        if (user == null) return "redirect:/login";
        model.addAttribute("pageTitle", "Profile Settings");
        model.addAttribute("activePage", "profile");
        model.addAttribute("profile", user.getProfile());
        return "lecturer/profile/settings";
    }

    @GetMapping("/profile/avatar")
    public String avatarSettings(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("currentUser", user.getProfile());
        return "lecturer/profile/avatar";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute org.example.project_java_webapplication.modules.auth.entity.UserProfiles profileData, 
                                Principal principal, RedirectAttributes ra) {
        try {
            User user = userRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            org.example.project_java_webapplication.modules.auth.entity.UserProfiles profile = user.getProfile();
            if (profile == null) {
                profile = new org.example.project_java_webapplication.modules.auth.entity.UserProfiles();
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
        return "redirect:/lecturer/profile/settings";
    }

    @GetMapping("/avatar")
    public String avatarSettings(Model model, @ModelAttribute("user") User user) {
        if (user == null) return "redirect:/login";
        model.addAttribute("pageTitle", "Avatar Designer");
        model.addAttribute("activePage", "profile");
        model.addAttribute("profile", user.getProfile());
        return "lecturer/profile/avatar";
    }

    @PostMapping("/avatar/update")
    public String updateAvatar(@org.springframework.web.bind.annotation.RequestParam String avatarUrl, 
                               Principal principal, RedirectAttributes ra) {
        try {
            User user = userRepository.findByEmail(principal.getName()).orElseThrow();
            org.example.project_java_webapplication.modules.auth.entity.UserProfiles profile = user.getProfile();
            if (profile == null) {
                profile = new org.example.project_java_webapplication.modules.auth.entity.UserProfiles();
                profile.setUser(user);
            }
            profile.setAvatarUrl(avatarUrl);
            userProfilesRepository.save(profile);
            ra.addFlashAttribute("successMessage", "Avatar updated successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/lecturer/profile/avatar";
    }
}

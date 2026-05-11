package org.example.project_java_webapplication.controller.lecturer;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.mentoring.service.MentoringService;
import org.example.project_java_webapplication.modules.user.entity.Lecturer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/lecturer")
@RequiredArgsConstructor
public class LecturerDashboardController {

    private final MentoringService mentoringService;

    @GetMapping
    public String lecturer() {
        return "redirect:/lecturer/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @ModelAttribute("lecturerProfile") Lecturer lecturer, @ModelAttribute("user") User lecturerUser) {
        if (lecturer == null) return "redirect:/login";

        model.addAttribute("pageTitle", "Dashboard Overview");
        model.addAttribute("activePage", "dashboard");

        model.addAttribute("totalMentored", mentoringService.getTotalMentored(lecturerUser));
        model.addAttribute("pendingCount", mentoringService.getPendingCount(lecturerUser));
        model.addAttribute("pendingRequests", mentoringService.getPendingRequests(lecturerUser));
        model.addAttribute("upcomingSessions", mentoringService.getConfirmedRequest(lecturerUser));

        return "lecturer/dashboard/dashboard";
    }

    @PostMapping("/dashboard")
    public String completeSession(@RequestParam(required = false) Long sessionId, RedirectAttributes ra) {
        if (sessionId != null) {
            mentoringService.updateStatus(sessionId, org.example.project_java_webapplication.modules.mentoring.entity.MentoringStatus.COMPLETED);
            ra.addFlashAttribute("successMessage", "Session marked as completed!");
        }
        return "redirect:/lecturer/dashboard";
    }
}

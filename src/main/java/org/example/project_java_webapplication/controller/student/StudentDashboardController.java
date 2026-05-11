package org.example.project_java_webapplication.controller.student;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.auth.repository.UserRepository;
import org.example.project_java_webapplication.modules.borrowings.repository.BorrowingRecordRepository;
import org.example.project_java_webapplication.modules.mentoring.entity.MentoringSession;
import org.example.project_java_webapplication.modules.mentoring.entity.MentoringStatus;
import org.example.project_java_webapplication.modules.mentoring.repository.MentoringSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentDashboardController {

    private final UserRepository userRepository;
    private final MentoringSessionRepository mentoringSessionRepository;
    private final BorrowingRecordRepository borrowingRecordRepository;

    @GetMapping
    public String student() {
        return "redirect:/student/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        if (principal != null) {
            User user = userRepository.findByEmail(principal.getName()).orElse(null);
            if (user != null) {
                List<MentoringSession> sessions = mentoringSessionRepository.findByStudent(user);
                model.addAttribute("sessionCount", sessions.size());
                model.addAttribute("borrowingCount", borrowingRecordRepository.findByStudentId(user.getId()).size());
                
                var upcoming = sessions.stream()
                        .filter(s -> s.getStatus() == MentoringStatus.CONFIRMED)
                        .sorted((a,b) -> a.getSessionDate().compareTo(b.getSessionDate()))
                        .findFirst().orElse(null);
                model.addAttribute("nextSession", upcoming);

                var history = sessions.stream()
                        .filter(s -> s.getStatus() == MentoringStatus.COMPLETED)
                        .sorted((a,b) -> b.getSessionDate().compareTo(a.getSessionDate()))
                        .limit(3).collect(Collectors.toList());
                model.addAttribute("sessionHistory", history);
            }
        }
        return "student/dashboard/dashboard";
    }

    @PostMapping("/dashboard")
    public String completeSession(@RequestParam(required = false) Long sessionId, RedirectAttributes ra) {
        ra.addFlashAttribute("successMessage", "Session ended.");
        return "redirect:/student/dashboard";
    }
}

package org.example.project_java_webapplication.controller.lecturer;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.mentoring.entity.MentoringSession;
import org.example.project_java_webapplication.modules.mentoring.entity.MentoringStatus;
import org.example.project_java_webapplication.modules.mentoring.service.MentoringService;
import org.example.project_java_webapplication.modules.user.entity.Lecturer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/lecturer")
@RequiredArgsConstructor
public class LecturerEvaluationController {

    private final MentoringService mentoringService;
    private final org.example.project_java_webapplication.modules.evaluations.repository.LecturerEvaluationRepository lecturerEvaluationRepository;
    private final org.example.project_java_webapplication.modules.evaluations.repository.StudentEvaluationRepository studentEvaluationRepository;

    @GetMapping("/evaluations")
    public String evaluations(Model model, @ModelAttribute("lecturerProfile") Lecturer lecturer, @ModelAttribute("user") User lecturerUser) {
        if (lecturer == null) return "redirect:/login";
        model.addAttribute("pageTitle", "Evaluations & Feedback");
        model.addAttribute("activePage", "evaluations");
        
        List<MentoringSession> history = mentoringService.getHistoryRequests(lecturerUser);
        List<MentoringSession> pendingEval = history.stream()
                .filter(s -> s.getStatus() == MentoringStatus.COMPLETED && s.getStudent() != null)
                .filter(s -> !studentEvaluationRepository.existsByMentoringSessionId(s.getId()))
                .toList();

        model.addAttribute("pendingEvaluations", pendingEval);
        
        List<org.example.project_java_webapplication.modules.evaluations.entity.StudentEvaluation> given = studentEvaluationRepository.findByLecturerId(lecturerUser.getId());
        List<org.example.project_java_webapplication.modules.evaluations.entity.LecturerEvaluation> received = lecturerEvaluationRepository.findByLecturerId(lecturerUser.getId());
        
        model.addAttribute("givenEvaluations", given);
        model.addAttribute("receivedEvaluations", received);

        // Stats
        model.addAttribute("totalGiven", given.size());
        model.addAttribute("totalReceived", received.size());
        double avgRating = received.stream().mapToInt(org.example.project_java_webapplication.modules.evaluations.entity.LecturerEvaluation::getRating).average().orElse(0.0);
        model.addAttribute("averageRating", String.format("%.1f", avgRating));

        // Performance Chart Data (Given to Students)
        model.addAttribute("excellentCount", given.stream().filter(e -> e.getPerformanceLevel().name().equals("EXCELLENT")).count());
        model.addAttribute("goodCount", given.stream().filter(e -> e.getPerformanceLevel().name().equals("GOOD")).count());
        model.addAttribute("averageCount", given.stream().filter(e -> e.getPerformanceLevel().name().equals("AVERAGE")).count());
        model.addAttribute("poorCount", given.stream().filter(e -> e.getPerformanceLevel().name().equals("POOR")).count());

        // Rating Chart Data (Received from Students)
        model.addAttribute("star5Count", received.stream().filter(e -> e.getRating() == 5).count());
        model.addAttribute("star4Count", received.stream().filter(e -> e.getRating() == 4).count());
        model.addAttribute("star3Count", received.stream().filter(e -> e.getRating() == 3).count());
        model.addAttribute("star2Count", received.stream().filter(e -> e.getRating() == 2).count());
        model.addAttribute("star1Count", received.stream().filter(e -> e.getRating() == 1).count());

        return "lecturer/evaluations/list";
    }
}

package org.example.project_java_webapplication.controller.student;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.auth.repository.UserRepository;
import org.example.project_java_webapplication.modules.mentoring.entity.MentoringSession;
import org.example.project_java_webapplication.modules.mentoring.entity.MentoringStatus;
import org.example.project_java_webapplication.modules.mentoring.repository.MentoringSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentEvaluationController {

    private final UserRepository userRepository;
    private final MentoringSessionRepository mentoringSessionRepository;
    private final org.example.project_java_webapplication.modules.evaluations.repository.LecturerEvaluationRepository lecturerEvaluationRepository;
    private final org.example.project_java_webapplication.modules.evaluations.repository.StudentEvaluationRepository studentEvaluationRepository;

    @GetMapping("/mentoring/evaluate/{id}")
    public String evaluateMentorPage(@PathVariable Long id, Model model) {
        MentoringSession session = mentoringSessionRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        
        if (session.getStatus() != MentoringStatus.COMPLETED || session.getLecturer() == null) {
            return "redirect:/student/mentoring/my-sessions";
        }
        
        model.addAttribute("mentoringSession", session);
        return "student/mentoring/evaluate";
    }

    @GetMapping("/evaluations")
    public String evaluations(Model model, Principal principal) {
        if (principal != null) {
            User user = userRepository.findByEmail(principal.getName()).orElse(null);
            if (user != null) {
                List<MentoringSession> sessions = mentoringSessionRepository.findByStudent(user);
                
                List<MentoringSession> pendingEval = sessions.stream()
                        .filter(s -> s.getStatus() == MentoringStatus.COMPLETED && s.getLecturer() != null)
                        .filter(s -> !lecturerEvaluationRepository.existsByMentoringSessionId(s.getId()))
                        .toList();
                
                List<org.example.project_java_webapplication.modules.evaluations.entity.LecturerEvaluation> given = lecturerEvaluationRepository.findByStudentId(user.getId());
                List<org.example.project_java_webapplication.modules.evaluations.entity.StudentEvaluation> received = studentEvaluationRepository.findByStudentId(user.getId());

                model.addAttribute("pendingEvaluations", pendingEval);
                model.addAttribute("givenEvaluations", given);
                model.addAttribute("receivedEvaluations", received);

                // Stats
                model.addAttribute("totalGiven", given.size());
                model.addAttribute("totalReceived", received.size());
                double avgRatingGiven = given.stream().mapToInt(org.example.project_java_webapplication.modules.evaluations.entity.LecturerEvaluation::getRating).average().orElse(0.0);
                model.addAttribute("averageRatingGiven", String.format("%.1f", avgRatingGiven));

                // Performance Chart Data (Received from Mentors)
                model.addAttribute("excellentCount", received.stream().filter(e -> e.getPerformanceLevel().name().equals("EXCELLENT")).count());
                model.addAttribute("goodCount", received.stream().filter(e -> e.getPerformanceLevel().name().equals("GOOD")).count());
                model.addAttribute("averageCount", received.stream().filter(e -> e.getPerformanceLevel().name().equals("AVERAGE")).count());
                model.addAttribute("poorCount", received.stream().filter(e -> e.getPerformanceLevel().name().equals("POOR")).count());

                // Rating Chart Data (Given to Mentors)
                model.addAttribute("star5Count", given.stream().filter(e -> e.getRating() == 5).count());
                model.addAttribute("star4Count", given.stream().filter(e -> e.getRating() == 4).count());
                model.addAttribute("star3Count", given.stream().filter(e -> e.getRating() == 3).count());
                model.addAttribute("star2Count", given.stream().filter(e -> e.getRating() == 2).count());
                model.addAttribute("star1Count", given.stream().filter(e -> e.getRating() == 1).count());
            }
        }
        return "student/evaluations/list";
    }
}

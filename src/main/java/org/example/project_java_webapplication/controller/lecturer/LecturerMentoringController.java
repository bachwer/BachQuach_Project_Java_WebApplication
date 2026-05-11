package org.example.project_java_webapplication.controller.lecturer;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.auth.repository.UserRepository;
import org.example.project_java_webapplication.modules.mentoring.entity.MentoringSession;
import org.example.project_java_webapplication.modules.mentoring.entity.MentoringStatus;
import org.example.project_java_webapplication.modules.mentoring.service.MentoringService;
import org.example.project_java_webapplication.modules.videoCall.entity.VideoCall;
import org.example.project_java_webapplication.modules.videoCall.entity.VideoCallStatus;
import org.example.project_java_webapplication.modules.videoCall.repository.VideoCallRepository;
import org.example.project_java_webapplication.modules.user.entity.Lecturer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/lecturer/mentoring")
@RequiredArgsConstructor
public class LecturerMentoringController {

    private final MentoringService mentoringService;
    private final UserRepository userRepository;
    private final VideoCallRepository videoCallRepository;

    @GetMapping("/pending")
    public String pendingMentoring(Model model, @ModelAttribute("lecturerProfile") Lecturer lecturer, @ModelAttribute("user") User lecturerUser) {
        if (lecturer == null) return "redirect:/login";

        model.addAttribute("pageTitle", "Pending Requests");
        model.addAttribute("activePage", "pending");
        model.addAttribute("pendingRequests", mentoringService.getPendingRequests(lecturerUser));

        return "lecturer/mentoring/pending";
    }

    @GetMapping("/accepted")
    public String acceptedMentoring(Model model, @ModelAttribute("lecturerProfile") Lecturer lecturer, @ModelAttribute("user") User lecturerUser) {
        if (lecturer == null) return "redirect:/login";

        model.addAttribute("pageTitle", "My Schedule");
        model.addAttribute("activePage", "accepted");
        model.addAttribute("sessions", mentoringService.getConfirmedRequest(lecturerUser));

        return "lecturer/mentoring/accepted";
    }

    @GetMapping("/completed")
    public String completedMentoring(Model model, @ModelAttribute("lecturerProfile") Lecturer lecturer, @ModelAttribute("user") User lecturerUser) {
        if (lecturer == null) return "redirect:/login";

        model.addAttribute("pageTitle", "Session History");
        model.addAttribute("activePage", "completed");
        model.addAttribute("sessions", mentoringService.getHistoryRequests(lecturerUser));

        return "lecturer/mentoring/completed";
    }

    @PostMapping("/approve")
    public String approveSession(@RequestParam Long sessionId, RedirectAttributes ra) {
        mentoringService.updateStatus(sessionId, MentoringStatus.CONFIRMED);
        ra.addFlashAttribute("successMessage", "Session approved successfully!");
        return "redirect:/lecturer/mentoring/pending";
    }

    @PostMapping("/reject")
    public String rejectSession(@RequestParam Long sessionId, RedirectAttributes ra) {
        mentoringService.updateStatus(sessionId, MentoringStatus.CANCELLED);
        ra.addFlashAttribute("successMessage", "Session rejected.");
        return "redirect:/lecturer/mentoring/pending";
    }

    @GetMapping("/evaluate/{id}")
    public String evaluateStudentPage(@PathVariable Long id, Model model) {
        MentoringSession session = mentoringService.getSessionWithDetails(id);
        if (session.getStatus() != MentoringStatus.COMPLETED || session.getStudent() == null) {
            return "redirect:/lecturer/mentoring/completed";
        }
        model.addAttribute("mentoringSession", session);
        return "lecturer/mentoring/evaluate";
    }

    @GetMapping("/init-data")
    public String initData(@ModelAttribute("lecturerProfile") Lecturer lecturer, RedirectAttributes ra) {
        if (lecturer == null) return "redirect:/login";
        
        List<User> students = userRepository.findByRolesName("STUDENT");
        if (students.isEmpty()) {
            ra.addFlashAttribute("errorMessage", "No students found in database. Create students first!");
            return "redirect:/lecturer/dashboard";
        }

        User student = students.get(0);
        User lecturerUser = lecturer.getUser();
        
        try {
            // Completed Session with Video Call
            MentoringSession s3 = MentoringSession.builder()
                    .student(student).lecturer(lecturerUser).department(lecturer.getDepartment())
                    .sessionDate(LocalDate.now().minusDays(1))
                    .startTime(LocalTime.of(9, 0)).endTime(LocalTime.of(10, 0))
                    .status(MentoringStatus.COMPLETED).note("Project Review: Architecture Design").build();
            s3 = mentoringService.createSession(s3);
            
            VideoCall call = new VideoCall();
            call.setMentoringSession(s3);
            call.setCaller(student);
            call.setReceiver(lecturerUser);
            call.setRoomId("ROOM-" + s3.getId());
            call.setStatus(VideoCallStatus.ENDED);
            call.setStartedAt(LocalDateTime.now().minusDays(1).withHour(9).withMinute(5));
            call.setEndedAt(LocalDateTime.now().minusDays(1).withHour(9).withMinute(55));
            videoCallRepository.save(call);
        } catch (Exception ignored) {}

        try {
            MentoringSession s1 = new MentoringSession();
            s1.setStudent(student);
            s1.setLecturer(lecturerUser);
            s1.setDepartment(lecturer.getDepartment());
            s1.setSessionDate(LocalDate.now().plusDays(1));
            s1.setStartTime(LocalTime.of(10, 0));
            s1.setEndTime(LocalTime.of(11, 30));
            s1.setStatus(MentoringStatus.PENDING);
            s1.setNote("Sample Request: Need help with Spring Boot");
            mentoringService.createSession(s1);
        } catch (Exception ignored) {}

        try {
            MentoringSession s2 = new MentoringSession();
            s2.setStudent(student);
            s2.setLecturer(lecturerUser);
            s2.setDepartment(lecturer.getDepartment());
            s2.setSessionDate(LocalDate.now().plusDays(2));
            s2.setStartTime(LocalTime.of(14, 0));
            s2.setEndTime(LocalTime.of(15, 30));
            s2.setStatus(MentoringStatus.CONFIRMED);
            s2.setNote("Sample Session: JPA Advanced Mapping");
            mentoringService.createSession(s2);
        } catch (Exception ignored) {}

        ra.addFlashAttribute("successMessage", "Sample mentoring data generated with history!");
        return "redirect:/lecturer/dashboard";
    }
}

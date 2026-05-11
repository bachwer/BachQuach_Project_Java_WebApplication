package org.example.project_java_webapplication.controller.student;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.auth.repository.UserRepository;
import org.example.project_java_webapplication.modules.borrowings.repository.BorrowingRecordRepository;
import org.example.project_java_webapplication.modules.mentoring.entity.MentoringSession;
import org.example.project_java_webapplication.modules.mentoring.entity.MentoringStatus;
import org.example.project_java_webapplication.modules.mentoring.repository.MentoringSessionRepository;
import org.example.project_java_webapplication.modules.mentoring.service.MentoringService;
import org.example.project_java_webapplication.modules.user.entity.Lecturer;
import org.example.project_java_webapplication.modules.user.repository.DepartmentRepository;
import org.example.project_java_webapplication.modules.user.repository.LecturerRepository;
import org.example.project_java_webapplication.modules.videoCall.repository.VideoCallRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/student/mentoring")
@RequiredArgsConstructor
public class StudentMentoringController {

    private final UserRepository userRepository;
    private final MentoringSessionRepository mentoringSessionRepository;
    private final MentoringService mentoringService;
    private final LecturerRepository lecturerRepository;
    private final DepartmentRepository departmentRepository;
    private final BorrowingRecordRepository borrowingRecordRepository;
    private final VideoCallRepository videoCallRepository;

    @GetMapping("/booking")
    public String booking(Model model) {
        model.addAttribute("lecturers", lecturerRepository.findAll());
        model.addAttribute("departments", departmentRepository.findAll());
        return "student/mentoring/booking";
    }

    @GetMapping("/my-sessions")
    public String mySessions(Model model, Principal principal) {
        if (principal != null) {
            User user = userRepository.findByEmail(principal.getName()).orElse(null);
            if (user != null) {
                List<MentoringSession> sessions = mentoringSessionRepository.findByStudent(user);
                model.addAttribute("sessions", sessions);
                model.addAttribute("borrowings", borrowingRecordRepository.findByStudentId(user.getId()));
                
                java.util.Map<Long, String> activeCalls = new java.util.HashMap<>();
                for (MentoringSession s : sessions) {
                    videoCallRepository.findTopByMentoringSessionIdOrderByCreatedAtDesc(s.getId())
                        .filter(call -> call.getStatus() != org.example.project_java_webapplication.modules.videoCall.entity.VideoCallStatus.ENDED)
                        .ifPresent(call -> activeCalls.put(s.getId(), call.getRoomId()));
                }
                model.addAttribute("activeCalls", activeCalls);
            }
        }
        return "student/mentoring/my-sessions";
    }

    @PostMapping("/book")
    public String bookSession(@RequestParam Long lecturerId, @RequestParam String sessionDate, 
                             @RequestParam String startTime, @RequestParam String endTime, 
                             @RequestParam String note, Principal principal, RedirectAttributes ra) {
        try {
            User student = userRepository.findByEmail(principal.getName()).orElseThrow();
            Lecturer lecturerEntity = lecturerRepository.findById(lecturerId).orElseThrow();
            User lecturerUser = lecturerEntity.getUser();

            LocalDate date = LocalDate.parse(sessionDate);
            LocalTime start = LocalTime.parse(startTime);
            if (!mentoringService.isSlotAvailable(lecturerUser, date, start)) {
                ra.addFlashAttribute("errorMessage", "Slot already booked.");
                return "redirect:/student/mentoring/booking";
            }
            MentoringSession session = MentoringSession.builder()
                    .student(student).lecturer(lecturerUser).department(lecturerEntity.getDepartment())
                    .sessionDate(date).startTime(start).endTime(LocalTime.parse(endTime))
                    .status(MentoringStatus.PENDING).note(note).build();
            mentoringSessionRepository.save(session);
            ra.addFlashAttribute("successMessage", "Booked!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/student/mentoring/my-sessions";
    }

    @PostMapping("/cancel")
    public String cancelSession(@RequestParam Long sessionId, RedirectAttributes ra) {
        try {
            mentoringService.updateStatus(sessionId, MentoringStatus.CANCELLED);
            ra.addFlashAttribute("successMessage", "Session cancelled successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/student/mentoring/my-sessions";
    }
}

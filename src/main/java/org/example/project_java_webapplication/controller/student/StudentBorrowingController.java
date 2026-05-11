package org.example.project_java_webapplication.controller.student;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.auth.repository.UserRepository;
import org.example.project_java_webapplication.modules.borrowings.dto.BorrowItemDTO;
import org.example.project_java_webapplication.modules.borrowings.dto.BorrowingRequestDTO;
import org.example.project_java_webapplication.modules.borrowings.repository.BorrowingRecordRepository;
import org.example.project_java_webapplication.modules.borrowings.service.BorrowingService;
import org.example.project_java_webapplication.modules.equipments.repository.EquipmentRepository;
import org.example.project_java_webapplication.modules.mentoring.repository.MentoringSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/student/borrowings")
@RequiredArgsConstructor
public class StudentBorrowingController {

    private final UserRepository userRepository;
    private final BorrowingRecordRepository borrowingRecordRepository;
    private final BorrowingService borrowingService;
    private final EquipmentRepository equipmentRepository;
    private final MentoringSessionRepository mentoringSessionRepository;

    @GetMapping("/my-borrowings")
    public String myBorrowings(Model model, Principal principal) {
        if (principal != null) {
            User user = userRepository.findByEmail(principal.getName()).orElse(null);
            if (user != null) {
                model.addAttribute("borrowings", borrowingRecordRepository.findByStudentId(user.getId()));
            }
        }
        return "student/borrowings/my-borrowings";
    }




    @GetMapping("/request")
    public String requestBorrowing(Model model, Principal principal) {
        if (principal != null) {
            User user = userRepository.findByEmail(principal.getName()).orElse(null);
            if (user != null) {
                model.addAttribute("sessions", mentoringSessionRepository.findByStudent(user));
                model.addAttribute("equipments", equipmentRepository.findAll());
            }
        }
        return "student/borrowings/request";
    }


    @PostMapping("/return/{id}")
    public String returnEquipment(@PathVariable Long id, RedirectAttributes ra){
        try {

            borrowingService.returnEquipment(id);

            ra.addFlashAttribute("successMessage", "Return successful!");

        } catch (Exception e) {

            ra.addFlashAttribute("errorMessage", e.getMessage());

        }
        return "redirect:/student/borrowings/my-borrowings";
    }



    @PostMapping("/request")
    public String submitBorrowRequest(
                                      @RequestParam int quantity,
                                      @RequestParam Long equipmentId,
                                      @RequestParam LocalDate expectedReturnDate,
                                      Principal principal, RedirectAttributes ra) {
        try {
            User student = userRepository.findByEmail(principal.getName()).orElseThrow();

            BorrowItemDTO item = new BorrowItemDTO();
            item.setEquipmentId(equipmentId);
            item.setQuantity(quantity);

            BorrowingRequestDTO request = new BorrowingRequestDTO();
            request.setStudentId(student.getId());
            request.setItems(List.of(item));
            request.setExpectedReturnDate(expectedReturnDate);

            borrowingService.requestBorrowing(request);
            ra.addFlashAttribute("successMessage", "Borrow request submitted successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/student/borrowings/my-borrowings";
    }
}

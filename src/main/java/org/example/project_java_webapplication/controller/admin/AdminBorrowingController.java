package org.example.project_java_webapplication.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.auth.repository.UserRepository;
import org.example.project_java_webapplication.modules.borrowings.entity.BorrowingStatus;
import org.example.project_java_webapplication.modules.borrowings.repository.BorrowingRecordRepository;
import org.example.project_java_webapplication.modules.borrowings.service.BorrowingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/admin/borrowings")
@RequiredArgsConstructor
public class AdminBorrowingController {

    private final BorrowingRecordRepository borrowingRecordRepository;
    private final BorrowingService borrowingService;
    private final UserRepository userRepository;

    @GetMapping("/pending")
    public String pendingBorrowings(@RequestParam(required = false) BorrowingStatus status, Model model) {
        var allBorrowings = (status != null) 
            ? borrowingRecordRepository.findByStatus(status)
            : borrowingRecordRepository.findAll();
            
        var stats = borrowingRecordRepository.findAll();
        model.addAttribute("borrowings", allBorrowings);
        model.addAttribute("currentStatus", status);
        
        model.addAttribute("pendingCount", stats.stream().filter(b -> b.getStatus() == BorrowingStatus.PENDING_DISPATCH).count());
        model.addAttribute("approvedCount", stats.stream().filter(b -> b.getStatus() == BorrowingStatus.DISPATCHED).count());
        model.addAttribute("rejectedCount", stats.stream().filter(b -> b.getStatus() == BorrowingStatus.REJECTED).count());
        model.addAttribute("returnedCount", stats.stream().filter(b -> b.getStatus() == BorrowingStatus.RETURNED).count());
        model.addAttribute("pendingReturnCount", stats.stream().filter(b -> b.getStatus() == BorrowingStatus.PENDING_RETURN).count());
        
        return "admin/borrowings/pending";
    }



    @PostMapping("/{id}/approve")
    public String approveBorrowing(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            User admin = userRepository.findByEmail(principal.getName()).orElseThrow();
            borrowingService.approveBorrowing(id, admin.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Borrowing request approved!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/admin/borrowings/pending";
    }

    @PostMapping("/{id}/reject")
    public String rejectBorrowing(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            User admin = userRepository.findByEmail(principal.getName()).orElseThrow();
            borrowingService.rejectBorrowing(id, admin.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Borrowing request rejected!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/admin/borrowings/pending";
    }

    @PostMapping("/{id}/return")
    public String returnBorrowing(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            borrowingService.returnBorrowing(id);
            redirectAttributes.addFlashAttribute("successMessage", "Item marked as returned!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/admin/borrowings/pending";
    }
}

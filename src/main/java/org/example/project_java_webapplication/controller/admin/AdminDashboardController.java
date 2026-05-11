package org.example.project_java_webapplication.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.borrowings.entity.BorrowingStatus;
import org.example.project_java_webapplication.modules.borrowings.repository.BorrowingRecordRepository;
import org.example.project_java_webapplication.modules.auth.repository.UserRepository;
import org.example.project_java_webapplication.modules.equipments.repository.EquipmentRepository;
import org.example.project_java_webapplication.modules.user.service.AdminUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final BorrowingRecordRepository borrowingRecordRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final AdminUserService adminUserService;

    @GetMapping
    public String admin() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("totalEquipments", equipmentRepository.count());
        model.addAttribute("activeBorrowings", borrowingRecordRepository.countByStatus(BorrowingStatus.DISPATCHED));
        model.addAttribute("totalLecturers", adminUserService.getAllLecturerEntities().size());
        model.addAttribute("recentTransactions", borrowingRecordRepository.findAll());
        model.addAttribute("topMentors", adminUserService.getAllLecturerEntities());
        return "admin/dashboard/dashboard";
    }
}

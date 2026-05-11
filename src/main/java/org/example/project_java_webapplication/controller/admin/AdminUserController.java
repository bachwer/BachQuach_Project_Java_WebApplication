package org.example.project_java_webapplication.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.user.dto.LecturerCreateDTO;
import org.example.project_java_webapplication.modules.user.dto.LecturerUpdateDTO;
import org.example.project_java_webapplication.modules.user.dto.StudentCreateDTO;
import org.example.project_java_webapplication.modules.user.entity.Lecturer;
import org.example.project_java_webapplication.modules.user.repository.DepartmentRepository;
import org.example.project_java_webapplication.modules.user.service.AdminUserService;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.borrowings.repository.BorrowingRecordRepository;
import org.example.project_java_webapplication.modules.evaluations.repository.StudentEvaluationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;
    private final DepartmentRepository departmentRepository;
    private final BorrowingRecordRepository borrowingRecordRepository;
    private final StudentEvaluationRepository studentEvaluationRepository;

    @GetMapping("/students")
    public String students(@RequestParam(required = false) String search, Model model) {
        model.addAttribute("students", adminUserService.searchStudents(search));
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("search", search);
        return "admin/users/students";
    }

    @PostMapping("/students/create")
    public String createStudent(@ModelAttribute StudentCreateDTO studentDTO, RedirectAttributes redirectAttributes) {
        try {
            adminUserService.createStudent(studentDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Student created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/admin/users/students";
    }

    @GetMapping("/lecturers")
    public String lecturers(@RequestParam(required = false) String search, Model model) {
        model.addAttribute("lecturers", adminUserService.searchLecturerEntities(search));
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("search", search);
        return "admin/users/lecturers";
    }

    @PostMapping("/users/lecturers/create") // Keep existing mapping if needed, but I'll use /lecturers/create
    public String createLecturer(@ModelAttribute LecturerCreateDTO lecturerDTO, RedirectAttributes redirectAttributes) {
        try {
            adminUserService.createLecturer(lecturerDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Lecturer created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/admin/users/lecturers";
    }

    @GetMapping("/lecturers/update/{id}")
    public String editLecturerPage(@PathVariable Long id, Model model) {
        Lecturer lecturer = adminUserService.getAllLecturerEntities()
                .stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        LecturerUpdateDTO dto = getLecturerUpdateDTO(lecturer);
        model.addAttribute("lecturer", dto);
        model.addAttribute("id", id);
        model.addAttribute("departments", departmentRepository.findAll());
        return "admin/users/editLecturer";
    }

    @PostMapping("/lecturers/update/{id}")
    public String updateLecturer(@ModelAttribute LecturerUpdateDTO lecturerDTO, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            adminUserService.updateLecturer(id, lecturerDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Lecturer updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/admin/users/lecturers";
    }

    @GetMapping("/students/{id}/details")
    public String studentDetails(@PathVariable Long id, Model model) {
        User student = adminUserService.getStudentById(id);
        model.addAttribute("student", student);
        model.addAttribute("borrowings", borrowingRecordRepository.findByStudentId(id));
        model.addAttribute("evaluations", studentEvaluationRepository.findByStudentId(id));
        return "admin/users/student-details";
    }

    private LecturerUpdateDTO getLecturerUpdateDTO(Lecturer lecturer) {
        LecturerUpdateDTO dto = new LecturerUpdateDTO();
        dto.setEmail(lecturer.getUser().getEmail());
        dto.setFullName(lecturer.getUser().getProfile().getFullName());
        dto.setPhone(lecturer.getUser().getProfile().getPhone());
        dto.setAvatarUrl(lecturer.getUser().getProfile().getAvatarUrl());
        dto.setDepartmentId(lecturer.getDepartment().getId());
        dto.setAcademicRank(lecturer.getAcademicRank());
        dto.setSpecialization(lecturer.getSpecialization());
        return dto;
    }
}

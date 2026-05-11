package org.example.project_java_webapplication.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.user.entity.Department;
import org.example.project_java_webapplication.modules.user.repository.DepartmentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/departments")
@RequiredArgsConstructor
public class AdminDepartmentController {

    private final DepartmentRepository departmentRepository;

    @GetMapping
    public String departments(Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
        return "admin/departments/list";
    }

    @PostMapping("/create")
    public String createDepartment(@ModelAttribute Department department, RedirectAttributes redirectAttributes) {
        try {
            departmentRepository.save(department);
            redirectAttributes.addFlashAttribute("successMessage", "Department created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating department: " + e.getMessage());
        }
        return "redirect:/admin/departments";
    }

    @PostMapping("/edit")
    public String editDepartment(@ModelAttribute Department department, RedirectAttributes redirectAttributes) {
        try {
            Department existing = departmentRepository.findById(department.getId()).orElseThrow();
            existing.setName(department.getName());
            existing.setCode(department.getCode());
            existing.setDescription(department.getDescription());
            departmentRepository.save(existing);
            redirectAttributes.addFlashAttribute("successMessage", "Department updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating department: " + e.getMessage());
        }
        return "redirect:/admin/departments";
    }

    @PostMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            departmentRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Department deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting department: " + e.getMessage());
        }
        return "redirect:/admin/departments";
    }
}

package org.example.project_java_webapplication.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.equipments.enity.Category;
import org.example.project_java_webapplication.modules.equipments.repository.CategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/equipments/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    public String categories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/equipments/categories";
    }

    @PostMapping("/create")
    public String createCategory(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        try {
            categoryRepository.save(category);
            redirectAttributes.addFlashAttribute("successMessage", "Category created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating category: " + e.getMessage());
        }
        return "redirect:/admin/equipments/categories";
    }

    @PostMapping("/edit")
    public String editCategory(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        try {
            Category existing = categoryRepository.findById(category.getId()).orElseThrow();
            existing.setName(category.getName());
            existing.setDescription(category.getDescription());
            categoryRepository.save(existing);
            redirectAttributes.addFlashAttribute("successMessage", "Category updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating category: " + e.getMessage());
        }
        return "redirect:/admin/equipments/categories";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Category deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting category: " + e.getMessage());
        }
        return "redirect:/admin/equipments/categories";
    }
}

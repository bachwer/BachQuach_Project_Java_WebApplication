package org.example.project_java_webapplication.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.equipments.dto.EquipmentRequestDTO;
import org.example.project_java_webapplication.modules.equipments.service.EquipmentService;
import org.example.project_java_webapplication.modules.equipments.repository.CategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/equipments")
@RequiredArgsConstructor
public class AdminEquipmentController {

    private final EquipmentService equipmentService;
    private final CategoryRepository categoryRepository;

    // =========================
    // LIST
    // =========================
    @GetMapping
    public String listEquipments(@RequestParam(required = false) String search, Model model) {
        model.addAttribute(
                "equipments",
                equipmentService.searchByName(search)
        );
        model.addAttribute("search", search);
        return "admin/equipments/list";
    }

    // =========================
    // SHOW CREATE FORM
    // =========================
    @GetMapping("/create")
    public String createForm(Model model) {

        model.addAttribute(
                "equipment",
                new EquipmentRequestDTO()
        );
        model.addAttribute("categories", categoryRepository.findAll());

        return "admin/equipments/create";
    }

    // =========================
    // HANDLE CREATE
    // =========================
    @PostMapping("/create")
    public String createEquipment(
            @ModelAttribute("equipment")
            EquipmentRequestDTO request
    ) {

        equipmentService.create(request);

        return "redirect:/admin/equipments";
    }

    // =========================
    // SHOW EDIT FORM
    // =========================
    @GetMapping("/edit/{id}")
    public String editForm(
            @PathVariable Long id,
            Model model
    ) {

        model.addAttribute(
                "equipment",
                equipmentService.getById(id)
        );
        model.addAttribute("categories", categoryRepository.findAll());

        return "admin/equipments/edit";
    }

    // =========================
    // HANDLE UPDATE
    // =========================
    @PostMapping("/update/{id}")
    public String updateEquipment(
            @PathVariable Long id,
            @ModelAttribute("equipment")
            EquipmentRequestDTO request
    ) {

        equipmentService.update(id, request);

        return "redirect:/admin/equipments";
    }

    // =========================
    // DELETE
    // =========================
    @GetMapping("/delete/{id}")
    public String deleteEquipment(
            @PathVariable Long id
    ) {

        equipmentService.delete(id);

        return "redirect:/admin/equipments";
    }

}
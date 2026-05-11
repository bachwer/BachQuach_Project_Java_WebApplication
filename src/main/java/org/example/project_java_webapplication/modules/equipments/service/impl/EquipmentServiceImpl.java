package org.example.project_java_webapplication.modules.equipments.service.impl;
import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.equipments.dto.EquipmentRequestDTO;
import org.example.project_java_webapplication.modules.equipments.dto.EquipmentResponseDTO;
import org.example.project_java_webapplication.modules.equipments.enity.Equipment;
import org.example.project_java_webapplication.modules.equipments.repository.EquipmentRepository;
import org.example.project_java_webapplication.modules.equipments.service.EquipmentService;
import org.springframework.stereotype.Service;
import org.example.project_java_webapplication.modules.equipments.enity.Category;
import org.example.project_java_webapplication.modules.equipments.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {
    private final CategoryRepository categoryRepository;
    private final EquipmentRepository equipmentRepository;

    @Override
    public EquipmentResponseDTO create(EquipmentRequestDTO request) {

        if (equipmentRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("Equipment code already exists");
        }

        Category category = null;

        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        }

        Equipment equipment = Equipment.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .category(category)
                .quantityInStock(request.getQuantityInStock())
                .currentStock(request.getCurrentStock())
                .isActive(request.getIsActive())
                .imageUrl(request.getImageUrl())
                .build();

        Equipment saved = equipmentRepository.save(equipment);

        return mapToDTO(saved);
    }

    @Override
    public EquipmentResponseDTO update(Long id, EquipmentRequestDTO request) {

        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));

        Category category = null;

        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        }

        equipment.setCode(request.getCode());
        equipment.setName(request.getName());
        equipment.setDescription(request.getDescription());
        equipment.setCategory(category);
        equipment.setQuantityInStock(request.getQuantityInStock());
        equipment.setCurrentStock(request.getCurrentStock());
        equipment.setIsActive(request.getIsActive());
        equipment.setImageUrl(request.getImageUrl());

        Equipment updated = equipmentRepository.save(equipment);

        return mapToDTO(updated);
    }

    @Override
    public EquipmentResponseDTO getById(Long id) {

        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));

        return mapToDTO(equipment);
    }

    @Override
    public List<EquipmentResponseDTO> getAll() {
        return equipmentRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<EquipmentResponseDTO> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAll();
        }
        return equipmentRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {

        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));

        // SOFT DELETE
        equipment.setIsActive(false);

        equipmentRepository.save(equipment);
    }

    private EquipmentResponseDTO mapToDTO(Equipment equipment) {

        return EquipmentResponseDTO.builder()
                .id(equipment.getId())
                .code(equipment.getCode())
                .name(equipment.getName())
                .description(equipment.getDescription())

                .categoryId(
                        equipment.getCategory() != null
                                ? equipment.getCategory().getId()
                                : null
                )

                .categoryName(
                        equipment.getCategory() != null
                                ? equipment.getCategory().getName()
                                : null
                )

                .quantityInStock(equipment.getQuantityInStock())
                .currentStock(equipment.getCurrentStock())
                .isActive(equipment.getIsActive())
                .imageUrl(equipment.getImageUrl())
                .createdAt(equipment.getCreatedAt())
                .updatedAt(equipment.getUpdatedAt())
                .build();
    }
}
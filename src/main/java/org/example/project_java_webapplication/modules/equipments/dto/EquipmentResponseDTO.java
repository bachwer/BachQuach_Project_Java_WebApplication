package org.example.project_java_webapplication.modules.equipments.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentResponseDTO {

    private Long id;

    private String code;

    private String name;

    private String description;

    // thêm
    private Long categoryId;

    // thêm
    private String categoryName;

    private Integer quantityInStock;

    private Integer currentStock;

    private Boolean isActive;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
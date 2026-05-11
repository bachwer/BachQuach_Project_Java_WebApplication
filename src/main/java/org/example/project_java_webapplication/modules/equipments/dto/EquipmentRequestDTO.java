package org.example.project_java_webapplication.modules.equipments.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentRequestDTO {

    private String code;

    private String name;

    private String description;

    // thêm
    private Long categoryId;

    private Integer quantityInStock;

    private Integer currentStock;

    private Boolean isActive;

    private String imageUrl;
}
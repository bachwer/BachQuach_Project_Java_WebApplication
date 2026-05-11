package org.example.project_java_webapplication.modules.borrowings.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BorrowItemDTO {
    private Long equipmentId;
    private Integer quantity;
    private LocalDate expectedReturnDate;
}

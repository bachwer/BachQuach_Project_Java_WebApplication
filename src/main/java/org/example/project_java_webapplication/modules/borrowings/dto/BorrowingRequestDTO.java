package org.example.project_java_webapplication.modules.borrowings.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BorrowingRequestDTO {
    private Long studentId;
    private List<BorrowItemDTO> items;
    private LocalDate expectedReturnDate;
}

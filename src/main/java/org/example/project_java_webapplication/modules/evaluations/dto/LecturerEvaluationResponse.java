package org.example.project_java_webapplication.modules.evaluations.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LecturerEvaluationResponse {

    private Long id;

    private Long mentoringSessionId;

    private Long lecturerId;
    private String lecturerName;

    private Long studentId;
    private String studentName;

    private Integer rating;

    private String feedback;

    private LocalDateTime createdAt;
}
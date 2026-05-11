package org.example.project_java_webapplication.modules.evaluations.dto;

import lombok.Builder;
import lombok.Data;
import org.example.project_java_webapplication.modules.evaluations.entity.PerformanceLevel;

import java.time.LocalDateTime;

@Data
@Builder
public class StudentEvaluationResponse {

    private Long id;

    private Long mentoringSessionId;

    private Long lecturerId;
    private String lecturerName;

    private Long studentId;
    private String studentName;

    private PerformanceLevel performanceLevel;

    private String evaluationComment;

    private String recommendation;

    private LocalDateTime createdAt;
}
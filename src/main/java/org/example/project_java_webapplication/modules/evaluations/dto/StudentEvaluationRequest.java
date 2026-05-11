package org.example.project_java_webapplication.modules.evaluations.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.project_java_webapplication.modules.evaluations.entity.PerformanceLevel;

@Data
public class StudentEvaluationRequest {

    @NotNull
    private Long mentoringSessionId;

    @NotNull
    private Long lecturerId;

    @NotNull
    private Long studentId;

    @NotNull
    private PerformanceLevel performanceLevel;

    private String evaluationComment;

    private String recommendation;
}
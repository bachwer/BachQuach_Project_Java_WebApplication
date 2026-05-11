package org.example.project_java_webapplication.modules.evaluations.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LecturerEvaluationRequest {

    @NotNull
    private Long mentoringSessionId;

    @NotNull
    private Long lecturerId;

    @NotNull
    private Long studentId;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    private String feedback;
}
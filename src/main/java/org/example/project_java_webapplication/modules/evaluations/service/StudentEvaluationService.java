package org.example.project_java_webapplication.modules.evaluations.service;

import org.example.project_java_webapplication.modules.evaluations.dto.StudentEvaluationRequest;
import org.example.project_java_webapplication.modules.evaluations.dto.StudentEvaluationResponse;

import java.util.List;

public interface StudentEvaluationService {

    StudentEvaluationResponse create(
            StudentEvaluationRequest request
    );

    StudentEvaluationResponse update(
            Long id,
            StudentEvaluationRequest request
    );

    StudentEvaluationResponse getById(Long id);

    List<StudentEvaluationResponse> getAll();

    void delete(Long id);
}
package org.example.project_java_webapplication.modules.evaluations.service;


import org.example.project_java_webapplication.modules.evaluations.dto.LecturerEvaluationRequest;
import org.example.project_java_webapplication.modules.evaluations.dto.LecturerEvaluationResponse;

import java.util.List;

public interface LecturerEvaluationService {

    LecturerEvaluationResponse create(LecturerEvaluationRequest request);

    LecturerEvaluationResponse update(
            Long id,
            LecturerEvaluationRequest request
    );

    LecturerEvaluationResponse getById(Long id);

    List<LecturerEvaluationResponse> getAll();

    void delete(Long id);
}
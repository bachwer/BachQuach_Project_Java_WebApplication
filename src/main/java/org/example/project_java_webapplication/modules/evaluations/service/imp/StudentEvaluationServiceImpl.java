package org.example.project_java_webapplication.modules.evaluations.service.imp;



import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.auth.repository.UserRepository;
import org.example.project_java_webapplication.modules.evaluations.dto.StudentEvaluationRequest;
import org.example.project_java_webapplication.modules.evaluations.dto.StudentEvaluationResponse;
import org.example.project_java_webapplication.modules.evaluations.entity.StudentEvaluation;
import org.example.project_java_webapplication.modules.evaluations.repository.StudentEvaluationRepository;
import org.example.project_java_webapplication.modules.evaluations.service.StudentEvaluationService;
import org.example.project_java_webapplication.modules.mentoring.entity.MentoringSession;
import org.example.project_java_webapplication.modules.mentoring.repository.MentoringSessionRepository;
import org.example.project_java_webapplication.modules.user.entity.Lecturer;
import org.example.project_java_webapplication.modules.user.repository.LecturerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentEvaluationServiceImpl
        implements StudentEvaluationService {

    private final StudentEvaluationRepository studentEvaluationRepository;
    private final MentoringSessionRepository mentoringSessionRepository;
    private final LecturerRepository lecturerRepository;
    private final UserRepository userRepository;

    @Override
    public StudentEvaluationResponse create(StudentEvaluationRequest request) {

        if (studentEvaluationRepository.existsByMentoringSessionId(request.getMentoringSessionId())) {
            throw new RuntimeException("Evaluation already exists for this mentoring session");
        }

        MentoringSession mentoringSession = mentoringSessionRepository.findById(request.getMentoringSessionId()).orElseThrow(() -> new RuntimeException("Mentoring session not found"));

        User lecturer = userRepository.findById(request.getLecturerId()).orElseThrow(() -> new RuntimeException("Lecturer not found"));

        User student = userRepository.findById(request.getStudentId()).orElseThrow(() -> new RuntimeException("Student not found"));

        StudentEvaluation evaluation = StudentEvaluation.builder()
                .mentoringSession(mentoringSession)
                .lecturer(lecturer)
                .student(student)
                .performanceLevel(request.getPerformanceLevel())
                .evaluationComment(request.getEvaluationComment())
                .recommendation(request.getRecommendation())
                .build();

        studentEvaluationRepository.save(evaluation);

        return mapToResponse(evaluation);
    }

    @Override
    public StudentEvaluationResponse update(
            Long id,
            StudentEvaluationRequest request
    ) {

        StudentEvaluation evaluation =
                studentEvaluationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Evaluation not found"));

        evaluation.setPerformanceLevel(
                request.getPerformanceLevel()
        );

        evaluation.setEvaluationComment(
                request.getEvaluationComment()
        );

        evaluation.setRecommendation(
                request.getRecommendation()
        );

        studentEvaluationRepository.save(evaluation);

        return mapToResponse(evaluation);
    }

    @Override
    public StudentEvaluationResponse getById(Long id) {

        StudentEvaluation evaluation =
                studentEvaluationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Evaluation not found"));

        return mapToResponse(evaluation);
    }

    @Override
    public List<StudentEvaluationResponse> getAll() {

        return studentEvaluationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {

        StudentEvaluation evaluation =
                studentEvaluationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Evaluation not found"));

        studentEvaluationRepository.delete(evaluation);
    }

    private StudentEvaluationResponse mapToResponse(
            StudentEvaluation entity
    ) {

        return StudentEvaluationResponse.builder()
                .id(entity.getId())
                .mentoringSessionId(entity.getMentoringSession().getId())
                .lecturerId(entity.getLecturer().getId())
                .lecturerName(entity.getLecturer().getFullName())
                .studentId(entity.getStudent().getId())
                .studentName(entity.getStudent().getFullName())
                .performanceLevel(entity.getPerformanceLevel())
                .evaluationComment(entity.getEvaluationComment())
                .recommendation(entity.getRecommendation())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
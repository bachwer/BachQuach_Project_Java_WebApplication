package org.example.project_java_webapplication.modules.evaluations.service.imp;


import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.auth.repository.UserRepository;
import org.example.project_java_webapplication.modules.evaluations.dto.LecturerEvaluationRequest;
import org.example.project_java_webapplication.modules.evaluations.dto.LecturerEvaluationResponse;
import org.example.project_java_webapplication.modules.evaluations.entity.LecturerEvaluation;
import org.example.project_java_webapplication.modules.evaluations.repository.LecturerEvaluationRepository;
import org.example.project_java_webapplication.modules.evaluations.service.LecturerEvaluationService;
import org.example.project_java_webapplication.modules.mentoring.entity.MentoringSession;
import org.example.project_java_webapplication.modules.mentoring.repository.MentoringSessionRepository;
import org.example.project_java_webapplication.modules.user.entity.Lecturer;
import org.example.project_java_webapplication.modules.user.repository.LecturerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LecturerEvaluationServiceImpl  implements LecturerEvaluationService {



    private final LecturerEvaluationRepository lecturerEvaluationRepository;

    private final MentoringSessionRepository mentoringSessionRepository;

    private final LecturerRepository lecturerRepository;

    private final UserRepository userRepository;



    @Override
    public LecturerEvaluationResponse create(LecturerEvaluationRequest request) {

        if(lecturerEvaluationRepository.existsByMentoringSessionId(request.getMentoringSessionId())){
            throw new RuntimeException("Evaluation already exists for this mentoring session");
        }

        MentoringSession mentoringSession = mentoringSessionRepository.findById(request.getMentoringSessionId()).orElseThrow(() -> new RuntimeException("Mentoring session not found!"));

        User lecturer = userRepository.findById(request.getLecturerId()).orElseThrow(() -> new RuntimeException("Lecturer not found"));

        User student = userRepository.findById(request.getStudentId()).orElseThrow(() -> new RuntimeException("Student not found"));

        LecturerEvaluation evaluation = LecturerEvaluation.builder()
                .mentoringSession(mentoringSession)
                .lecturer(lecturer)
                .student(student)
                .rating(request.getRating())
                .feedback(request.getFeedback())
                .build();

        lecturerEvaluationRepository.save(evaluation);

        return mapToResponse(evaluation);

    }

    @Override
    public LecturerEvaluationResponse update(Long id, LecturerEvaluationRequest request) {
        LecturerEvaluation evaluation = lecturerEvaluationRepository.findById(id).orElseThrow(() -> new RuntimeException("Evaluation not found !"));

        evaluation.setRating(request.getRating());

        evaluation.setFeedback(request.getFeedback());

        lecturerEvaluationRepository.save(evaluation);

        return mapToResponse(evaluation);

    }

    @Override
    public LecturerEvaluationResponse getById(Long id) {
        LecturerEvaluation evaluation = lecturerEvaluationRepository.findById(id).orElseThrow(() -> new RuntimeException("Evaluation not found !"));

        return mapToResponse(evaluation);
    }


    @Override

    public List<LecturerEvaluationResponse> getAll() {

        return lecturerEvaluationRepository.findAll()

                .stream()

                .map(this::mapToResponse)

                .toList();

    }

    @Override

    public void delete(Long id) {

        LecturerEvaluation evaluation =

                lecturerEvaluationRepository.findById(id)

                        .orElseThrow(() ->

                                new RuntimeException("Evaluation not found"));

        lecturerEvaluationRepository.delete(evaluation);

    }
    private LecturerEvaluationResponse mapToResponse(
            LecturerEvaluation entity
    ) {

        return LecturerEvaluationResponse.builder()
                .id(entity.getId())
                .mentoringSessionId(entity.getMentoringSession().getId())
                .lecturerId(entity.getLecturer().getId())
                .lecturerName(entity.getLecturer().getFullName())
                .studentId(entity.getStudent().getId())
                .studentName(entity.getStudent().getFullName())
                .rating(entity.getRating())
                .feedback(entity.getFeedback())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}

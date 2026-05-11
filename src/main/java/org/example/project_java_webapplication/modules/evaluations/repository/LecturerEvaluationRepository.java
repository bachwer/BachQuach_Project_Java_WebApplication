package org.example.project_java_webapplication.modules.evaluations.repository;

import org.example.project_java_webapplication.modules.evaluations.entity.LecturerEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LecturerEvaluationRepository extends JpaRepository<LecturerEvaluation, Long> {
    Optional<LecturerEvaluation> findByMentoringSessionId(Long mentoringSessionId);

    List<LecturerEvaluation> findByLecturerId(Long lecturerId);

    List<LecturerEvaluation> findByStudentId(Long studentId);

    boolean existsByMentoringSessionId(Long mentoringSessionId);
}

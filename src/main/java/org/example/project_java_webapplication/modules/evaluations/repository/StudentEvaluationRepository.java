package org.example.project_java_webapplication.modules.evaluations.repository;


import org.example.project_java_webapplication.modules.evaluations.entity.StudentEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentEvaluationRepository
        extends JpaRepository<StudentEvaluation, Long> {

    Optional<StudentEvaluation> findByMentoringSessionId(Long mentoringSessionId);

    List<StudentEvaluation> findByLecturerId(Long lecturerId);

    List<StudentEvaluation> findByStudentId(Long studentId);

    boolean existsByMentoringSessionId(Long mentoringSessionId);
}
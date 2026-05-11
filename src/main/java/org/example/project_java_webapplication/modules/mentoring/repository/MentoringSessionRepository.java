package org.example.project_java_webapplication.modules.mentoring.repository;

import org.example.project_java_webapplication.modules.mentoring.entity.MentoringSession;
import org.example.project_java_webapplication.modules.mentoring.entity.MentoringStatus;
import org.example.project_java_webapplication.modules.user.entity.Lecturer;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface MentoringSessionRepository extends JpaRepository<MentoringSession, Long> {
    List<MentoringSession> findByStudent(User student);
    List<MentoringSession> findByLecturerAndStatus(User lecturer, MentoringStatus status);
    List<MentoringSession> findByLecturerAndStatusInOrderBySessionDateDescStartTimeDesc(User lecturer, List<MentoringStatus> statuses);
    List<MentoringSession> findByLecturerAndSessionDate(User lecturer, LocalDate date);
    long countByLecturerAndStatus(User lecturer, MentoringStatus status);

    @Query("SELECT m FROM MentoringSession m " +
           "LEFT JOIN FETCH m.lecturer l " +
           "LEFT JOIN FETCH l.profile p " +
           "LEFT JOIN FETCH m.student s " +
           "LEFT JOIN FETCH s.profile sp " +
           "WHERE m.id = :id")
    java.util.Optional<MentoringSession> findByIdWithDetails(Long id);

    @Query("SELECT COUNT(m) > 0 FROM MentoringSession m WHERE m.lecturer = :lecturer " +
           "AND m.sessionDate = :date AND m.startTime = :startTime " +
           "AND m.status IN (org.example.project_java_webapplication.modules.mentoring.entity.MentoringStatus.PENDING, " +
           "org.example.project_java_webapplication.modules.mentoring.entity.MentoringStatus.CONFIRMED)")
    boolean isSlotOccupied(User lecturer, LocalDate date, LocalTime startTime);
}

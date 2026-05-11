package org.example.project_java_webapplication.modules.borrowings.repository;

import org.example.project_java_webapplication.modules.borrowings.entity.BorrowingRecord;
import org.example.project_java_webapplication.modules.borrowings.entity.BorrowingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    List<BorrowingRecord> findByStudentId(Long studentId);
    List<BorrowingRecord> findByStatus(BorrowingStatus status);
    long countByStatus(BorrowingStatus status);
}

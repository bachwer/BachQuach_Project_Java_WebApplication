package org.example.project_java_webapplication.modules.borrowings.repository;

import org.example.project_java_webapplication.modules.borrowings.entity.BorrowingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowingDetailRepository extends JpaRepository<BorrowingDetail, Long> {
}

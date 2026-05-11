package org.example.project_java_webapplication.modules.borrowings.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.project_java_webapplication.modules.auth.entity.User;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "borrowing_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private BorrowingStatus status = BorrowingStatus.PENDING_DISPATCH;

    @Column(name = "borrow_date")
    private LocalDateTime borrowDate;

    @Column(name = "expected_return_date")
    private LocalDate expectedReturnDate;

    @Column(name = "actual_return_date")
    private LocalDate actualReturnDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "borrowingRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BorrowingDetail> details = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (borrowDate == null) {
            borrowDate = LocalDateTime.now();
        }
    }

    public void addDetail(BorrowingDetail detail) {
        details.add(detail);
        detail.setBorrowingRecord(this);
    }
}

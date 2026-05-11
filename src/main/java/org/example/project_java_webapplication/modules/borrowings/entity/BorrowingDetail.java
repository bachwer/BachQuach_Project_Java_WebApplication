package org.example.project_java_webapplication.modules.borrowings.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.project_java_webapplication.modules.equipments.enity.Equipment;

@Entity
@Table(name = "borrowing_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowingDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrowing_record_id", nullable = false)
    private BorrowingRecord borrowingRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @Column(nullable = false)
    private Integer quantity;
}

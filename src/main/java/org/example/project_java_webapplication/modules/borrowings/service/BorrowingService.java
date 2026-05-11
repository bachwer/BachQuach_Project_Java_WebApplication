package org.example.project_java_webapplication.modules.borrowings.service;

import lombok.RequiredArgsConstructor;
import org.example.project_java_webapplication.modules.auth.entity.User;
import org.example.project_java_webapplication.modules.auth.repository.UserRepository;
import org.example.project_java_webapplication.modules.borrowings.dto.BorrowItemDTO;
import org.example.project_java_webapplication.modules.borrowings.dto.BorrowingRequestDTO;
import org.example.project_java_webapplication.modules.borrowings.entity.BorrowingDetail;
import org.example.project_java_webapplication.modules.borrowings.entity.BorrowingRecord;
import org.example.project_java_webapplication.modules.borrowings.entity.BorrowingStatus;
import org.example.project_java_webapplication.modules.borrowings.repository.BorrowingRecordRepository;
import org.example.project_java_webapplication.modules.equipments.enity.Equipment;
import org.example.project_java_webapplication.modules.equipments.repository.EquipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BorrowingService {

    private final BorrowingRecordRepository borrowingRecordRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;

    @Transactional
    public BorrowingRecord requestBorrowing(BorrowingRequestDTO request) {

        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));


        BorrowingRecord record = BorrowingRecord.builder()
                .student(student)
                .expectedReturnDate(request.getExpectedReturnDate())
                .status(BorrowingStatus.PENDING_DISPATCH)
                .build();

        for (BorrowItemDTO itemDto : request.getItems()) {

            Equipment equipment = equipmentRepository.findById(itemDto.getEquipmentId())
                    .orElseThrow(() -> new RuntimeException("Equipment not found"));

            if (equipment.getCurrentStock() < itemDto.getQuantity()) {
                throw new RuntimeException(
                        "Not enough stock for equipment: " + equipment.getName()
                );
            }

            BorrowingDetail detail = BorrowingDetail.builder()
                    .equipment(equipment)
                    .quantity(itemDto.getQuantity())
                    .build();

            record.addDetail(detail);
        }
        System.out.println(record +  "=========");

        return borrowingRecordRepository.save(record);
    }

    @Transactional
    public BorrowingRecord returnEquipment(Long recordId){
        BorrowingRecord record = borrowingRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Borrowing record not found"));

        if (record.getStatus() != BorrowingStatus.DISPATCHED) {
            throw new RuntimeException("Only DISPATCHED borrowings can be approved");
        }

        record.setStatus(BorrowingStatus.PENDING_RETURN);
        return borrowingRecordRepository.save(record);
    }


    @Transactional
    public BorrowingRecord approveBorrowing(Long recordId, Long adminId) {
        BorrowingRecord record = borrowingRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Borrowing record not found"));

        if (record.getStatus() != BorrowingStatus.PENDING_DISPATCH) {
            throw new RuntimeException("Only pending borrowings can be approved");
        }

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin user not found"));

        record.setStatus(BorrowingStatus.DISPATCHED);
        record.setApprovedBy(admin);

        // Decrease stock on approval
        for (BorrowingDetail detail : record.getDetails()) {
            Equipment equipment = detail.getEquipment();
            if (equipment.getCurrentStock() < detail.getQuantity()) {
                throw new RuntimeException("Not enough stock to approve for equipment: " + equipment.getName());
            }
            equipment.setCurrentStock(equipment.getCurrentStock() - detail.getQuantity());
            equipmentRepository.save(equipment);
        }
        
        return borrowingRecordRepository.save(record);
    }

    @Transactional
    public BorrowingRecord rejectBorrowing(Long recordId, Long adminId) {
        BorrowingRecord record = borrowingRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Borrowing record not found"));

        if (record.getStatus() != BorrowingStatus.PENDING_DISPATCH) {
            throw new RuntimeException("Only pending borrowings can be rejected");
        }

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin user not found"));

        record.setStatus(BorrowingStatus.REJECTED);
        record.setApprovedBy(admin);

        // No need to restore stock as it wasn't deducted during request phase

        return borrowingRecordRepository.save(record);
    }

    @Transactional
    public BorrowingRecord returnBorrowing(Long recordId) {
        BorrowingRecord record = borrowingRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Borrowing record not found"));

        if (record.getStatus() != BorrowingStatus.PENDING_RETURN) {
            throw new RuntimeException("Only dispatched borrowings can be returned");
        }

        record.setStatus(BorrowingStatus.RETURNED);
        record.setActualReturnDate(LocalDate.now());

        // Restore stock
        for (BorrowingDetail detail : record.getDetails()) {
            Equipment equipment = detail.getEquipment();
            equipment.setCurrentStock(equipment.getCurrentStock() + detail.getQuantity());
            equipmentRepository.save(equipment);
        }

        return borrowingRecordRepository.save(record);
    }
}

package org.example.project_java_webapplication.modules.equipments.repository;

import org.example.project_java_webapplication.modules.equipments.enity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findByIsActiveTrue();

    Optional<Equipment> findByCode(String code);

    boolean existsByCode(String code);

    List<Equipment> findByNameContainingIgnoreCase(String name);
}
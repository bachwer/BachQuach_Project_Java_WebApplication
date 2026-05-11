package org.example.project_java_webapplication.modules.equipments.service;

import org.example.project_java_webapplication.modules.equipments.dto.EquipmentRequestDTO;
import org.example.project_java_webapplication.modules.equipments.dto.EquipmentResponseDTO;

import java.util.List;

public interface EquipmentService {

    EquipmentResponseDTO create(EquipmentRequestDTO request);

    EquipmentResponseDTO update(Long id, EquipmentRequestDTO request);

    EquipmentResponseDTO getById(Long id);

    List<EquipmentResponseDTO> getAll();

    List<EquipmentResponseDTO> searchByName(String name);

    void delete(Long id);
}
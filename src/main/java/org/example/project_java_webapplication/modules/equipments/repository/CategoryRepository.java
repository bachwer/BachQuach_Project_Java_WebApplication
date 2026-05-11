package org.example.project_java_webapplication.modules.equipments.repository;

import org.example.project_java_webapplication.modules.equipments.enity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
package ru.ddc.em.persistence.dao;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.ddc.em.persistence.model.Department;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @EntityGraph(attributePaths = "vacancies")
    List<Department> findAll();
}
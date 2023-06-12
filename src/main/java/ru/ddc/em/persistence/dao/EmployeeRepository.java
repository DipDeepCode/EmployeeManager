package ru.ddc.em.persistence.dao;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.ddc.em.persistence.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @EntityGraph(attributePaths = "vacancy")
    List<Employee> findAll();

    @EntityGraph(attributePaths = "vacancy")
    Page<Employee> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "vacancy")
    Optional<Employee> findById(@Nonnull Long personnelNumber);

    @EntityGraph(attributePaths = "vacancy")
    List<Employee> findByVacancyNull();

    @EntityGraph(attributePaths = "vacancy")
    List<Employee> findByVacancyNull(Sort sort);
}
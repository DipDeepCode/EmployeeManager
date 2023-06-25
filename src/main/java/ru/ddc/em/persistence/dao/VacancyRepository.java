package ru.ddc.em.persistence.dao;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ddc.em.persistence.model.Vacancy;

import java.util.List;
import java.util.Optional;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {

    @EntityGraph(attributePaths = {"department", "employee"})
    List<Vacancy> findAll();

    @EntityGraph(attributePaths = {"department", "employee"})
    Page<Vacancy> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"department", "employee"})
    List<Vacancy> findByDepartmentId(@Nonnull Long departmentId);

    @EntityGraph(attributePaths = {"department", "employee"})
    Page<Vacancy> findByDepartmentId(@Nonnull Long departmentId, Pageable pageable);

    @EntityGraph(attributePaths = {"department", "employee"})
    Optional<Vacancy> findById(@Nonnull Long id);
}
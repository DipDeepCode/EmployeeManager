package ru.ddc.em.persistence.dao;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ddc.em.persistence.entity.Employee;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

//    @EntityGraph(attributePaths = "vacancy")
//    List<Employee> findAll();

//    @EntityGraph(attributePaths = "vacancy")
//    Page<Employee> findAll(Pageable pageable);

//    @EntityGraph(attributePaths = "vacancy")
//    List<Employee> findByVacancyNull();

//    @EntityGraph(attributePaths = "vacancy")
//    List<Employee> findByVacancyNull(Sort sort);
}
package ru.ddc.em.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ddc.em.persistence.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
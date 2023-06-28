package ru.ddc.em.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.ddc.em.persistence.model.Employee;
import ru.ddc.em.persistence.dao.EmployeeRepository;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Page<Employee> findAll(Integer pageNo, Integer pageSize, Sort sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        return findAll(pageable);
    }
    
    public Page<Employee> findAll(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Employee.defaultSort);
        return findAll(pageable);
    }

    public Page<Employee> findAll(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    public Employee findById(Long personnelNumber) {
        return employeeRepository.findById(personnelNumber).orElseThrow();
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public List<Employee> findAllNotAssignedToAnyDepartment() {
        return employeeRepository.findByVacancyNull();
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void deleteById(Long personnelNumber) {
        employeeRepository.deleteById(personnelNumber);
    }
}

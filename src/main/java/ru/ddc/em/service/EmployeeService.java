package ru.ddc.em.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ddc.em.exceptions.DeleteEntityException;
import ru.ddc.em.persistence.dao.VacancyRepository;
import ru.ddc.em.persistence.entity.Employee;
import ru.ddc.em.persistence.dao.EmployeeRepository;
import ru.ddc.em.persistence.entity.Vacancy;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final VacancyRepository vacancyRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           VacancyRepository vacancyRepository) {
        this.employeeRepository = employeeRepository;
        this.vacancyRepository = vacancyRepository;
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
        Employee employee = findById(personnelNumber);
        if (employee.getVacancy() == null) {
            employeeRepository.deleteById(personnelNumber);
        } else {
            throw new DeleteEntityException("Нельзя удалить сотрудника, который назначен на вакансию");
        }
    }

    @Transactional
    public void removeFromVacancy(Long personnelNumber) {
        Employee employee = employeeRepository.findById(personnelNumber).orElseThrow();
        Vacancy vacancy = employee.getVacancy();
        vacancy.setEmployee(null);
        employee.setVacancy(null);
        employeeRepository.save(employee);
        vacancyRepository.save(vacancy);
    }

    @Transactional
    public void transferToAnotherVacancy(Long personnelNumber, Long vacancyId) {
        Employee employee = employeeRepository.findById(personnelNumber).orElseThrow();
        employee.getVacancy().setEmployee(null);
        Vacancy vacancy = vacancyRepository.findById(vacancyId).orElseThrow();
        vacancy.setEmployee(employee);
        employeeRepository.save(employee);
        vacancyRepository.save(vacancy);
    }

    @Transactional
    public void appointToVacancy(Long personnelNumber, Long vacancyId) {
        Employee employee = employeeRepository.findById(personnelNumber).orElseThrow();
        Vacancy vacancy = vacancyRepository.findById(vacancyId).orElseThrow();
        vacancy.setEmployee(employee);
        employee.setVacancy(vacancy);
        employeeRepository.save(employee);
        vacancyRepository.save(vacancy);
    }
}

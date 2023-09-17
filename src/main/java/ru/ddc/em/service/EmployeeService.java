package ru.ddc.em.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ddc.em.persistence.dao.VacancyRepository;
import ru.ddc.em.persistence.entity.Department;
import ru.ddc.em.persistence.entity.Employee;
import ru.ddc.em.persistence.dao.EmployeeRepository;
import ru.ddc.em.persistence.entity.Vacancy;
import ru.ddc.em.utils.custommapper.CustomMapper;
import ru.ddc.em.web.dto.DepartmentDto;
import ru.ddc.em.web.dto.EmployeeDto;
import ru.ddc.em.web.dto.VacancyDto;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final VacancyRepository vacancyRepository;
    private final VacancyService vacancyService;
    private final CustomMapper mapper;
    @Value("${em.employee.page.size}") int pageSize;


    public EmployeeService(EmployeeRepository employeeRepository,
                           VacancyRepository vacancyRepository,
                           VacancyService vacancyService,
                           CustomMapper mapper) {
        this.employeeRepository = employeeRepository;
        this.vacancyRepository = vacancyRepository;
        this.vacancyService = vacancyService;
        this.mapper = mapper;
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
        return employeeRepository.findById(personnelNumber)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Сотрудник с табельным номером %05d не найден", personnelNumber))
                );
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void deleteById(Long personnelNumber) {
        Employee employee = findById(personnelNumber);
//        if (employee.getVacancy() == null) {
//            employeeRepository.deleteById(personnelNumber);
//        } else {
//            throw new DeleteEntityException("Нельзя удалить сотрудника, который назначен на вакансию");
//        }
    }

    @Transactional
    public void removeFromVacancy(Long personnelNumber) {
//        Employee employee = findById(personnelNumber);
//        Vacancy vacancy = employee.getVacancy();
//        vacancy.setEmployee(null);
//        employee.setVacancy(null);
//        employeeRepository.save(employee);
//        vacancyRepository.save(vacancy);
    }

    @Transactional
    public void transferToAnotherVacancy(Long personnelNumber, Long vacancyId) {
//        Employee employee = findById(personnelNumber);
//        employee.getVacancy().setEmployee(null);
//        Vacancy vacancy = vacancyService.findById(vacancyId);
//        vacancy.setEmployee(employee);
//        employeeRepository.save(employee);
//        vacancyRepository.save(vacancy);
    }

    @Transactional
    public void appointToVacancy(Long personnelNumber, Long vacancyId) {
//        Employee employee = findById(personnelNumber);
//        Vacancy vacancy = vacancyService.findById(vacancyId);
//        vacancy.setEmployee(employee);
//        employee.setVacancy(vacancy);
//        employeeRepository.save(employee);
//        vacancyRepository.save(vacancy);
    }


















    public EmployeeDto findDtoById(Long personnelNumber) {
        Employee employee = findById(personnelNumber);
        EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);

        Optional<Vacancy> vacancyOptional = vacancyRepository.findByEmployeePersonnelNumber(personnelNumber);
        if (vacancyOptional.isPresent()) {

            Vacancy vacancy = vacancyOptional.get();
            VacancyDto vacancyDto = mapper.map(vacancy, VacancyDto.class);
            employeeDto.setVacancyDto(vacancyDto);

            Department department = vacancy.getDepartment();
            DepartmentDto departmentDto = mapper.map(department, DepartmentDto.class);
            vacancyDto.setDepartmentDto(departmentDto);
        }
        return employeeDto;
    }

    public Page<EmployeeDto> findAllDto(Integer pageNo) {
        Page<Employee> employeePage = findAll(pageNo - 1, pageSize);
        return mapper.mapPage(employeePage, EmployeeDto.class);
    }








}

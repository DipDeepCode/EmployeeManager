package ru.ddc.em.persistence.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.ddc.em.persistence.entity.Employee;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.ddc.em.testutils.EmployeeTestBuilder.*;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void whenFindAllEmployees_thenListSizeEqualsFive() {
        List<Employee> employeeList = employeeRepository.findAll();
        assertEquals(5, employeeList.size());
    }

    @Test
    public void whenFindAllEmployeesPage_thenListSizeEqualsSomeValue() {
        Pageable pageable = PageRequest.of(0, 2,
                Sort.Direction.ASC, "patronymic", "firstname", "lastname");
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        showEmployeeList(employeePage);
        assertEquals(2, employeePage.getSize());
    }

    @Test
    public void whenFindEmployeeById_thenEmployeePersonnelNumberEqualsToSpecified() {
        Long employeePersonnelNumber = 1L;
        Employee employee = employeeRepository.findById(employeePersonnelNumber).orElseThrow();
        showEmployee(employee);
        assertEquals(employeePersonnelNumber, employee.getPersonnelNumber());
    }

    @Test
    public void whenFindMissingEmployeeById_thenReturnEmptyOptional() {
        Optional<Employee> optionalEmployee = employeeRepository.findById(100L);
        assertTrue(optionalEmployee.isEmpty());
    }

    @Test
    public void whenSaveEmployee_thenEmployeeCounterLagerByOne() {
        long counterBeforeSave = employeeRepository.count();
        Employee employee = aEmployee().build();
        showEmployee(employee);
        employeeRepository.saveAndFlush(employee);
        long counterAfterSave = employeeRepository.count();
        assertEquals(1, counterAfterSave - counterBeforeSave);
    }

    @Test
    public void whenDeleteEmployeeByIdWithVacancy_thenThrowException() {
        Long employeePersonnelNumber = 1L;
        Employee employee = employeeRepository.findById(employeePersonnelNumber).orElseThrow();
        employeeRepository.deleteById(employee.getPersonnelNumber());
        assertThrows(DataIntegrityViolationException.class, () -> employeeRepository.flush());
    }

    @Test
    public void whenDeleteEmployeeByIdWithoutVacancy_thenEmployeeCounterLessByOne() {
        Long employeePersonnelNumber = 5L;
        long counterBeforeDelete = employeeRepository.count();
        Employee employee = employeeRepository.findById(employeePersonnelNumber).orElseThrow();
        employeeRepository.deleteById(employee.getPersonnelNumber());
        employeeRepository.flush();
        long counterAfterDelete = employeeRepository.count();
        assertEquals(1, counterBeforeDelete - counterAfterDelete);
    }

    private static void showEmployeeList(Iterable<Employee> employeeList) {
        employeeList.forEach(EmployeeRepositoryTest::showEmployee);
    }

    private static void showEmployee(Employee employee) {
        System.out.println(employee);
    }


}

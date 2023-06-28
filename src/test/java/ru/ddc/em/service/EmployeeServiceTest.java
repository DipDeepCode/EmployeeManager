package ru.ddc.em.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.ddc.em.persistence.dao.EmployeeRepository;
import ru.ddc.em.persistence.model.Department;
import ru.ddc.em.persistence.model.Employee;
import ru.ddc.em.testutils.VacancyTestBuilder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.ddc.em.testutils.EmployeeTestBuilder.*;
import static ru.ddc.em.testutils.VacancyTestBuilder.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    public void whenFindAllByPageNoPageSizeSortBy_thenReturnExpectedPage() {
        Page<Employee> expectedPage = getExpectedPage();

        when(employeeRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        Page<Employee> actualPage = employeeService.findAll(1, 2, Sort.by("sort"));

        assertTrue(new ReflectionEquals(expectedPage).matches(actualPage));
    }

    private Page<Employee> getExpectedPage() {
        Employee employee1 = aEmployee()
                .withPersonnelNumber(1L)
                .withFirstname("firstname1")
                .withLastname("lastname1")
                .build();
        Employee employee2 = aEmployee()
                .withPersonnelNumber(2L)
                .withFirstname("firstname2")
                .withLastname("lastname2")
                .build();
        return new PageImpl<>(List.of(employee1, employee2));
    }

    @Test
    public void whenFindAllPageByNoPageSize_thenReturnExpectedPage() {
        Page<Employee> expectedPage = getExpectedPage();

        when(employeeRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        Page<Employee> actualPage = employeeService.findAll(1, 2, Sort.by("sort"));

        assertTrue(new ReflectionEquals(expectedPage).matches(actualPage));
    }

    @Test
    public void whenFindAllByPageable_thenReturnExpectedPage() {
        Page<Employee> expectedPage = getExpectedPage();
        Pageable pageable = PageRequest.of(0, 2, Department.defaultSort);
        when(employeeRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        Page<Employee> actualPage = employeeService.findAll(pageable);

        assertTrue(new ReflectionEquals(expectedPage).matches(actualPage));
    }

    @Test
    public void whenSaveEmployee_thenReturnSameEmployee() {
        String firstname = "firstname";
        String lastname = "lastname";

        Employee employeeToSave = aEmployee()
                .withFirstname(firstname)
                .withLastname(lastname)
                .build();

        Employee expectedEmployee = aEmployee()
                .withPersonnelNumber(100L)
                .withFirstname(firstname)
                .withLastname(lastname)
                .build();

        when(employeeRepository.save(any(Employee.class))).thenReturn(expectedEmployee);

        Employee actualEmployee = employeeService.save(employeeToSave);

        assertTrue(new ReflectionEquals(expectedEmployee, "personnelNumber").matches(actualEmployee));
        assertNotNull(actualEmployee.getPersonnelNumber());
    }

    @Test
    public void whenFindExistingEmployeeById_thenReturnExpectedEmployee() {
        Long employeePersonnelNumber = 1L;
        Employee expectedEmployee = aEmployee()
                .withPersonnelNumber(employeePersonnelNumber)
                .withFirstname("firstname")
                .withLastname("lastname")
                .build();

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(expectedEmployee));

        Employee actualEmployee = employeeService.findById(employeePersonnelNumber);

        assertTrue(new ReflectionEquals(expectedEmployee).matches(actualEmployee));
    }

    @Test
    public void whenFindMissingEmployeeById_thenThrowException() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> employeeService.findById(anyLong()));
    }
}







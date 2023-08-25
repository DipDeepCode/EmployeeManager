package ru.ddc.em.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.ddc.em.exceptions.DeleteEntityException;
import ru.ddc.em.persistence.dao.DepartmentRepository;
import ru.ddc.em.persistence.entity.Department;
import ru.ddc.em.persistence.entity.Vacancy;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static ru.ddc.em.testutils.DepartmentTestBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    public void whenFindAllByPageNoPageSizeSortBy_thenReturnExpectedPage() {
        Page<Department> expectedPage = getExpectedPage();

        when(departmentRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        Page<Department> actualPage = departmentService.findAll(1,2, Sort.by("sort"));

        assertTrue(new ReflectionEquals(expectedPage).matches(actualPage));
    }

    private Page<Department> getExpectedPage() {
        Department expectedDepartment1 = aDepartment()
                .withId(1L)
                .withName("test_department_name_1")
                .withNumber("test_department_number_1")
                .build();
        Department expectedDepartment2 = aDepartment()
                .withId(2L)
                .withName("test_department_name_2")
                .withNumber("test_department_number_2")
                .build();
        return new PageImpl<>(List.of(expectedDepartment1, expectedDepartment2));
    }

    @Test
    public void whenFindAllByPageNoPageSize_thenReturnExpectedPage() {
        Page<Department> expectedPage = getExpectedPage();

        when(departmentRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        Page<Department> actualPage = departmentService.findAll(1, 1);

        assertTrue(new ReflectionEquals(expectedPage).matches(actualPage));
    }

    @Test
    public void whenFindAllByPageable_thenReturnExpectedPage() {
        Page<Department> expectedPage = getExpectedPage();
        Pageable pageable = PageRequest.of(0, 2, Department.defaultSort);
        when(departmentRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        Page<Department> actualPage = departmentService.findAll(pageable);

        assertTrue(new ReflectionEquals(expectedPage).matches(actualPage));
    }

    @Test
    public void whenSaveDepartment_thenReturnSameDepartment() {
        String departmentName = "department_to_save_name";
        String departmentNumber = "department_to_save_number";

        Department departmentToSave = aDepartment()
                .withName(departmentName)
                .withNumber(departmentNumber)
                .build();

        Department expectedDepartment = aDepartment()
                .withId(100L)
                .withName(departmentName)
                .withNumber(departmentNumber)
                .build();

        when(departmentRepository.save(any(Department.class))).thenReturn(expectedDepartment);

        Department actualDepartment = departmentService.save(departmentToSave);

        assertTrue(new ReflectionEquals(expectedDepartment, "id").matches(actualDepartment));
        assertNotNull(actualDepartment.getId());
    }

    @Test
    public void whenFindExistingDepartmentById_thenReturnExpectedDepartment() {
        Long departmentId = 1L;
        Department expectedDepartment = aDepartment()
                .withId(departmentId)
                .withName("test_department_name")
                .withNumber("test_department_number")
                .build();

        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(expectedDepartment));

        Department actualDepartment = departmentService.findById(departmentId);

        assertTrue(new ReflectionEquals(expectedDepartment).matches(actualDepartment));
    }

    @Test
    public void whenFindMissingDepartmentById_thenThrowException() {
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> departmentService.findById(anyLong()));
    }

    @Test
    public void whenDeleteDepartmentWithoutVacancies_thenWithoutThrowingException() {
        Department department = aDepartment()
                .withId(1L)
                .withVacancyList(new ArrayList<>())
                .build();
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));
        assertDoesNotThrow(() -> departmentService.deleteById(anyLong()));
    }

    @Test
    public void whenDeleteDepartmentWithVacancies_thenThrowException() {
        Department department = aDepartment()
                .withId(1L)
                .withVacancyList(List.of(new Vacancy()))
                .build();
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));
        assertThrows(DeleteEntityException.class, () -> departmentService.deleteById(anyLong()));
    }
}
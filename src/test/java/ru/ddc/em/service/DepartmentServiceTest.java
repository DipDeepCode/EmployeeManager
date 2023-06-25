package ru.ddc.em.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.ddc.em.persistence.dao.DepartmentRepository;
import ru.ddc.em.persistence.model.Department;
import ru.ddc.em.persistence.model.Vacancy;
import ru.ddc.em.utils.custommapper.CustomMapper;
import ru.ddc.em.web.error.DeleteEntityError;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static ru.ddc.em.testutils.DepartmentTestBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ModelMapper.class, CustomMapper.class})
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository mockedRepository;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    public void whenFindAllByPageNoPageSizeSortBy_thenReturnExpectedPage() {
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
        Page<Department> expectedPage = new PageImpl<>(List.of(expectedDepartment1, expectedDepartment2));

        when(mockedRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        Page<Department> actualPage = departmentService.findAll(1, 1, "sort");

        assertTrue(new ReflectionEquals(expectedPage).matches(actualPage));
    }

    @Test
    public void whenFindAllByPageNoPageSize_thenReturnExpectedPage() {
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
        Page<Department> expectedPage = new PageImpl<>(List.of(expectedDepartment1, expectedDepartment2));

        when(mockedRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        Page<Department> actualPage = departmentService.findAll(1, 1);

        assertTrue(new ReflectionEquals(expectedPage).matches(actualPage));
    }

    @Test
    public void whenFindAllByPageable_thenReturnExpectedPage() {
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
        Page<Department> expectedPage = new PageImpl<>(List.of(expectedDepartment1, expectedDepartment2));
        Pageable pageable = PageRequest.of(0, 2, Department.defaultSort);
        when(mockedRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

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

        when(mockedRepository.save(any(Department.class))).thenReturn(expectedDepartment);

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

        when(mockedRepository.findById(anyLong())).thenReturn(Optional.of(expectedDepartment));

        Department actualDepartment = departmentService.findById(departmentId);

        assertTrue(new ReflectionEquals(expectedDepartment).matches(actualDepartment));
    }

    @Test
    public void whenFindMissingDepartmentById_thenThrowException() {
        when(mockedRepository.findById(anyLong())).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> departmentService.findById(anyLong()));
    }

    @Test
    public void whenDeleteDepartmentWithoutVacancies_thenWithoutThrowingException() {
        Long id = 1L;
        Department department = aDepartment()
                .withId(id)
                .withVacancyList(new ArrayList<>())
                .build();
        when(mockedRepository.findById(anyLong())).thenReturn(Optional.of(department));
        assertDoesNotThrow(() -> departmentService.deleteById(anyLong()));
    }

    @Test
    public void whenDeleteDepartmentWithVacancies_thenThrowException() {
        Long id = 1L;
        Department department = aDepartment()
                .withId(id)
                .withVacancyList(List.of(new Vacancy()))
                .build();
        when(mockedRepository.findById(anyLong())).thenReturn(Optional.of(department));
        assertThrows(DeleteEntityError.class, () -> departmentService.deleteById(anyLong()));
    }
}
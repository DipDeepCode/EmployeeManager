package ru.ddc.em.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.ddc.em.persistence.dao.VacancyRepository;
import ru.ddc.em.persistence.model.Vacancy;
import ru.ddc.em.testutils.EmployeeTestBuilder;
import ru.ddc.em.web.error.DeleteEntityError;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static ru.ddc.em.testutils.VacancyTestBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VacancyServiceTest {

    @Mock
    private VacancyRepository vacancyRepository;

    @InjectMocks
    private VacancyService vacancyService;

    @Test
    public void whenFindAllByPageNoPageSizeSortBy_thenReturnExpectedPage() {
        Page<Vacancy> expectedPage = getExpectedPage();

        when(vacancyRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        Page<Vacancy> actualPage = vacancyService.findAll(1, 2, Sort.by("position"));

        assertTrue(new ReflectionEquals(expectedPage).matches(actualPage));
    }

    private Page<Vacancy> getExpectedPage() {
        Vacancy vacancy1 = aVacancy()
                .withPosition("position_1")
                .withSalary(100f)
                .build();
        Vacancy vacancy2 = aVacancy()
                .withPosition("position_2")
                .withSalary(200f)
                .build();
        return new PageImpl<>(List.of(vacancy1, vacancy2));
    }

    @Test
    public void whenFindAllByPageNoPageSize_thenReturnExpectedPage() {
        Page<Vacancy> expectedPage = getExpectedPage();

        when(vacancyRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        Page<Vacancy> actualPage = vacancyService.findAll(1, 2);

        assertTrue(new ReflectionEquals(expectedPage).matches(actualPage));
    }

    @Test
    public void findAllByPageable_thenReturnExpectedPage_thenReturnExpectedPage() {
        Page<Vacancy> expectedPage = getExpectedPage();

        when(vacancyRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        Pageable pageable = PageRequest.of(0, 2, Vacancy.defaultSort);
        Page<Vacancy> actualPage = vacancyService.findAll(pageable);

        assertTrue(new ReflectionEquals(expectedPage).matches(actualPage));
    }

    @Test
    public void whenSaveVacancy_thenReturnSameDepartment() {
            String position = "position";
            float salary = 100f;

            Vacancy vacancyToSave = aVacancy()
                    .withPosition(position)
                    .withSalary(salary)
                    .build();
            Vacancy expectedVacancy = aVacancy()
                    .withId(100L)
                    .withPosition(position)
                    .withSalary(salary)
                    .build();

            when(vacancyRepository.save(any(Vacancy.class))).thenReturn(expectedVacancy);

            Vacancy actualVacancy = vacancyService.save(vacancyToSave);

            assertTrue(new ReflectionEquals(expectedVacancy).matches(actualVacancy));
    }

    @Test
    public void whenFindExistingVacancyById_thenReturnExpectedVacancy() {
        Long vacancyId = 1L;
        Vacancy expectedVacancy = aVacancy()
                .withId(vacancyId)
                .withPosition("position")
                .withSalary(100f)
                .build();

        when(vacancyRepository.findById(anyLong())).thenReturn(Optional.of(expectedVacancy));

        Vacancy actualVacancy = vacancyService.findById(vacancyId);

        assertTrue(new ReflectionEquals(expectedVacancy).matches(actualVacancy));
    }

    @Test
    public void whenFindMissingVacancyById_thenThrowException() {
        when(vacancyRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> vacancyService.findById(anyLong()));
    }

    @Test
    public void whenDeleteVacancyWithoutEmployee_thenWithoutThrowingException() {
        Vacancy vacancy = aVacancy()
                .withId(1L)
                .withPosition("position")
                .withSalary(100f)
                .withEmployee(null)
                .build();
        when(vacancyRepository.findById(anyLong())).thenReturn(Optional.of(vacancy));
        assertDoesNotThrow(() -> vacancyService.deleteById(anyLong()));
    }

    @Test
    public void whenDeleteVacancyWithEmployee_thenThrowException() {
        Vacancy vacancy = aVacancy()
                .withId(1L)
                .withPosition("position")
                .withSalary(100f)
                .withEmployee(EmployeeTestBuilder.aEmployee().build())
                .build();
        when(vacancyRepository.findById(anyLong())).thenReturn(Optional.of(vacancy));
        assertThrows(DeleteEntityError.class, () -> vacancyService.deleteById(anyLong()));
    }
}
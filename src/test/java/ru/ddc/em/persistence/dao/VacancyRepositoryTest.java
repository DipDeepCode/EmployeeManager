package ru.ddc.em.persistence.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.ddc.em.persistence.entity.Department;
import ru.ddc.em.persistence.entity.Vacancy;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.ddc.em.testutils.VacancyTestBuilder.*;

@DataJpaTest
public class VacancyRepositoryTest {

    @Autowired
    private VacancyRepository vacancyRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    public void whenFindAllVacancies_thenListSizeEqualsSix() {
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        showVacancyList(vacancyList);
        assertEquals(6, vacancyList.size());
    }

    @Test
    public void whenFindAllVacanciesPage_thenListSizeEqualsSomeValue() {
        Pageable pageable = PageRequest.of(0, 2, Vacancy.defaultSort);
        Page<Vacancy> vacancyPage = vacancyRepository.findAll(pageable);
        showVacancyList(vacancyPage);
        assertEquals(2, vacancyPage.getSize());
    }

    @Test
    public void whenFindAllVacanciesByDepartmentNumber_thenListSizeEqualsSomeValue() {
        Long departmentId = 1L;
        List<Vacancy> vacancyList = vacancyRepository.findByDepartmentId(departmentId);
        showVacancyList(vacancyList);
        assertEquals(3, vacancyList.size());
    }

    @Test
    public void whenFindAllVacanciesByDepartmentNumberPage_thenListSizeEqualsSomeValue() {
        Long departmentId = 1L;
        Pageable pageable = PageRequest.of(0, 2, Vacancy.defaultSort);
        Page<Vacancy> vacancyPage = vacancyRepository.findByDepartmentId(departmentId, pageable);
        showVacancyList(vacancyPage);
        assertEquals(2, vacancyPage.getSize());
    }

    @Test
    public void whenFindVacancyById_thenVacancyIdEqualsToSpecified() {
        Long vacancyId = 1L;
        Vacancy vacancy = vacancyRepository.findById(vacancyId).orElseThrow();
        showVacancy(vacancy);
        assertEquals(vacancyId, vacancy.getId());
    }

    @Test
    public void whenFindMissingVacancyById_thenReturnEmptyOptional() {
        Optional<Vacancy> optionalVacancy = vacancyRepository.findById(100L);
        assertTrue(optionalVacancy.isEmpty());
    }

    @Test
    public void whenSaveVacancyWithoutDepartment_thenThrowException() {
        Vacancy vacancy = aVacancy().build();
        showVacancy(vacancy);
        assertThrows(DataIntegrityViolationException.class, () -> vacancyRepository.saveAndFlush(vacancy));
    }

    @Test
    public void whenSaveVacancyWithoutEmployee_thenWithoutThrowingException() {
        Department department = departmentRepository.findById(1L).orElseThrow();
        Vacancy vacancy = aVacancy()
                .withId(1L)
                .withPosition("position")
                .withSalary(100f)
                .withDepartment(department)
                .withEmployee(null)
                .build();
        showVacancy(vacancy);
        assertDoesNotThrow(() -> vacancyRepository.saveAndFlush(vacancy));
    }

    @Test
    public void whenSaveVacancyWithDepartment_thenVacancyCounterLagerByOne() {
        long counterBeforeSave = vacancyRepository.count();
        Vacancy vacancy = aVacancy().build();
        Department department = departmentRepository.findById(1L).orElseThrow();
        department.addVacancy(vacancy);
        showVacancy(vacancy);
        vacancyRepository.saveAndFlush(vacancy);
        long counterAfterSave = vacancyRepository.count();
        assertEquals(1, counterAfterSave - counterBeforeSave);
    }

    @Test
    public void whenDeleteVacancyById_thenVacancyCounterLessByOne() {
        long counterBeforeDelete = vacancyRepository.count();
        Long vacancyId = 1L;
        Vacancy vacancy = vacancyRepository.findById(vacancyId).orElseThrow();
        vacancyRepository.deleteById(vacancy.getId());
        vacancyRepository.flush();
        long counterAfterDelete = vacancyRepository.count();
        assertEquals(1, counterBeforeDelete - counterAfterDelete);
    }

    @Test
    public void whenFindAllEmployeesWithoutVacancy_thenListSizeEqualsTwo() {
        List<Vacancy> vacancyList = vacancyRepository.findByEmployeeNull();
        showVacancyList(vacancyList);
        assertEquals(2, vacancyList.size());
    }

    @Test
    public void when_findByExistsEmployeePersonnelNumber_then_resultConsistEmployeeAndDepartment() {
        Vacancy vacancy = vacancyRepository.findByEmployeePersonnelNumber(5L).orElseThrow();
        showVacancy(vacancy);
        assertNotNull(vacancy);
        assertNotNull(vacancy.getEmployee());
        assertNotNull(vacancy.getDepartment());
    }

    @Test
    public void when_findByNotExistsEmployeePersonnelNumber_then_throwException() {

    }

    private static void showVacancyList(Iterable<Vacancy> vacancyList) {
        vacancyList.forEach(VacancyRepositoryTest::showVacancy);
    }

    private static void showVacancy(Vacancy vacancy) {
        System.out.println(vacancy);
        System.out.println("    employee = " + vacancy.getEmployee());
        System.out.println("    department = " + vacancy.getDepartment());
    }
}

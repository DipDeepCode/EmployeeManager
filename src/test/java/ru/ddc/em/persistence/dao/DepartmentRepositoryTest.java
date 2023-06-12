package ru.ddc.em.persistence.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.ddc.em.persistence.model.Department;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.ddc.em.persistence.testutils.DepartmentTestBuilder.*;

@DataJpaTest
public class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    public void whenFindAllDepartments_thenListSizeEqualsFour() {
        List<Department> departmentList = departmentRepository.findAll();
        showDepartmentList(departmentList);
        assertEquals(4, departmentList.size());
    }

    @Test
    public void whenFindAllDepartmentsPage_thenListSizeEqualsSomeValue() {
        Pageable pageable = PageRequest.of(0, 2, Department.defaultSort);
        Page<Department> departmentPage = departmentRepository.findAll(pageable);
        showDepartmentList(departmentPage);
        assertEquals(2, departmentPage.getSize());
    }

    @Test
    public void whenFindDepartmentById_thenDepartmentIdEqualsToSpecified() {
        Long departmentId = 1L;
        Department department = departmentRepository.findById(departmentId).orElseThrow();
        showDepartment(department);
        assertEquals(departmentId, department.getId());
    }

    @Test
    public void whenSaveDepartment_thenDepartmentCounterLagerByOne() {
        long counterBeforeSave = departmentRepository.count();
        Department department = aDepartment().build();
        showDepartment(department);
        departmentRepository.save(department);
        long counterAfterSave = departmentRepository.count();
        assertEquals(1, counterAfterSave - counterBeforeSave);
    }

    @Test
    public void whenUpdateDepartment_thenNewValueWillBeSaved() {
        Long departmentId = 1L;
        String newName = "new_name";
        Department department = departmentRepository.findById(departmentId).orElseThrow();
        showDepartment(department);
        department.setName(newName);
        department = departmentRepository.saveAndFlush(department);
        showDepartment(department);
        assertEquals(newName, department.getName());
    }

    @Test
    public void whenDeleteDepartmentByIdWithVacancies_thenThrownException() {
        Long departmentId = 1L;
        Department department = departmentRepository.findById(departmentId).orElseThrow();
        departmentRepository.deleteById(department.getId());
        assertThrows(DataIntegrityViolationException.class, () -> departmentRepository.flush());
    }

    @Test
    public void whenDeleteDepartmentByIdWithoutVacancies_thenDepartmentCounterLessByOne() {
        Long departmentId = 4L;
        Department department = departmentRepository.findById(departmentId).orElseThrow();
        long counterBeforeDelete = departmentRepository.count();
        departmentRepository.deleteById(department.getId());
        departmentRepository.flush();
        long counterAfterDelete = departmentRepository.count();
        assertEquals(1, counterBeforeDelete - counterAfterDelete);
    }

    private static void showDepartmentList(Iterable<Department> departmentList) {
        departmentList.forEach(DepartmentRepositoryTest::showDepartment);
    }

    private static void showDepartment(Department department) {
        System.out.println(department);
        department.getVacancies().forEach(vacancy -> System.out.println("   -" + vacancy));
    }
}

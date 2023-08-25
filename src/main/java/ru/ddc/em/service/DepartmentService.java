package ru.ddc.em.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.ddc.em.exceptions.DeleteEntityException;
import ru.ddc.em.persistence.entity.Department;
import ru.ddc.em.persistence.dao.DepartmentRepository;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> findAll(Sort sort) {
        return departmentRepository.findAll(sort);
    }

    public Page<Department> findAll(Integer pageNo, Integer pageSize, Sort sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        return findAll(pageable);
    }
    
    public Page<Department> findAll(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Department.defaultSort);
        return findAll(pageable);
    }

    public Page<Department> findAll(Pageable pageable) {
        return departmentRepository.findAll(pageable);
    }

    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    public Department findById(Long id) {
        return departmentRepository.findById(id).orElseThrow();
    }

    public void deleteById(Long id) {
        Department department = departmentRepository.findById(id).orElseThrow();
        if (department.getVacancies().isEmpty()) {
            departmentRepository.deleteById(id);
        } else {
            throw new DeleteEntityException("Нельзя удалить отдел, так как он имеет вакансии");
        }
    }
}

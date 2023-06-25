package ru.ddc.em.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.ddc.em.persistence.model.Department;
import ru.ddc.em.persistence.dao.DepartmentRepository;
import org.springframework.data.domain.Sort;
import ru.ddc.em.web.error.DeleteEntityError;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
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
        return departmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный номер отдела: " + id));
    }

    public void deleteById(Long id) throws DeleteEntityError {
        Department department = departmentRepository.findById(id).orElseThrow();
        if (department.getVacancies().size() == 0) {
            departmentRepository.deleteById(id);
        } else {
            throw new DeleteEntityError("Нельзя удалить отдел, так как он имеет вакансии");
        }
    }
}

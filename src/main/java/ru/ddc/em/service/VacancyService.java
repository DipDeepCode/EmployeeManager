package ru.ddc.em.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.ddc.em.exceptions.DeleteEntityException;
import ru.ddc.em.persistence.entity.Vacancy;
import ru.ddc.em.persistence.dao.VacancyRepository;

import java.util.List;
import java.util.Optional;


@Service
public class VacancyService {
    private final VacancyRepository vacancyRepository;

    @Autowired
    public VacancyService(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    public Page<Vacancy> findAll(Integer pageNo, Integer pageSize, Sort sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        return findAll(pageable);
    }

    public Page<Vacancy> findAll(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Vacancy.defaultSort);
        return findAll(pageable);
    }

    public Page<Vacancy> findAll(Pageable pageable) {
        return vacancyRepository.findAll(pageable);
    }

    public Page<Vacancy> findByDepartmentId(Long departmentId, Integer pageNo, Integer pageSize, Sort sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        return findByDepartmentId(departmentId, pageable);
    }

    public Page<Vacancy> findByDepartmentId(Long departmentId, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Vacancy.defaultSort);
        return findByDepartmentId(departmentId, pageable);
    }

    public Page<Vacancy> findByDepartmentId(Long departmentId, Pageable pageable) {
        return vacancyRepository.findByDepartmentId(departmentId, pageable);
    }

    public Vacancy save(Vacancy vacancy) {
        return vacancyRepository.save(vacancy);
    }

    public Vacancy findById(Long id) {
        return vacancyRepository.findById(id).orElseThrow();
    }

    public void deleteById(Long id) {
        Vacancy vacancy = vacancyRepository.findById(id).orElseThrow();//TODO заполнить orElseThrow
        if (vacancy.getEmployee() == null) {
            vacancyRepository.deleteById(id);
        } else {
            throw new DeleteEntityException("Нельзя удалить вакансию, так как к ней привязан работник");
        }
    }

    public List<Vacancy> findByEmployeeNull() {
        return vacancyRepository.findByEmployeeNull();
    }

    public Optional<Vacancy> findByEmployeePersonnelNumber(Long employeePersonnelNumber) {
        return vacancyRepository.findByEmployeePersonnelNumber(employeePersonnelNumber);
    }
}

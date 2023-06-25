package ru.ddc.em.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.ddc.em.persistence.model.Vacancy;
import ru.ddc.em.persistence.dao.VacancyRepository;
import ru.ddc.em.web.error.DeleteEntityError;


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
        return vacancyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный id вакансии: " + id));
    }

    public void deleteById(Long id) throws DeleteEntityError {
        Vacancy vacancy = vacancyRepository.findById(id).orElseThrow();//TODO заполнить orElseThrow
        if (vacancy.getEmployee() == null) {
            vacancyRepository.deleteById(id);
        }
        else {
            throw new DeleteEntityError("Нельзя удалить вакансию, так как к ней привязан работник");
        }
    }
}

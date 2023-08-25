package ru.ddc.em.web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ddc.em.persistence.entity.Department;
import ru.ddc.em.persistence.entity.Vacancy;
import ru.ddc.em.service.DepartmentService;
import ru.ddc.em.utils.custommapper.CustomMapper;
import ru.ddc.em.web.dto.DepartmentDto;
import ru.ddc.em.web.dto.VacancyDto;

import java.util.List;

@Controller
@RequestMapping("departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    private final CustomMapper mapper;

    @Autowired
    public DepartmentController(DepartmentService departmentService,
                                CustomMapper mapper) {
        this.departmentService = departmentService;
        this.mapper = mapper;
    }

    @GetMapping
    public String index(Model model) {
        Page<Department> departmentPage = departmentService.findAll(0, 10);
        List<DepartmentDto> departmentDtoList = mapper.mapIterable(departmentPage, DepartmentDto.class);
        model.addAttribute("departments", departmentDtoList);
        return "departments/departments_index";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute("department") DepartmentDto departmentDto) {
        return "departments/departments_create";
    }

    @PostMapping
    public String store(@Valid DepartmentDto departmentDto, BindingResult result) {
        if (result.hasErrors()) {
            return "departments/department_create";
        } else {
            departmentService.save(mapper.map(departmentDto, Department.class));
            return "redirect:/departments";
        }
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        DepartmentDto departmentDto = mapper.map(departmentService.findById(id), DepartmentDto.class);
        model.addAttribute("department", departmentDto);
        return "departments/departments_edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") Long id,
                         @Valid DepartmentDto departmentDto,
                         BindingResult result) {
        if (result.hasErrors()) {
            departmentDto.setId(id);
            return "departments/departments_edit";
        } else {
            departmentService.save(mapper.map(departmentDto, Department.class));
            return "redirect:/departments";
        }
    }

    @DeleteMapping("/{id}")
    public String destroy(@PathVariable("id") Long id) {
        departmentService.deleteById(id);
        return "redirect:/departments";
    }

    @GetMapping("/{id}/vacancies")
    public String showVacancies(@PathVariable("id") Long id,
                                Model model) {
        Department department = departmentService.findById(id);
        DepartmentDto departmentDto = mapper.map(department, DepartmentDto.class);
        model.addAttribute("department", departmentDto);

        List<Vacancy> vacancyList = department.getVacancies();
        List<VacancyDto> vacancyDtoList = mapper.mapIterable(vacancyList, VacancyDto.class);
        model.addAttribute("vacancies", vacancyDtoList);

        return "departments/vacancies_show";
    }
}

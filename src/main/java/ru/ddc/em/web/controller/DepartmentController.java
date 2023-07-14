package ru.ddc.em.web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ddc.em.persistence.model.Department;
import ru.ddc.em.service.DepartmentService;
import ru.ddc.em.utils.custommapper.CustomMapper;
import ru.ddc.em.web.dto.DepartmentDto;
import ru.ddc.em.web.error.DeleteEntityError;

import java.util.List;

@Controller
@RequestMapping("departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    private final CustomMapper mapper;

    @Autowired
    public DepartmentController(DepartmentService departmentService, CustomMapper mapper) {
        this.departmentService = departmentService;
        this.mapper = mapper;
    }

    @GetMapping
    public String showDepartmentList(Model model) {
        Page<Department> departmentPage = departmentService.findAll(0, 10);
        List<DepartmentDto> departmentDtoList = mapper.mapIterable(departmentPage, DepartmentDto.class);
        model.addAttribute("departments", departmentDtoList);
        return "departments";
    }

    @GetMapping("/addForm")
    public String showAddForm(DepartmentDto departmentDto) {
        return "add-department";
    }

    @PostMapping("/add")
    public String addDepartment(@Valid DepartmentDto departmentDto, BindingResult result) {
        if (result.hasErrors()) {
            return "add-department";
        } else {
            departmentService.save(mapper.map(departmentDto, Department.class));
            return "redirect:/departments";
        }
    }

    @GetMapping("/updateForm/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        DepartmentDto departmentDto = mapper.map(departmentService.findById(id), DepartmentDto.class);
        model.addAttribute("department", departmentDto);
        return "update-department";
    }

    @PostMapping("/update/{id}")
    public String updateDepartment(@PathVariable("id") Long id, @Valid DepartmentDto departmentDto, BindingResult result) {
        if (result.hasErrors()) {
            departmentDto.setId(id);
            return "update-department";
        } else {
            departmentService.save(mapper.map(departmentDto, Department.class));
            return "redirect:/departments";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable("id") Long id) throws DeleteEntityError {
        departmentService.deleteById(id);
        return "redirect:/departments";
    }
}

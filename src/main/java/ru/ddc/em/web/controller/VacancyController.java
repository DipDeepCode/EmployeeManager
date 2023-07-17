package ru.ddc.em.web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ddc.em.persistence.entity.Department;
import ru.ddc.em.persistence.entity.Employee;
import ru.ddc.em.persistence.entity.Vacancy;
import ru.ddc.em.service.DepartmentService;
import ru.ddc.em.service.EmployeeService;
import ru.ddc.em.service.VacancyService;
import ru.ddc.em.utils.custommapper.CustomMapper;
import ru.ddc.em.web.dto.DepartmentDto;
import ru.ddc.em.web.dto.EmployeeDto;
import ru.ddc.em.web.dto.VacancyDto;
import ru.ddc.em.web.error.DeleteEntityError;

import java.util.List;

@Controller
@RequestMapping("vacancies")
public class VacancyController {
    private final VacancyService vacancyService;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final CustomMapper mapper;


    @Autowired
    public VacancyController(VacancyService vacancyService,
                             EmployeeService employeeService,
                             DepartmentService departmentService,
                             CustomMapper mapper) {
        this.vacancyService = vacancyService;
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.mapper = mapper;
    }

    @GetMapping
    public String showVacancyList(Model model) {
        Page<Vacancy> vacancyPage = vacancyService.findAll(0, 10);
        List<VacancyDto> vacancyDtoList = mapper.mapIterable(vacancyPage, VacancyDto.class);
        model.addAttribute("vacancies", vacancyDtoList);
        model.addAttribute("isVacanciesOnlyOfTheSameDepartment", false);
        return "vacancy/vacancies";
    }

    @GetMapping("/{departmentId}")
    public String showVacancyList(@PathVariable("departmentId") Long departmentId,
                                  Model model) {
        Department department = departmentService.findById(departmentId);
        DepartmentDto departmentDto = mapper.map(department, DepartmentDto.class);
        model.addAttribute("department", departmentDto);

        Page<Vacancy> vacancyPage = vacancyService.findByDepartmentId(departmentId, 0, 10);
        List<VacancyDto> vacancyDtoList = mapper.mapIterable(vacancyPage, VacancyDto.class);
        model.addAttribute("vacancies", vacancyDtoList);

        model.addAttribute("isVacanciesOnlyOfTheSameDepartment", true);
        return "vacancy/vacancies";
    }

    @GetMapping("/addForm/{departmentId}")
    public String showAddForm(@PathVariable("departmentId") Long departmentId,
                              VacancyDto vacancyDto,
                              Model model) {
        addDepartmentAndListOfEmployeesToModel(model, departmentId);
        return "vacancy/add-vacancy";
    }

    @PostMapping("/add")
    public String addVacancy(@Valid VacancyDto vacancyDto,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            addDepartmentAndListOfEmployeesToModel(model, vacancyDto.getDepartmentDto().getId());
            return "vacancy/add-vacancy";
        } else {
            if (vacancyDto.getEmployeeDto() != null && vacancyDto.getEmployeeDto().getPersonnelNumber() == -1) {
                vacancyDto.setEmployeeDto(null);
            }
            Vacancy vacancy = mapper.map(vacancyDto, Vacancy.class);
            vacancyService.save(vacancy);
            return "redirect:/vacancies/" + vacancyDto.getDepartmentDto().getId();
        }
    }

    private void addDepartmentAndListOfEmployeesToModel(Model model,
                                                        Long departmentId) {
        Department department = departmentService.findById(departmentId);
        DepartmentDto departmentDto = mapper.map(department, DepartmentDto.class);
        model.addAttribute("department", departmentDto);
        List<Employee> employeePage = employeeService.findAllNotAssignedToAnyDepartment();
        List<EmployeeDto> employeeDtoList = mapper.mapIterable(employeePage, EmployeeDto.class);
        model.addAttribute("employees", employeeDtoList);
    }

    @GetMapping("/updateForm/{id}")
    public String showUpdateForm(@PathVariable("id") Long id,
                                 Model model) {
        Vacancy vacancy = vacancyService.findById(id);
        VacancyDto vacancyDto = mapper.map(vacancy, VacancyDto.class);
        model.addAttribute("vacancy", vacancyDto);
        List<Employee> employeeList = employeeService.findAllNotAssignedToAnyDepartment();
        List<EmployeeDto> employeeDtoList = mapper.mapIterable(employeeList, EmployeeDto.class);
        model.addAttribute("employees", employeeDtoList);
        return "vacancy/update-vacancy";
    }

    @PostMapping("/update/{id}")
    public String updateVacancy(@PathVariable("id") Long id,
                                @Valid VacancyDto vacancyDto,
                                BindingResult result) {
        if (result.hasErrors()) {
            vacancyDto.setId(id);
            return "vacancy/update-vacancy";
        } else {
            if (vacancyDto.getEmployeeDto() != null && vacancyDto.getEmployeeDto().getPersonnelNumber() == -1) {
                vacancyDto.setEmployeeDto(null);
            }
            Vacancy vacancy = mapper.map(vacancyDto, Vacancy.class);
            vacancyService.save(vacancy);
            return "redirect:/vacancies/" + vacancyDto.getDepartmentDto().getId();
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteVacancy(@PathVariable("id") Long id) throws DeleteEntityError {
        Vacancy vacancy = vacancyService.findById(id);
        Long departmentId = vacancy.getDepartment().getId();
        vacancyService.deleteById(id);
        return "redirect:/vacancies/" + departmentId;
    }
}

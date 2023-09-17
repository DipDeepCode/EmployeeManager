package ru.ddc.em.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ddc.em.persistence.entity.Department;
import ru.ddc.em.persistence.entity.Vacancy;
import ru.ddc.em.service.DepartmentService;
import ru.ddc.em.service.EmployeeService;
import ru.ddc.em.service.VacancyService;
import ru.ddc.em.utils.custommapper.CustomMapper;
import ru.ddc.em.web.dto.DepartmentDto;
import ru.ddc.em.web.dto.VacancyDto;

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
    public String showAllVacancies(Model model) {
        Page<Vacancy> vacancyPage = vacancyService.findAll(0, 10);
        List<VacancyDto> vacancyDtoList = mapper.mapIterable(vacancyPage, VacancyDto.class);
        model.addAttribute("vacancies", vacancyDtoList);
        return "vacancies/vacancies_all";
    }

    @GetMapping("/byDepartment/{dID}")
    public String showAllVacanciesByDepartment(@PathVariable("dID") Long dID,
                                               Model model) {
        Department department = departmentService.findById(dID);
        DepartmentDto departmentDto = mapper.map(department, DepartmentDto.class);
        model.addAttribute("department", departmentDto);

        Page<Vacancy> vacancyPage = vacancyService.findByDepartmentId(dID, 0, 10);
        List<VacancyDto> vacancyDtoList = mapper.mapIterable(vacancyPage, VacancyDto.class);
        model.addAttribute("vacancies", vacancyDtoList);
        return "vacancies/vacancies_byDepartment";
    }

    @GetMapping("{id}")
    public String showOneVacancy(@PathVariable("id") Long id,
                                 Model model) {
        Vacancy vacancy = vacancyService.findById(id);
        VacancyDto vacancyDto = mapper.map(vacancy, VacancyDto.class);
        model.addAttribute("vacancy", vacancyDto);
        return "vacancies/vacancies_one";
    }

//    @GetMapping("/{departmentId}")
//    public String allByDepartmentId(@PathVariable("departmentId") Long departmentId,
//                                    Model model) {
//        Department department = departmentService.findById(departmentId);
//        DepartmentDto departmentDto = mapper.map(department, DepartmentDto.class);
//        model.addAttribute("department", departmentDto);
//
//        Page<Vacancy> vacancyPage = vacancyService.findByDepartmentId(departmentId, 0, 10);
//        List<VacancyDto> vacancyDtoList = mapper.mapIterable(vacancyPage, VacancyDto.class);
//        model.addAttribute("vacancies", vacancyDtoList);
//
//        model.addAttribute("isVacanciesOnlyOfTheSameDepartment", true);
//        return "vacancy/all";
//    }

    @GetMapping("/new")
    public String showNewVacancyForm(@ModelAttribute("vacancy") VacancyDto vacancyDto,
                                     Model model) {
//        addDepartmentAndListOfEmployeesToModel(model, departmentId);
//        List<Employee> employeePage = employeeService.findAllNotAssignedToAnyVacancy();
//        List<EmployeeDto> employeeDtoList = mapper.mapIterable(employeePage, EmployeeDto.class);
//        model.addAttribute("employees", employeeDtoList);
//
//        List<Department> departmentList = departmentService.findAll(Sort.by("number").ascending());
//        List<DepartmentDto> departmentDtoList = mapper.mapIterable(departmentList, DepartmentDto.class);
//        model.addAttribute("departments", departmentDtoList);
        return "vacancies/vacancies_new";
    }

//    @PostMapping
//    public String saveNewVacancy(@Valid VacancyDto vacancyDto,
//                             BindingResult result,
//                             Model model) {
//        if (result.hasErrors()) {
//            addDepartmentAndListOfEmployeesToModel(model, vacancyDto.getDepartmentDto().getId());
//            return "vacancies/vacancies_new";
//        } else {
//
//            if (vacancyDto.getEmployeeDto() != null && vacancyDto.getEmployeeDto().getPersonnelNumber() == -1) {
//                vacancyDto.setEmployeeDto(null);
//            }
//            System.out.println(vacancyDto);
//            System.out.println(vacancyDto.getDepartmentDto());
//            System.out.println(vacancyDto.getEmployeeDto());
//            Vacancy vacancy = mapper.map(vacancyDto, Vacancy.class);
//            vacancyService.save(vacancy);
//            return "redirect:/vacancies";
//        }
//    }

    private void addDepartmentAndListOfEmployeesToModel(Model model,
                                                        Long departmentId) {
//        Department department = departmentService.findById(departmentId);
//        DepartmentDto departmentDto = mapper.map(department, DepartmentDto.class);
//        model.addAttribute("department", departmentDto);
//        List<Employee> employeePage = employeeService.findAllNotAssignedToAnyVacancy();
//        List<EmployeeDto> employeeDtoList = mapper.mapIterable(employeePage, EmployeeDto.class);
//        model.addAttribute("employees", employeeDtoList);
    }

    @GetMapping("/{id}/edit")
    public String showUpdateVacancyForm(@PathVariable("id") Long id,
                                 Model model) {
//        Vacancy vacancy = vacancyService.findById(id);
//        VacancyDto vacancyDto = mapper.map(vacancy, VacancyDto.class);
//        model.addAttribute("vacancy", vacancyDto);
//        List<Employee> employeeList = employeeService.findAllNotAssignedToAnyVacancy();
//        List<EmployeeDto> employeeDtoList = mapper.mapIterable(employeeList, EmployeeDto.class);
//        model.addAttribute("employees", employeeDtoList);
        return "vacancies/vacancies_edit";
    }

//    @PatchMapping("/{id}")
//    public String updateVacancy(@PathVariable("id") Long id,
//                                @Valid VacancyDto vacancyDto,
//                                BindingResult result) {
//        if (result.hasErrors()) {
//            vacancyDto.setId(id);
//            return "vacancies/vacancies_edit";
//        } else {
//            if (vacancyDto.getEmployeeDto() != null && vacancyDto.getEmployeeDto().getPersonnelNumber() == -1) {
//                vacancyDto.setEmployeeDto(null);
//            }
//            Vacancy vacancy = mapper.map(vacancyDto, Vacancy.class);
//            vacancyService.save(vacancy);
//            return "redirect:/vacancies/" + vacancyDto.getId();
//        }
//    }

    @DeleteMapping("/{id}")
    public String deleteVacancy(@PathVariable("id") Long id) {
        Vacancy vacancy = vacancyService.findById(id);
        Long departmentId = vacancy.getDepartment().getId();
        vacancyService.deleteById(id);
        return "redirect:/vacancies";
    }
}

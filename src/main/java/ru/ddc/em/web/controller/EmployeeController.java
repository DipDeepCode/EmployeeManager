package ru.ddc.em.web.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ddc.em.persistence.entity.Employee;
import ru.ddc.em.persistence.entity.Vacancy;
import ru.ddc.em.service.EmployeeService;
import ru.ddc.em.service.VacancyService;
import ru.ddc.em.utils.custommapper.CustomMapper;
import ru.ddc.em.web.dto.EmployeeDto;

import jakarta.validation.Valid;
import ru.ddc.em.web.dto.VacancyDto;

import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final VacancyService vacancyService;
    private final CustomMapper mapper;

    public EmployeeController(EmployeeService employeeService,
                              VacancyService vacancyService,
                              CustomMapper mapper) {
        this.employeeService = employeeService;
        this.vacancyService = vacancyService;
        this.mapper = mapper;
    }

    @GetMapping
    public String index(Model model) {
        Page<Employee> employeePage = employeeService.findAll(0, 15);
        List<EmployeeDto> employeeDtoList = mapper.mapIterable(employeePage, EmployeeDto.class);
        model.addAttribute("employees", employeeDtoList);
        return "employees/employee-index";
    }

    @GetMapping("/{personnelNumber}")
    public String show(@PathVariable("personnelNumber") Long personnelNumber,
                       Model model) {
        Employee employee = employeeService.findById(personnelNumber);
        EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);
        model.addAttribute("employeeDto", employeeDto);
        Vacancy vacancy = employee.getVacancy();
        VacancyDto vacancyDto = vacancy != null ? mapper.map(vacancy, VacancyDto.class) : null;
        model.addAttribute("vacancyDto", vacancyDto);
        return "employees/employee-show";
    }

    @GetMapping("/{personnelNumber}/edit")
    public String edit(@PathVariable("personnelNumber") Long personnelNumber,
                       Model model) {
        Employee employee = employeeService.findById(personnelNumber);
        EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);
        model.addAttribute("employeeDto", employeeDto);
        Vacancy vacancy = employee.getVacancy();
        VacancyDto vacancyDto = vacancy != null ? mapper.map(vacancy, VacancyDto.class) : null;
        model.addAttribute("vacancyDto", vacancyDto);
        return "employees/employee-edit";
    }

    @PatchMapping("/{personnelNumber}")
    public String update(@PathVariable("personnelNumber") Long personnelNumber,
                         @ModelAttribute("employee") @Valid EmployeeDto employeeDto,
                         BindingResult result) {
        if (result.hasErrors()) {
            return "employees/employee-edit";
        } else {
            Employee employee = mapper.map(employeeDto, Employee.class);
            employeeService.save(employee);
            return "redirect:/employees/" + personnelNumber;
        }
    }

    @GetMapping("/create")
    public String create(@ModelAttribute("employee") EmployeeDto employeeDto) {
        return "employees/employee-create";
    }

    @PostMapping
    public String store(@ModelAttribute("employee") @Valid EmployeeDto employeeDto,
                        BindingResult result) {
        if (result.hasErrors()) {
            return "employees/employee-create";
        } else {
            Employee employee = mapper.map(employeeDto, Employee.class);
            employeeService.save(employee);
            return "redirect:/employees";
        }
    }

    @PatchMapping("/{personnelNumber}/removeFromVacancy")
    public String removeFromVacancy(@PathVariable("personnelNumber") Long personnelNumber,
                                    Model model) {
        employeeService.removeFromVacancy(personnelNumber);
        return "redirect:/employees/" + personnelNumber;
    }

    @GetMapping("/{personnelNumber}/transfer")
    public String transferToAnotherVacancy(@PathVariable("personnelNumber") Long personnelNumber,
                                           Model model) {
        Employee employee = employeeService.findById(personnelNumber);
        EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);
        model.addAttribute("employeeDto", employeeDto);

        Vacancy vacancy = employee.getVacancy();
        VacancyDto vacancyDto = vacancy != null ? mapper.map(vacancy, VacancyDto.class) : null;
        model.addAttribute("vacancyDto", vacancyDto);

        List<Vacancy> vacancyList = vacancyService.findByEmployeeNull();
        List<VacancyDto> vacancyDtoList = mapper.mapIterable(vacancyList, VacancyDto.class);
        model.addAttribute("vacancyDtoList", vacancyDtoList);
        return "/employees/employee-transfer";
    }

    @PatchMapping("/{personnelNumber}/transfer")
    public String transferToAnotherVacancy(@PathVariable("personnelNumber") Long personnelNumber,
                                           @RequestParam("vacancyId") Long vacancyId,
                                           Model model) {
        employeeService.transferToAnotherVacancy(personnelNumber, vacancyId);
        return "redirect:/employees/" + personnelNumber;
    }

    @GetMapping("/{personnelNumber}/appoint")
    public String appointToVacancy(@PathVariable("personnelNumber") Long personnelNumber,
                                   Model model) {
        Employee employee = employeeService.findById(personnelNumber);
        EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);
        model.addAttribute("employeeDto", employeeDto);

        List<Vacancy> vacancyList = vacancyService.findByEmployeeNull();
        List<VacancyDto> vacancyDtoList = mapper.mapIterable(vacancyList, VacancyDto.class);
        model.addAttribute("vacancyDtoList", vacancyDtoList);
        return "/employees/employee-appoint";
    }

    @PatchMapping("/{personnelNumber}/appoint")
    public String appointToVacancy(@PathVariable("personnelNumber") Long personnelNumber,
                                   @RequestParam("vacancyId") Long vacancyId,
                                   Model model) {
        employeeService.appointToVacancy(personnelNumber, vacancyId);
        return "redirect:/employees/" + personnelNumber;
    }

    @DeleteMapping("/{personnelNumber}")
    public String destroy(@PathVariable("personnelNumber") Long personnelNumber) {
        employeeService.deleteById(personnelNumber);
        return "redirect:/employees";
    }
}

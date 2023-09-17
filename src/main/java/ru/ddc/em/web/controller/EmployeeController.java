package ru.ddc.em.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Controller
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final VacancyService vacancyService;
    private final CustomMapper mapper;
    private Integer previousPageNo = 1;

    public EmployeeController(EmployeeService employeeService,
                              VacancyService vacancyService,
                              CustomMapper mapper) {
        this.employeeService = employeeService;
        this.vacancyService = vacancyService;
        this.mapper = mapper;
    }

    @GetMapping
    public String index(@RequestParam(value = "page", required = false) Integer pageNo,
                        Model model) {
        pageNo = pageNo == null ? previousPageNo : pageNo;
        previousPageNo = pageNo;
        Page<EmployeeDto> employeeDtoPage = employeeService.findAllDto(pageNo);
        model.addAttribute("employeeDtoPage", employeeDtoPage);
        List<Integer> pageNumbers = getPagesList(employeeDtoPage);
        model.addAttribute("pageNumbers", pageNumbers);
        return "employees/employee-index";
    }

    private List<Integer> getPagesList(Page<EmployeeDto> employeeDtoPage) {
        return IntStream.rangeClosed(1, employeeDtoPage.getTotalPages())
                .boxed()
                .collect(Collectors.toList());
    }

    @GetMapping("/{personnelNumber}")
    public String show(@PathVariable("personnelNumber") Long personnelNumber,
                       Model model) {
        EmployeeDto employeeDto = employeeService.findDtoById(personnelNumber);
        model.addAttribute("employeeDto", employeeDto);
        return "employees/employee-show";
    }

    @GetMapping("/{personnelNumber}/edit")
    public String edit(@PathVariable("personnelNumber") Long personnelNumber,
                       Model model) {
        Employee employee = employeeService.findById(personnelNumber);
        EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);
        model.addAttribute("employeeDto", employeeDto);
//        Vacancy vacancy = employee.getVacancy();
//        VacancyDto vacancyDto = vacancy != null ? mapper.map(vacancy, VacancyDto.class) : null;
//        model.addAttribute("vacancyDto", vacancyDto);
        return "employees/employee-edit";
    }

    @PatchMapping("/{personnelNumber}")
    public String update(@PathVariable("personnelNumber") Long personnelNumber,
                         @ModelAttribute("employeeDto") @Valid EmployeeDto employeeDto,
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
    public String create(@ModelAttribute("employeeDto") EmployeeDto employeeDto) {
        return "employees/employee-create";
    }

    @PostMapping
    public String store(@ModelAttribute("employeeDto") @Valid EmployeeDto employeeDto,
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

//        Vacancy vacancy = employee.getVacancy();
//        VacancyDto vacancyDto = vacancy != null ? mapper.map(vacancy, VacancyDto.class) : null;
//        model.addAttribute("vacancyDto", vacancyDto);

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

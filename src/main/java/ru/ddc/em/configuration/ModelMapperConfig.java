package ru.ddc.em.configuration;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ddc.em.persistence.model.Employee;
import ru.ddc.em.persistence.model.Vacancy;
import ru.ddc.em.web.dto.EmployeeDto;
import ru.ddc.em.web.dto.VacancyDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class ModelMapperConfig {
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//        modelMapper.getConfiguration().setAmbiguityIgnored(true);
//        Converter<LocalDate, String> dateToStringConverter = mappingContext -> mappingContext.getSource().format(dateFormat);
//        Converter<String, LocalDate> stringToDateConverter = mappingContext -> LocalDate.parse(mappingContext.getSource());
        Converter<Short, String> shortToStringConverter = mappingContext -> String.format("%05d", mappingContext.getSource());

        modelMapper.createTypeMap(Employee.class, EmployeeDto.class)
                .addMappings(expression -> expression.using(shortToStringConverter)
                        .map(Employee::getPersonnelNumber, EmployeeDto::setPersonnelNumber));
//                .addMappings(expression -> expression.using(dateToStringConverter)
//                        .map(Employee::getBirthdate, EmployeeDto::setBirthdate));

//        modelMapper.createTypeMap(EmployeeDto.class, Employee.class)
//                .addMappings(expression -> expression.using(stringToDateConverter)
//                        .map(EmployeeDto::getBirthdate, Employee::setBirthdate));

        modelMapper.createTypeMap(Vacancy.class, VacancyDto.class)
                .addMapping(Vacancy::getDepartment, VacancyDto::setDepartmentDto)
                .addMapping(Vacancy::getEmployee, VacancyDto::setEmployeeDto);
        modelMapper.createTypeMap(VacancyDto.class, Vacancy.class)
                .addMapping(VacancyDto::getDepartmentDto, Vacancy::setDepartment)
                .addMapping(VacancyDto::getEmployeeDto, Vacancy::setEmployee);

        return modelMapper;
    }
}

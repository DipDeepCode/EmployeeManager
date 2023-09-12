package ru.ddc.em.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ddc.em.persistence.entity.Employee;
import ru.ddc.em.persistence.entity.Vacancy;
import ru.ddc.em.web.dto.EmployeeDto;
import ru.ddc.em.web.dto.VacancyDto;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.createTypeMap(Employee.class, EmployeeDto.class)
                .addMapping(Employee::getVacancy, EmployeeDto::setVacancyDto);

        modelMapper.createTypeMap(Vacancy.class, VacancyDto.class)
                .addMapping(Vacancy::getDepartment, VacancyDto::setDepartmentDto);

        return modelMapper;
    }
}

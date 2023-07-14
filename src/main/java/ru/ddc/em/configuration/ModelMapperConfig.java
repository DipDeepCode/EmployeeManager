package ru.ddc.em.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ddc.em.persistence.model.Vacancy;
import ru.ddc.em.web.dto.VacancyDto;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.createTypeMap(Vacancy.class, VacancyDto.class)
                .addMapping(Vacancy::getDepartment, VacancyDto::setDepartmentDto)
                .addMapping(Vacancy::getEmployee, VacancyDto::setEmployeeDto);
        modelMapper.createTypeMap(VacancyDto.class, Vacancy.class)
                .addMapping(VacancyDto::getDepartmentDto, Vacancy::setDepartment)
                .addMapping(VacancyDto::getEmployeeDto, Vacancy::setEmployee);
        return modelMapper;
    }
}

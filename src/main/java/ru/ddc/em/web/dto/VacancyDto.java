package ru.ddc.em.web.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VacancyDto {
    private Long id;
    private String position;
    private Long salary;
    private DepartmentDto departmentDto;
}

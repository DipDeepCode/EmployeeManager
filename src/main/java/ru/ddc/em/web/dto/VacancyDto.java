package ru.ddc.em.web.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VacancyDto {
    private Long id;
    private String position;
    private Long salary;
    private DepartmentDto departmentDto;
}

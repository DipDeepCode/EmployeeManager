package ru.ddc.em.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DepartmentDto {
    private Long id;
    private String number;
    private String name;
}

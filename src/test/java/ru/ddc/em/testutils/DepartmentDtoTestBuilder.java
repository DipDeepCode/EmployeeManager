package ru.ddc.em.testutils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.ddc.em.persistence.entity.Vacancy;
import ru.ddc.em.web.dto.DepartmentDto;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aDepartmentDto")
@With
public class DepartmentDtoTestBuilder implements TestBuilder<DepartmentDto> {
    private Long id;
    private String number = "department_number";
    private String name = "department_name";
    private List<Vacancy> vacancyList = new ArrayList<>();

    @Override
    public DepartmentDto build() {
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setId(id);
        departmentDto.setName(name);
        departmentDto.setNumber(number);
        return departmentDto;
    }
}

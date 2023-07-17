package ru.ddc.em.testutils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.ddc.em.persistence.entity.Department;
import ru.ddc.em.persistence.entity.Vacancy;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aDepartment")
@With
public class DepartmentTestBuilder implements TestBuilder<Department> {
    private Long id;
    private String number = "department_number";
    private String name = "department_name";
    private List<Vacancy> vacancyList = new ArrayList<>();

    @Override
    public Department build() {
        Department department = new Department();
        department.setId(id);
        department.setName(name);
        department.setNumber(number);
        department.setVacancies(vacancyList);
        return department;
    }
}

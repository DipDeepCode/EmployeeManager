package ru.ddc.em.testutils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.ddc.em.persistence.entity.Department;
import ru.ddc.em.persistence.entity.Employee;
import ru.ddc.em.persistence.entity.Vacancy;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aVacancy")
@With
public class VacancyTestBuilder implements TestBuilder<Vacancy> {
    private Long id;
    private String position = "position";
    private float salary = 100;
    private Department department = null;
    private Employee employee = null;

    @Override
    public Vacancy build() {
        Vacancy vacancy = new Vacancy();
        vacancy.setId(id);
        vacancy.setPosition(position);
        vacancy.setSalary(salary);
        vacancy.setDepartment(department);
        vacancy.setEmployee(employee);
        return vacancy;
    }
}

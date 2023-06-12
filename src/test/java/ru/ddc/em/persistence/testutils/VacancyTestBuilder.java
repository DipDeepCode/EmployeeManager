package ru.ddc.em.persistence.testutils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.ddc.em.persistence.model.Department;
import ru.ddc.em.persistence.model.Employee;
import ru.ddc.em.persistence.model.Vacancy;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aVacancy")
@With
public class VacancyTestBuilder implements TestBuilder<Vacancy> {
    private String position = "position";
    private float salary = 100;
    private Department department = null;
    private Employee employee = null;

    @Override
    public Vacancy build() {
        Vacancy vacancy = new Vacancy();
        vacancy.setPosition(position);
        vacancy.setSalary(salary);
        vacancy.setDepartment(department);
        vacancy.setEmployee(employee);
        return vacancy;
    }
}

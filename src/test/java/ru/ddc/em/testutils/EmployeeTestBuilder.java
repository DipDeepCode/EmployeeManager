package ru.ddc.em.testutils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.ddc.em.persistence.entity.Employee;
import ru.ddc.em.persistence.entity.Vacancy;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aEmployee")
@With
public class EmployeeTestBuilder implements TestBuilder<Employee> {
    private Long personnelNumber;
    private String firstname = "firstname";
    private String lastname = "lastname";
    private String patronymic = "patronymic";
    private Vacancy vacancy = null;

    @Override
    public Employee build() {
        Employee employee = new Employee();
        employee.setPersonnelNumber(personnelNumber);
        employee.setFirstname(firstname);
        employee.setLastname(lastname);
        employee.setPatronymic(patronymic);
        employee.setVacancy(vacancy);
        return employee;
    }
}

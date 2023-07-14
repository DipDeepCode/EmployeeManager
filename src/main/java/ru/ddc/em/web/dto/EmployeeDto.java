package ru.ddc.em.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeeDto {
    private Long personnelNumber;
    private String firstname;
    private String lastname;
    private String patronymic;
    private LocalDate birthdate;
    private String email;
    private String telephoneNumber;

    @Override
    public String toString() {
        return "EmployeeDto{" +
                "personnelNumber=" + personnelNumber +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthDate=" + birthdate +
                ", email='" + email + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                '}';
    }
}

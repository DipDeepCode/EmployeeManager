package ru.ddc.em.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeeDto {

    private Long personnelNumber;

    @NotBlank(message = "Имя обязательно для заполнения")
    @Size(min = 2, max = 30, message = "Имя должно быть от 2 до 30 символов")
    private String firstname;

    @NotBlank(message = "Фамилия обязательна для заполнения")
    @Size(min = 2, max = 30, message = "Фамилия должна быть от 2 до 30 символов")
    private String lastname;

    @NotBlank(message = "Отчество обязательно для заполнения")
    @Size(min = 2, max = 30, message = "Отчество должно быть от 2 до 30 символов")
    private String patronymic;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    @Email(message = "Некорректный email")
    private String email;

    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{10}$",
            message = "Некорректный телефонный номер")
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

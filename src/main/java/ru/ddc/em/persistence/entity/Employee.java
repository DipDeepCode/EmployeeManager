package ru.ddc.em.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Entity
@Table(name = "employee")
public class Employee {
    public static final Sort defaultSort = Sort.by("personnelNumber").ascending();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personnel_number", nullable = false)
    private Long personnelNumber;

    @Column(name = "firstname", length = 64, nullable = false)
    private String firstname;

    @Column(name = "lastname", length = 64, nullable = false)
    private String lastname;

    @Column(name = "patronymic", length = 64)
    private String patronymic;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "email")
    private String email;

    @Column(name = "telephone_number")
    private String telephoneNumber;

    @ToString.Exclude
    @OneToOne(mappedBy = "employee")
    private Vacancy vacancy;
}
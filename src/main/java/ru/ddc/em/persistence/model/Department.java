package ru.ddc.em.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "department")
public class Department {
    public static final Sort defaultSort = Sort.by("number").ascending();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "number", length = 32, nullable = false, unique = true)
    private String number;

    @Column(name = "name", length = 64, nullable = false, unique = true)
    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "department")
    private List<Vacancy> vacancies = new ArrayList<>();

    public void addVacancy(Vacancy vacancy) {
        if (vacancy == null) {
            throw new NullPointerException("Error add vacancy. Vacancy is null");
        }
        if (vacancy.getDepartment() != null) {
            throw new IllegalStateException("Vacancy already assigned to an Department");
        }
        if (vacancies == null) {
            vacancies = new ArrayList<>();
        }
        vacancies.add(vacancy);
        vacancy.setDepartment(this);
    }

    public void removeVacancy(Vacancy vacancy) {
        if (vacancy == null) {
            throw new NullPointerException("Error remove vacancy. Vacancy is null");
        }
        if (vacancy.getDepartment() == null) {
            throw new IllegalStateException("Vacancy already removed");
        }
        if (vacancies != null) {
            vacancies.remove(vacancy);
            vacancy.setDepartment(null);
        }
    }
}
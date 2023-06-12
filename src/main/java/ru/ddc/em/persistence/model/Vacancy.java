package ru.ddc.em.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@ToString
@Entity
@Table(name = "vacancy")
public class Vacancy {
    public static final Sort defaultSort = Sort.by("position").ascending();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "position", length = 64, nullable = false)
    private String position;

    @Column(name = "salary", nullable = false)
    private Float salary;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_personnel_number")
    private Employee employee;

}
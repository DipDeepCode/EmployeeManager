package ru.ddc.em.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@Entity
@Table(name = "vacancy")
public class Vacancy {
    public static final Sort defaultSort = Sort.by("department_number", "position").ascending();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "position", length = 64, nullable = false)
    private String position;

    @Column(name = "salary", nullable = false)
    private Float salary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_personnel_number", unique = true)
    private Employee employee;
}

package com.springboot.insurance.model;

import com.springboot.insurance.enums.EmployeeRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private EmployeeRole employeeRole;

    private boolean isActive = true;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}

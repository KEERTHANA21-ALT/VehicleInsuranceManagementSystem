package com.springboot.insurance.model;


import com.springboot.insurance.enums.PlanType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "insurance_plan")
public class InsurancePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PlanType planType;

    private double basePremium;

    private double coverageAmount;

    private boolean inspectionRequired = true;

    private boolean isActive = true;


}

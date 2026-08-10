package com.springboot.insurance.model;


import com.springboot.insurance.enums.ProposalStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double premiumAmount;

    private double basePremium;

    private double discount;

    private boolean isActive = true;

    private boolean policyCreated = false;

    @Enumerated(EnumType.STRING)
    private ProposalStatus proposalStatus;

    @CreationTimestamp
    private Instant proposalDate;

    @ManyToOne
    @JoinColumn(name = "policy_holder_id")
    private PolicyHolder policyHolder;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "insurance_plan_id")
    private InsurancePlan insurancePlan;


}

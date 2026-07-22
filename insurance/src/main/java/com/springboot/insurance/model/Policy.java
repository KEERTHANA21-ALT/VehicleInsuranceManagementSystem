package com.springboot.insurance.model;


import com.springboot.insurance.enums.PolicyStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private PolicyStatus policyStatus;

    @Column(name = "policy_number", nullable = false, unique = true)
    private String policyNumber;

    @OneToOne
    @JoinColumn(name = "proposal_id")
    private Proposal proposal;

}

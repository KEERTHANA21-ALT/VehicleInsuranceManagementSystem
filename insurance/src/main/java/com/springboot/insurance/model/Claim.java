package com.springboot.insurance.model;


import com.springboot.insurance.enums.ClaimStatus;
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
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double claimAmount;

    private boolean isActive = true;

    @Enumerated(EnumType.STRING)
    private ClaimStatus claimStatus;

    @CreationTimestamp
    private Instant claimDate;

    @Column(nullable = false)
    private String claimReason;

    @Column(nullable = false)
    private String claimRemarks;

    @ManyToOne
    @JoinColumn(name = "policy_id")
    private Policy policy;


}
